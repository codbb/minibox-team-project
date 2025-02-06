package dev.mini.minibox.controllers;

import dev.mini.minibox.entities.EmailTokenEntity;
import dev.mini.minibox.entities.UserEntity;
import dev.mini.minibox.results.CommonResult;
import dev.mini.minibox.results.Result;
import dev.mini.minibox.services.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 로그인
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getIndex(HttpSession session, UserEntity user) {
        Result result = this.userService.login(user);
        if (result == CommonResult.SUCCESS) {
            session.setAttribute("user", user);
        }
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        return response.toString();
    }

    // users 테이블에 회원 등록
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postIndex(HttpServletRequest request, UserEntity user) throws MessagingException {
        Result result = this.userService.register(request, user);
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        return response.toString();
    }

    // 아이디(이메일) 찾기
    @RequestMapping(value = "/recover-email", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRecoverEmail(UserEntity user) {
        Result result = this.userService.recoverEmail(user);
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        if (result == CommonResult.SUCCESS) {
            response.put("email", user.getEmail());
        }
        return response.toString();
    }

    // 비밀번호 변경 페이지 (변경할 비밀번호 입력 하는 페이지)
    @RequestMapping(value = "/recover-password", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverPassword(@RequestParam(value = "userEmail", required = false) String userEmail,
                                           @RequestParam(value = "key", required = false) String key) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userEmail", userEmail);
        modelAndView.addObject("key", key);
        modelAndView.setViewName("user/recoverPassword");
        return modelAndView;
    }

    // 비밀번호 변경 수정 (변경할 비밀번호 입력 시 처리)
    @RequestMapping(value = "/recover-password", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchRecoverPassword(EmailTokenEntity emailToken,
                                       @RequestParam(value = "password", required = false) String password) {
        Result result = this.userService.resolveRecoverPassword(emailToken, password);
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        return response.toString();
    }

    // 아이디(이메일)로 비밀번호 찾기
    @RequestMapping(value = "/recover-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRecoverPassword(HttpServletRequest request, @RequestParam(value = "email", required = false) String email) throws MessagingException {
        Result result = this.userService.provokeRecoverPassword(request, email);
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        return response.toString();
    }

    // 받은 이메일에서 인증하기 버튼 누르면 나오는 페이지
    @RequestMapping(value = "/validate-email-token", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getValidateEmailToken(EmailTokenEntity emailToken) {
        Result result = this.userService.validateEmailToken(emailToken);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject(Result.NAME, result.nameToLower());
        modelAndView.setViewName("user/validateEmailToken");
        return modelAndView;
    }

    // 유저 정보 수정하기
    @RequestMapping(value = "/modify-user", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchModifyUser(@SessionAttribute(value = "user", required = false) UserEntity user,
                                  UserEntity userEntity) {
        String userEmail = (user != null) ? user.getEmail() : null;
        Result result = this.userService.resolveModifyUser(userEmail, userEntity);
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        return response.toString();
    }

    // 회원탈퇴
    @RequestMapping(value = "/cancel-user", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteCancelUser(@SessionAttribute(value = "user", required = false) UserEntity user) {
        String userEmail = (user != null) ? user.getEmail() : null;
        Result result = this.userService.deleteCancelUser(userEmail);
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        return response.toString();
    }

    // 마이페이지에서 비밀번호 변경
    @RequestMapping(value = "/change-password", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchChangePassword(@SessionAttribute(value = "user", required = false) UserEntity user,
                                      @RequestParam(value = "beforePassword", required = false) String beforePassword,
                                      @RequestParam(value = "newPassword", required = false) String newPassword) {
        String userEmail = (user != null) ? user.getEmail() : null;
        Result result = this.userService.changePassword(userEmail, beforePassword, newPassword);
        JSONObject response = new JSONObject();
        response.put(Result.NAME, result.nameToLower());
        return response.toString();
    }
}
