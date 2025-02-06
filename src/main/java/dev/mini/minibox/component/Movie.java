package dev.mini.minibox.component;

import dev.mini.minibox.entities.MovieDetailEntity;
import dev.mini.minibox.entities.MovieEntity;
import dev.mini.minibox.services.MovieDetailService;
import dev.mini.minibox.services.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
public class Movie {

    private final MovieService movieService;
    private final MovieDetailService movieDetailService;

    @Autowired
    public Movie(MovieService movieService, MovieDetailService movieDetailService) {

        this.movieService = movieService;
        this.movieDetailService = movieDetailService;
    }

    // 개봉상태 업데이트
    public void scheduleMovieStatusUpdate() {
        movieService.updateMovieStatuses();
    }

    @Transactional
    public void BoxOfficeData() {
        // 한국 영화 진흥 위원회 서비스 키
        String key = "e92aaeb021a64ac0d9f7c538a19fb1df";
        // 파싱한 데이터를 저장할 변수
        String result = "";

        // 일별 박스오피스 targetDt 어제 날짜로 구하기
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        calendar.add(Calendar.DATE, -1);
        String targetDt = formatter.format(calendar.getTime());

        try {
            URL url = new URL("https://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json?key=" + key + "&targetDt=" + targetDt);
//            log.info("Retrieved BoxOfficeUrl: {}", url);

            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            result = reader.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            JSONObject boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");
            JSONArray boxOfficeList = (JSONArray) boxOfficeResult.get("dailyBoxOfficeList");


            for (Object obj : boxOfficeList) {
                JSONObject boxOffice = (JSONObject) obj;

                // 데이터 추출
                MovieEntity movieEntity = MovieEntity.builder()
                        .movieCd((String) boxOffice.get("movieCd"))
                        .movieNm((String) boxOffice.get("movieNm"))
                        .rank(Integer.parseInt((String) boxOffice.get("rank")))
                        .openDt((String) boxOffice.get("openDt")) // YYYY-MM-DD
                        .audiAcc(Integer.parseInt((String) boxOffice.get("audiAcc")))
                        .build();

                // 저장
                String movieCd = (String) boxOffice.get("movieCd");
                // 같은 영화코드가 존재하는지 확인
                MovieEntity existingEntity = movieService.findByMovieCd(movieCd);
                if (existingEntity != null && existingEntity.getMovieCd().equals(movieCd)) {
                    // 기존 데이터가 있으면 업데이트
                    existingEntity.update(movieEntity);
                    movieService.saveMovie(existingEntity);
//                    log.info("Update BoxOfficeData Success: movieCd={}", movieCd);

                } else {
                    // 새 데이터 저장
                    movieService.saveMovie(movieEntity);
//                    log.info("Insert BoxOfficeData Success: movieCd={}", movieCd);
                }
            }

        } catch (Exception e) {
//            log.error("Failed to process BoxOfficeData: {}", e.getMessage(), e);
        }

    }

    @Transactional
    public void MovieData() {
        // 한국 영화 진흥 위원회 서비스 키
        String key = "e92aaeb021a64ac0d9f7c538a19fb1df";
        // 파싱한 데이터를 저장할 변수
        String result = "";

        // 한국 영화 데이터 베이스 인증 키
        String dataKey = "JI2NJ01L6WLZQWR1P3D2";

        List<String> movieCdList = movieService.getAllMovieCds();
//        log.info("Retrieved movieCdList: {}", movieCdList);
        List<String> movieNmList = movieService.getAllMovieNms();
//        log.info("Retrieved movieNmList: {}", movieNmList);
        List<String> openDtList = movieService.getAllOpenDts();
//        log.info("Retrieved openDtList: {}", openDtList);

        Map<String, Map<String, String>> movieDataMap = IntStream.range(0, Math.min(Math.min(movieCdList.size(), movieNmList.size()), openDtList.size()))
                .boxed()
                .collect(Collectors.toMap(
                        movieCdList::get,
                        i -> Map.of(
                                "movieNm", movieNmList.get(i),
                                "openDt", openDtList.get(i)
                        )
                ));

        for (Map.Entry<String, Map<String, String>> entry : movieDataMap.entrySet()) {
            String movieCd = entry.getKey(); // 영화코드
            Map<String, String> movieDetails = entry.getValue();
            String movieNm = movieDetails.get("movieNm"); // 영화제목
            String openDt = movieDetails.get("openDt"); // 영화 개봉일

            try {
                // 영화 진흥 위원회 API 에서 가져올 데이터
                URL url = new URL("https://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=" + key + "&movieCd=" + movieCd);
//                log.info("Retrieved MovieInfoUrl: {}", url);

                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                result = reader.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONObject movieInfoResult = (JSONObject) jsonObject.get("movieInfoResult");
                JSONObject movieInfo = (JSONObject) movieInfoResult.get("movieInfo");

                // 상영 타입
                JSONArray showTypesArray = (JSONArray) movieInfo.get("showTypes");
                List<String> showTypeList = new ArrayList<>();
                for (Object obj : showTypesArray) {
                    JSONObject showType = (JSONObject) obj;
                    String showTypeNm = (String) showType.get("showTypeNm");
                    showTypeList.add(showTypeNm.replace("디지털", "2D"));
                }

                // 영화 감독 이름
                JSONArray directorsArray = (JSONArray) movieInfo.get("directors");
                List<String> directorList = new ArrayList<>();
                for (Object obj : directorsArray) {
                    JSONObject director = (JSONObject) obj;
                    directorList.add((String) director.get("peopleNm"));
                }

                // 영화 배우 이름
                JSONArray actorsArray = (JSONArray) movieInfo.get("actors");
                List<String> actorList = new ArrayList<>();
                for (Object obj : actorsArray) {
                    JSONObject actor = (JSONObject) obj;
                    actorList.add((String) actor.get("peopleNm"));
                }

                // 영화 데이터 베이스에서 가져올 데이터
                String encodedTitle = URLEncoder.encode(movieNm, StandardCharsets.UTF_8.toString()); // 영화 제목 필터링
                // 특수 문자 처리
                encodedTitle = encodedTitle.replace("%3A", "");
                String formattedOpenDt = openDt.replace("-", ""); // 'YYYY-MM-DD' 형식에서 'YYYYMMDD' 형식으로 변환

                URL movieDataUrl = new URL("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y&title=" + encodedTitle + "&releaseDts=" + formattedOpenDt + "&ServiceKey=" + dataKey);
//                log.info("Retrieved MovieDataUrl: {}", movieDataUrl);

                BufferedReader dataReader = new BufferedReader(new InputStreamReader(movieDataUrl.openStream()));
                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = dataReader.readLine()) != null) {
                    sb.append(line);
                }

                String dataResult = sb.toString();

                // JSP 응답 파싱
                Document doc = Jsoup.parse(dataResult);
                Element jsonElement = doc.select("body").first(); // JSON 이 포함된 body 태그
                if (jsonElement == null) {
//                    log.error("No JSON element found in JSP response.");
                }

                String json = jsonElement.html();

                JSONParser dataJsonParser = new JSONParser();
                JSONObject dataJsonObject = (JSONObject) dataJsonParser.parse(json);

                JSONArray dataArray = (JSONArray) dataJsonObject.get("Data");
                JSONObject dataObject = (JSONObject) dataArray.get(0);
                JSONArray Result = (JSONArray) dataObject.get("Result");
                JSONObject resultObject;

                // 검색 결과에 오류가 있어서 'Result == null' 일 경우
                if (Result == null) {
                    // 개봉날짜 대신 영화감독 이름 가져오기
                    String directorNm = directorList.get(0);
                    String encodedDirector = URLEncoder.encode(directorNm, StandardCharsets.UTF_8.toString());

                    // url 다시 가져오기
                    URL newMovieDataUrl = new URL("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y&title=" + encodedTitle + "&director=" + encodedDirector + "&ServiceKey=" + dataKey);
//                    log.info("Retrieved MovieDataUrl: {}", newMovieDataUrl);

                    BufferedReader newDataReader = new BufferedReader(new InputStreamReader(newMovieDataUrl.openStream()));
                    StringBuilder newSB = new StringBuilder();
                    String newLine;

                    while ((newLine = newDataReader.readLine()) != null) {
                        newSB.append(newLine);
                    }

                    String newDataResult = newSB.toString();

                    // JSP 응답 파싱
                    Document newDoc = Jsoup.parse(newDataResult);
                    Element newJsonElement = newDoc.select("body").first(); // JSON 이 포함된 body 태그

                    String newJson = newJsonElement.html();

                    JSONParser newDataJsonParser = new JSONParser();
                    JSONObject newDataJsonObject = (JSONObject) newDataJsonParser.parse(newJson);

                    JSONArray newDataArray = (JSONArray) newDataJsonObject.get("Data");
                    JSONObject newDataObject = (JSONObject) newDataArray.get(0);
                    JSONArray newResult = (JSONArray) newDataObject.get("Result");
                    resultObject = (JSONObject) newResult.get(0);
                } else {
                    resultObject = (JSONObject) Result.get(0);
                }


                // 데이터 추출
                MovieDetailEntity movieDetailEntity = MovieDetailEntity.builder()
                        .movieCd((String) movieInfo.get("movieCd"))
                        .movieNm((String) movieInfo.get("movieNm"))
                        .movieNmEn((String) movieInfo.get("movieNmEn"))
                        .showTypes(showTypeList.toString().replace("[", "").replace("]", ""))
                        .directors(directorList.toString().replace("[", "").replace("]", ""))
                        .actors(actorList.toString().replace("[", "").replace("]", ""))
                        .genres(extractGenre(movieInfo))
                        .showTm((String) movieInfo.get("showTm"))
                        .watchGradeNm(extractWatchGrade(movieInfo))
                        .openDt(formattedOpenDt(movieInfo))
                        .prdtStatNm((String) movieInfo.get("prdtStatNm"))
                        .typeNm((String) movieInfo.get("typeNm"))
                        .nationNm(extractNation(movieInfo))
                        .poster(extractPoster(resultObject))
                        .content(extractContent(resultObject))
                        .build();

                // 저장
                MovieDetailEntity existingEntity = movieDetailService.findByMovieCd(movieCd);
                if (existingEntity != null && existingEntity.getMovieCd().equals(movieCd)) {
                    // 기존 데이터가 있으면 업데이트
                    existingEntity.update(movieDetailEntity);
                    movieDetailService.saveMovieInfo(existingEntity);
//                    log.info("Update MovieData Success: movieCd={}", movieCd);

                } else {
                    // 새 데이터 저장
                    movieDetailService.saveMovieInfo(movieDetailEntity);
//                    log.info("Insert MovieData Success: movieCd={}", movieCd);
                }

            } catch (Exception e) {
//                log.error("Error processing movieCd: {}, movieNm: {}, openDt: {}. Error: {}", movieCd, movieNm, openDt, e.getMessage(), e);
                continue;
            }

        }
    }

    @Transactional
    public void ComingSoonMovieData() {
        // 한국 영화 진흥 위원회 서비스 키
        String key = "e92aaeb021a64ac0d9f7c538a19fb1df";
        // 파싱한 데이터를 저장할 변수
        String result = "";
        // 한국 영화 데이터 베이스 인증 키
        String dataKey = "JI2NJ01L6WLZQWR1P3D2";

        // 가지고 올 날짜 시작 (오늘 날짜)
        Calendar calendarS = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        calendarS.add(Calendar.DATE, 0);
        String releaseDts = formatter.format(calendarS.getTime());

        // 가지고 올 날짜 끝 (두 달 뒤)
        Calendar calendarE = Calendar.getInstance();
        calendarE.add(Calendar.DATE, 60);
        String releaseDte = formatter.format(calendarE.getTime());

        try {
            // 영화데이터베이스에서 가져올 데이터
            URL dataUrl = new URL("https://api.koreafilm.or.kr/openapi-data2/wisenut/search_api/search_json2.jsp?collection=kmdb_new2&detail=Y&ServiceKey=" + dataKey + "&releaseDts=" + releaseDts + "&releaseDte=" + releaseDte + "&listCount=50");
//            log.info("Retrieved comingSoonUrl: {}", dataUrl);

            BufferedReader dataReader = new BufferedReader(new InputStreamReader(dataUrl.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = dataReader.readLine()) != null) {
                sb.append(line);
            }

            String dataResult = sb.toString();

            // JSP 응답 파싱
            Document doc = Jsoup.parse(dataResult);
            Element jsonElement = doc.select("body").first(); // JSON 이 포함된 body 태그
            if (jsonElement == null) {
//                log.error("No JSON element found in JSP response.");
            }

            String json = jsonElement.html();

            JSONParser dataJsonParser = new JSONParser();
            JSONObject dataJsonObject = (JSONObject) dataJsonParser.parse(json);

            JSONArray dataArray = (JSONArray) dataJsonObject.get("Data");
            JSONObject dataObject = (JSONObject) dataArray.get(0);
            JSONArray Result = (JSONArray) dataObject.get("Result");

            // 모든 영화 반복
            for (Object obj : Result) {
                JSONObject resultObject = (JSONObject) obj;

                // 영화 데이터 베이스 url 에서 영화 이름 가져오기
                String movieNm = (String) resultObject.get("title");
                String formattedMovieNm = movieNm.replace(" ", "");

                // 영화 데이터 베이스 url 에서 영화 감독 이름 가져오기
                JSONObject directorsObject = (JSONObject) resultObject.get("directors");
                JSONArray directorArray = (JSONArray) directorsObject.get("director");
                JSONObject director = (JSONObject) directorArray.get(0);
                String directorNm = (String) director.get("directorNm");

                // URL 인코딩 처리
                String encodedMovieNm = URLEncoder.encode(formattedMovieNm, StandardCharsets.UTF_8.toString());
                String encodedDirectorNm = URLEncoder.encode(directorNm, StandardCharsets.UTF_8.toString());

                // 영화 진흥 위원회 API 에서 가져올 데이터

                // 영화목록에서 영화코드 가져오기
                URL url = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key=" + key + "&movieNm=" + encodedMovieNm + "&directorNm=" + encodedDirectorNm);
//                log.info("Retrieved movieList url: {}", url);

                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

                result = reader.readLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
                JSONObject movieListResult = (JSONObject) jsonObject.get("movieListResult");
                // movieListResult 가 빈 배열일 때 예외 발생
                if (movieListResult == null || movieListResult.isEmpty()) {
                    continue; // 다음 영화로 넘어가기
                }
                JSONArray movieList = (JSONArray) movieListResult.get("movieList");
                // movieList 가 빈 배열일 때 예외 발생
                if (movieList == null || movieList.isEmpty()) {
                    continue; // 다음 영화로 넘어가기
                }
                JSONObject movie = (JSONObject) movieList.get(0);
                String movieCd = (String) movie.get("movieCd");

                // 영화상세정보에서 나머지 상세정보 가져오기
                URL infoUrl = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json?key=" + key + "&movieCd=" + movieCd);
//                log.info("Retrieved movieInfo url: {}", infoUrl);

                BufferedReader infoReader = new BufferedReader(new InputStreamReader(infoUrl.openStream()));

                result = infoReader.readLine();

                JSONParser infoJsonParser = new JSONParser();
                JSONObject infoJsonObject = (JSONObject) infoJsonParser.parse(result);
                JSONObject movieInfoResultObject = (JSONObject) infoJsonObject.get("movieInfoResult");
                JSONObject movieInfo = (JSONObject) movieInfoResultObject.get("movieInfo");

                // 장르
                JSONArray genres = (JSONArray) movieInfo.get("genres");
                List<String> genreList = new ArrayList<>();
                if (genres != null) {
                    for (Object type : genres) {
                        JSONObject genre = (JSONObject) type;
                        genreList.add((String) genre.get("genreNm"));
                    }
                }
                // 영화 감독 이름
                JSONArray directors = (JSONArray) movieInfo.get("directors");
                List<String> directorList = new ArrayList<>();
                if (directors != null) {
                    for (Object type : directors) {
                        JSONObject direct = (JSONObject) type;
                        directorList.add((String) direct.get("peopleNm"));
                    }
                }
                // 영화 배우 이름
                JSONArray actors = (JSONArray) movieInfo.get("actors");
                List<String> actorList = new ArrayList<>();
                if (actors != null) {
                    for (Object type : actors) {
                        JSONObject actor = (JSONObject) type;
                        actorList.add((String) actor.get("peopleNm"));
                    }
                }
                // 상영 타입
                JSONArray showTypes = (JSONArray) movieInfo.get("showTypes");
                List<String> showTypeList = new ArrayList<>();
                if (showTypes != null) {
                    for (Object type : showTypes) {
                        JSONObject showType = (JSONObject) type;
                        String showTypeNm = (String) showType.get("showTypeNm");
                        showTypeList.add(showTypeNm.replace("디지털", "2D"));
                    }
                }
                // 관람 연령
                JSONArray audits = (JSONArray) movieInfo.get("audits");
                List<String> auditList = new ArrayList<>();
                if (audits != null) {
                    for (Object type : audits) {
                        JSONObject audit = (JSONObject) type;
                        auditList.add((String) audit.get("watchGradeNm"));
                    }
                } if (auditList.isEmpty()) {
                    auditList.add("");
                }
                // 국가
                JSONArray nations = (JSONArray) movieInfo.get("nations");
                List<String> nationList = new ArrayList<>();
                if (nations != null) {
                    for (Object type : nations) {
                        JSONObject nation = (JSONObject) type;
                        nationList.add((String) nation.get("nationNm"));
                    }
                }

                // 데이터 추출
                MovieDetailEntity movieDetailEntity = MovieDetailEntity.builder()
                        .movieCd((String) movieInfo.get("movieCd"))
                        .movieNm((String) movieInfo.get("movieNm"))
                        .movieNmEn((String) movieInfo.get("movieNmEn"))
                        .showTypes(showTypeList.toString().replace("[", "").replace("]", ""))
                        .directors(directorList.toString().replace("[", "").replace("]", ""))
                        .actors(actorList.toString().replace("[", "").replace("]", ""))
                        .genres(genreList.toString().replace("[", "").replace("]", ""))
                        .showTm((String) movieInfo.get("showTm"))
                        .watchGradeNm(auditList.toString().replace("[", "").replace("]", ""))
                        .openDt(formattedOpenDt(movieInfo))
                        .prdtStatNm((String) movieInfo.get("prdtStatNm"))
                        .typeNm((String) movieInfo.get("typeNm"))
                        .nationNm(nationList.toString().replace("[", "").replace("]", ""))
                        .poster(extractPoster(resultObject))
                        .content(extractContent(resultObject))
                        .build();

                // 저장
                MovieDetailEntity existingEntity = movieDetailService.findByMovieCd(movieCd);
                if (existingEntity != null && existingEntity.getMovieCd().equals(movieCd)) {
                    // 기존 데이터가 있으면 업데이트
                    existingEntity.update(movieDetailEntity);
                    movieDetailService.saveMovieInfo(existingEntity);
//                    log.info("Update comingSoonMovieData Success: movieCd={}", movieCd);

                } else {
                    // 새 데이터 저장
                    movieDetailService.saveMovieInfo(movieDetailEntity);
//                    log.info("Insert ComingSoonMovieData Success: movieCd={}", movieCd);
                }
            }

        } catch (MalformedURLException e) {
//            log.error("Invalid URL format: {}", e.getMessage());
        } catch (IOException e) {
//            log.error("Error reading data from API: {}", e.getMessage());
        } catch (Exception e) {
//            log.error("Unexpected error: {}", e.getMessage());
        }

    }

    private String extractGenre(JSONObject movieInfo) {
        JSONArray genresArray = (JSONArray) movieInfo.get("genres");
        List<JSONObject> genresList = new ArrayList<>();
        for (Object obj : genresArray) {
            genresList.add((JSONObject) obj);
        }
        return genresList.stream()
                .map(genre -> (String) genre.get("genreNm"))
                .collect(Collectors.joining(", "));
    }

    private String extractWatchGrade(JSONObject movieInfo) {
        JSONArray auditsArray = (JSONArray) movieInfo.get("audits");
        List<JSONObject> watchGradeList = new ArrayList<>();
        for (Object obj : auditsArray) {
            watchGradeList.add((JSONObject) obj);
        }
        return watchGradeList.stream()
                .map(showType -> (String) showType.get("watchGradeNm"))
                .collect(Collectors.joining(", "));
    }

    private String formattedOpenDt(JSONObject movieInfo) {
        String openDt = movieInfo.get("openDt").toString();
        // openDt가 빈 문자열이거나 null 인 경우 처리
        if (openDt == null || openDt.trim().isEmpty()) {
            return ""; // 빈 문자열 반환
        }
        return LocalDate
                .parse(openDt, DateTimeFormatter.ofPattern("yyyyMMdd"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    } // yyyyMMdd -> yyyy-MM-dd

    private String extractNation(JSONObject movieInfo) {
        JSONArray nationsArray = (JSONArray) movieInfo.get("nations");
        List<JSONObject> nationList = new ArrayList<>();
        for (Object obj : nationsArray) {
            nationList.add((JSONObject) obj);
        }
        return nationList.stream()
                .map(nation -> (String) nation.get("nationNm"))
                .collect(Collectors.joining(", "));
    }

    private String extractContent(JSONObject resultObject) {
        JSONObject plots = (JSONObject) resultObject.get("plots");
        JSONArray plotArray = (JSONArray) plots.get("plot");
        List<JSONObject> plotTexts = new ArrayList<>();
        for (Object obj : plotArray) {
            JSONObject plot = (JSONObject) obj;
            plotTexts.add(plot);
        }
        return plotTexts.stream()
                .map(content -> (String) content.get("plotText"))
                .collect(Collectors.joining(", "));
    }

    private String extractPoster(JSONObject resultObject) {
        String posters = (String) resultObject.get("posters");
        // 개별 포스터 URL 리스트로 분리
        List<String> posterList = Arrays.asList(posters.split("\\|"));
        return posterList.stream()
                .collect(Collectors.joining(", "));
    }
}



