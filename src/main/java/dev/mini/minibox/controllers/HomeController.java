package dev.mini.minibox.controllers;

import dev.mini.minibox.entities.MovieBoxOffice;
import dev.mini.minibox.entities.UserEntity;
import dev.mini.minibox.services.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(value = "/")
public class HomeController {
    private final MovieService movieService;
    private final BookingService bookingService;
    private final ReviewService reviewService;
    private final UserService userService;
    private final MovieLikeService movieLikeService;

    @Autowired
    public HomeController(MovieService movieService, BookingService bookingService, ReviewService reviewService, UserService userService, MovieLikeService movieLikeService) {
        this.movieService = movieService;
        this.bookingService = bookingService;
        this.reviewService = reviewService;
        this.userService = userService;
        this.movieLikeService = movieLikeService;
    }

    // Home 화면 보여주는 페이지
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex(@SessionAttribute(value = "user", required = false) UserEntity user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", user);
        MovieBoxOffice[] movieBoxOffices = this.movieService.getMovieBoxOffices(true);
        modelAndView.addObject("movieBoxOffices", movieBoxOffices);
        Map<String, String> mainPosterMap = this.movieService.getMainPoster();
        modelAndView.addObject("mainPosterMap", mainPosterMap);
        Map<String, Double> getAvgRatingMap = this.reviewService.getAvgRatingByMovieCd();
        modelAndView.addObject("getAvgRatingMap", getAvgRatingMap);
        Map<Integer, Integer> movieLikeMap = this.movieService.getMovieLike();
        modelAndView.addObject("movieLikeMap", movieLikeMap);

        modelAndView.setViewName("home/index");
        return modelAndView;
    }

    // 로그아웃
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String getLogout(HttpSession session, HttpServletRequest request) {
        session.setAttribute("user", null);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    // 회원가입 보여주는 페이지
    @RequestMapping(value = "/register", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/register");
        return modelAndView;
    }

    // 아이디(이메일) 찾기 보여주는 페이지
    @RequestMapping(value = "/user-find", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getUserFind() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/user-find");
        return modelAndView;
    }

    // 비밀번호 찾기 보여주는 페이지
    @RequestMapping(value = "/pass-find", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getPassFind() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/pass-find");
        return modelAndView;
    }

    // 마이 페이지
    @RequestMapping(value = "/my", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMy(@SessionAttribute(value = "user", required = false) UserEntity user) {
        ModelAndView modelAndView = new ModelAndView();
        String userEmail = (user != null) ? user.getEmail() : null;
        modelAndView.addObject("bookings", this.bookingService.getBookingsByEmail(userEmail));
        modelAndView.addObject("reviews", this.reviewService.getReviewByUserEmail(userEmail));
        modelAndView.addObject("user", this.userService.getUser(userEmail));
        modelAndView.addObject("movieLikes", this.movieLikeService.getMovieLikesByUserEmail(userEmail));
        modelAndView.setViewName("home/my");
        return modelAndView;
    }

    // 이벤트 페이지
    @RequestMapping(value = "/event", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getEvent() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/event");
        return modelAndView;
    }

    // 스토어 페이지
    @RequestMapping(value = "/store", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getStore() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/store");
        return modelAndView;
    }

    // 혜택 페이지
    @RequestMapping(value = "/benefit", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getBenefit() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/benefit");
        return modelAndView;
    }

    // 고객센터 페이지
    @RequestMapping(value = "/support", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSupport() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home/support");
        return modelAndView;
    }

    // 영화 좋아요 기능
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> toggleMovieLike(@RequestParam(value = "movieId") int movieId,
                                                               @RequestParam(value = "userId") String userId,
                                                               @RequestParam(value = "like") boolean like) {
        Map<String, Object> response = new HashMap<>();
        boolean result = movieLikeService.toggleMovieLike(movieId, userId, like);
        response.put("result", result);
        response.put("movieId", movieId);
        response.put("userId", userId);
        response.put("like", like);
        System.out.printf("response: %s\n", response);
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Integer>> getLikedMovies(@RequestParam(value = "userId") String userId) {
        List<Integer> likedMovies = this.movieLikeService.getMovieLikeList(userId);
        return ResponseEntity.ok(likedMovies);
    }

}
