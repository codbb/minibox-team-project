package dev.mini.minibox;

import dev.mini.minibox.component.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MovieRunner implements CommandLineRunner {
    private final Movie movie;

    @Override
    public void run(String... args) throws Exception {
        runDaily();
    }

    //     스케줄링: 하루에 한 번 실행
    @Scheduled(cron = "0 0 14 * * ?")
    public void runDaily() {
        // 영화 박스오피스 데이터, 영화 상세정보 DB에 업데이트 또는 저장
        movie.BoxOfficeData();
        movie.MovieData();
        movie.ComingSoonMovieData();
        movie.scheduleMovieStatusUpdate();
    }
}
