package dev.mini.minibox.controllers;

import dev.mini.minibox.entities.*;
import dev.mini.minibox.results.theater.NameCheck;
import dev.mini.minibox.services.AreaService;
import dev.mini.minibox.services.TheaterDetailService;
import dev.mini.minibox.services.TheaterService;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@RequestMapping(value = "/theater")
public class TheaterController {
    private final AreaService areaService;
    private final TheaterService theaterService;
    private final TheaterDetailService theaterDetailService;

    @Autowired
    public TheaterController(AreaService areaService, TheaterService theaterService, TheaterDetailService theaterDetailService) {
        this.areaService = areaService;
        this.theaterService = theaterService;
        this.theaterDetailService = theaterDetailService;
    }

    // 메인 화면에서 극장을 누르면 맨 처음 보여자는 화면
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("areas", this.areaService.selectAreas()); // 전체극장 중 지역을 나타내 주는 것
        modelAndView.addObject("theaters", this.areaService.selectTheaters()); // 전체극장 중 지역을 선택하면 해당 하위 지점을 나타내 주는것
        modelAndView.setViewName("theater/list");
        return modelAndView;
    }

    // 메인 화면에서 극장을 누르면 맨 처음 보여자는 화면
    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postList(AreaEntity theater) {
        NameCheck result = this.areaService.name(theater);
        JSONObject response = new JSONObject();
        response.put("result", result.name().toLowerCase());
        return response.toString();
    }

    // 극장 화면에서 원하는 지역을 선택 후 하위 원한는 지점을 선택하였을때 보여지는 화면
    @RequestMapping(value = "/detail", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getDetail(@RequestParam(value = "brchNo", required = false, defaultValue = "1") String brchNo, HttpSession session) {
        TheaterEntity theater = this.theaterService.getTheater(brchNo);
        TheaterDetailEntity detail = this.theaterDetailService.getDetailByDetailCode(brchNo);


        if (detail == null) {
            detail = new TheaterDetailEntity();
            detail.setTitle("정보 없음");
            detail.setContent("해당 세부 정보가 존재하지 않습니다.");
        }

        UserEntity currentUser = (UserEntity) session.getAttribute("user");
        boolean isAdmin = currentUser != null && "관리자".equals(currentUser.getNickname());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("theater", theater);
        modelAndView.addObject("detail", detail);
        modelAndView.addObject("areas", this.areaService.selectAreas());
        modelAndView.addObject("theaterNames", this.areaService.selectTheaters());
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.setViewName("theater/detail");
        return modelAndView;
    }

    // 극장 화면에서 원하는 지역을 선택 후 하위 원한는 지점을 선택하였을때 보여지는 화면
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ResponseBody
    public String postDetail(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("brchNo") String brchNo) {
        TheaterDetailEntity detail = theaterDetailService.getDetailByDetailCode(brchNo);
        if (detail != null) {
            detail.setTitle(title);
            detail.setContent(content);
            theaterDetailService.update(detail);
            return "redirect:/theater/detail?brchNo=" + brchNo;
        } else {
            return "redirect:/theater/detail?brchNo=" + brchNo + "&title=해당 정보가 없습니다.";
        }
    }

    // ckeditor를 통해 관리자가 글을 수정하거나 작성하였을때 이미지를 추가하였을 경우 사용
    @RequestMapping(value = "/image", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@RequestParam(value = "index", required = false , defaultValue = "0") int index) {
        ImageEntity image = this.theaterService.getImage(index);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .ok()
                .contentLength(image.getData().length)
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .body(image.getData());
    }

    // ckeditor를 통해 관리자가 글을 수정하거나 작성하였을때 이미지를 추가하였을 경우 사용
    @RequestMapping(value = "/image", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postImage(@RequestParam(value = "upload")MultipartFile file) throws IOException {
        ImageEntity image = new ImageEntity();
        image.setData(file.getBytes());
        image.setContentType(file.getContentType());
        image.setName(file.getOriginalFilename());
        JSONObject response = new JSONObject();
        boolean result = this.theaterService.uploadImage(image);
        if (result) {
            response.put("url", "/theater/image?index=" + image.getIndex());
        }
        return response.toString();
    }

    // ckeditor를 통해 관리자가 글을 작성하거나 수정할때 사용
    @RequestMapping(value = "/write", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ModelAndView getWrite(@RequestParam(value = "brchNo", required = false, defaultValue = "0") String brchNo) {
        TheaterEntity detail = this.theaterDetailService.getDetailByBrchCode(brchNo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("detail", detail);
        modelAndView.setViewName("theater/detail");
        return modelAndView;
    }

    // ckeditor를 통해 관리자가 글을 작성하거나 수정할때 사용
    @RequestMapping(value = "/write", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postWrite(TheaterDetailEntity detail, @RequestParam(value = "brchNo") String brchNo) {
        System.out.println("Received detail : " + detail);
        System.out.println("Brch No : " + brchNo);

        detail.setBrchNo(brchNo);

        boolean result = this.theaterDetailService.updateDetail(detail);

        JSONObject response = new JSONObject();
        response.put("result", result);
        if (result) {
            response.put("brchNo", detail.getBrchNo());
        }
       return response.toString();
    }
}


