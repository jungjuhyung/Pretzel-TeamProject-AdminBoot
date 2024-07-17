package com.ict.pretzel_admin.jung.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays; // 이기찬 배열

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile; // 이기찬 멀티파트

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.jung.service.MovieService;
import com.ict.pretzel_admin.jung.tools.TMDBTools;
import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.vo.CastVO;
import com.ict.pretzel_admin.vo.CrewVO;
import com.ict.pretzel_admin.vo.MovieVO;

import lombok.RequiredArgsConstructor;

// 이기찬 구글 번역 API
import com.google.cloud.translate.Translate; // 이기찬 구글 번역 api 
import com.google.cloud.translate.TranslateOptions; // 이기찬 구글 번역 api 
import com.google.cloud.translate.Translation; // 이기찬 구글 번역 api 

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    
    @Value("${spring.cloud.gcp.storage.bucket}") // application.properties에 써둔 bucket 이름
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}") // application.properties에 써둔 bucket 이름
    private String id;

    @Autowired
    private MovieService movieService;

    @Autowired
    private TMDBTools tmdbTools;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("query") String query, @RequestParam("year") String year) {
        try {
            String encode_query = URLEncoder.encode(query, "UTF-8").replaceAll("\\+", "%20");
            String encode_year = URLEncoder.encode(year, "UTF-8").replaceAll("\\+", "%20");
            String apiURL = "";
            if (year.equals("")) {
                apiURL = "https://api.themoviedb.org/3/search/movie?query=" + encode_query + "&include_adult=true&language=ko-kr&language=en-US";
            } else {
                apiURL = "https://api.themoviedb.org/3/search/movie?query=" + encode_query + "&primary_release_year=" + encode_year + "&include_adult=true&language=ko-kr&language=en-US";
            }
            URL url = new URL(apiURL);
            String api_key = "your_api_key_here";
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + api_key);
            int responeseCode = conn.getResponseCode();
            if (responeseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String line = "";
                StringBuffer sb = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String result = sb.toString();
                return ResponseEntity.ok(result);
            }

        } catch (Exception e) {
            System.out.println("연결 실패");
        }
        return ResponseEntity.ok(0);
    }

    @PostMapping("/insert_movie")
    public ResponseEntity<?> insert_movie(@RequestHeader("Authorization") String token, MovieVO movieVO, @RequestParam("subtitle") MultipartFile subtitle) throws IOException { // 이기찬 리퀘스트파람 추가
        try {
            // 스토리지 생성
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/ict-pretzel-43373d904ced.json"));
            Storage storage = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .setProjectId(id).build().getService();
            JwtDecode jwtDecode = new JwtDecode(token); 
            String movie_id = movieVO.getMovie_id();
            String storage_folder = "";
            switch (movieVO.getThema()) {
                case "로맨스":
                storage_folder = "pretzel-romance/";
                break;
                case "코믹":
                storage_folder = "pretzel-comic/";
                break;
                case "범죄/스릴러":
                storage_folder = "pretzel-crime/";
                break;
                case "액션":
                storage_folder = "pretzel-action/";
                break;
                case "애니메이션":
                storage_folder = "pretzel-ani/";
                break;
                case "공포":
                storage_folder = "pretzel-horror/";
                break;
                default:
                break;
            }
            
            String video_ext = movieVO.getMovie().getContentType(); // 파일의 형식 ex) JPG
            String video_name = URLEncoder.encode(movieVO.getMovie().getOriginalFilename(), "UTF-8").replaceAll("\\+", "%20");
            // Cloud에 비디오 업로드
            BlobInfo blobVideoInfo = storage.createFrom(
                BlobInfo.newBuilder(bucketName, storage_folder+movieVO.getMovie().getOriginalFilename())
                .setContentType(video_ext)
                .build(),
                movieVO.getMovie().getInputStream());
                movieVO.setMovie_url(storage_folder+video_name);
                movieVO.setMovie_del_name(storage_folder+movieVO.getMovie().getOriginalFilename());
                movieVO.setSubtitle_url("");
                movieVO.setSub_del_name("");

            // Cloud에 자막 업로드
            if (movieVO.getSubtitle() != null) {
                String subtitle_ext = movieVO.getSubtitle().getContentType();
                String subtitle_name = URLEncoder.encode(movieVO.getSubtitle().getOriginalFilename(), "UTF-8").replaceAll("\\+", "%20");
                BlobInfo blobSubtitleInfo = storage.createFrom(
                    BlobInfo.newBuilder(bucketName, storage_folder+movieVO.getSubtitle().getOriginalFilename())
                    .setContentType(subtitle_ext)
                    .build(),
                    movieVO.getSubtitle().getInputStream());
                    movieVO.setSubtitle_url(storage_folder+subtitle_name);
                    movieVO.setSub_del_name(storage_folder+movieVO.getSubtitle().getOriginalFilename());

                    // 자막 번역 및 업로드 (추가)
                    Map<String, String> translatedFiles = translateAndUploadSubtitles(storage, bucketName, storage_folder, movieVO.getSubtitle()); // 이기찬 자막 번역 및 업로드 메소드 
                    movieVO.setTranslated_subtitles(translatedFiles); // 이기찬 번역된 자막 파일의 URL을 무비브이오에 저장
            }
                
            Map<String, String> detail = tmdbTools.detail(movie_id);
            movieVO.setTmdb_title(detail.get("tmdb_title"));
            movieVO.setPoster_url(detail.get("poster_url"));
            movieVO.setBackdrop_url(detail.get("backdrop_url"));
            movieVO.setRuntime(detail.get("runtime"));
            movieVO.setRelease_date(detail.get("release_date"));
            movieVO.setSynopsis(tmdbTools.translations(movie_id));
            movieVO.setTrailer_url(tmdbTools.trailer(movie_id));
            movieVO.setMovie_grade(tmdbTools.certification(movie_id));
            movieVO.setAdmin_id(jwtDecode.getAdmin_id());
            
            movieService.movie_insert(movieVO);
                
            List<CastVO> cast_list = tmdbTools.cast(movie_id);
            for (CastVO k : cast_list) {
                movieService.cast_insert(k);
            }
            
            List<CrewVO> crew_list = tmdbTools.crew(movie_id);
            for (CrewVO k : crew_list) {
                movieService.crew_insert(k);
            }
            return ResponseEntity.ok(1);
        } catch (Exception e) {
            return ResponseEntity.ok(0);
        }
    }

    @PostMapping("/update_movie")
    public ResponseEntity<?> update_movie(@RequestHeader("Authorization") String token, MovieVO movieVO) throws IOException {
        try {
            MovieVO movie_info = movieService.movie_info(movieVO.getMovie_idx());
            String movie_id = movieVO.getMovie_id();
            JwtDecode jwtDecode = new JwtDecode(token); 
            if (movieVO.getMovie() != null || !movieVO.getThema().equals(movie_info.getThema())) {
                // 스토리지 생성
                GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/ict-pretzel-43373d904ced.json"));
                Storage storage = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(id).build().getService();
                // 영화 파일 삭제
                Blob blob = storage.get(bucketName, movie_info.getMovie_del_name());
                BlobId blobId = blob.getBlobId();
                boolean deleted = storage.delete(blobId);
                // 자막 파일 삭제
                Blob sub_blob = storage.get(bucketName, movie_info.getSub_del_name());
                BlobId sub_blobId = sub_blob.getBlobId();
                boolean sub_deleted = storage.delete(sub_blobId);
                
                // 새로운 영화 및 자막 업데이트
                String storage_folder = "";
                switch (movieVO.getThema()) {
                    case "로맨스":
                    storage_folder = "pretzel-romance/";
                    break;
                    case "코믹":
                    storage_folder = "pretzel-comic/";
                    break;
                    case "범죄/스릴러":
                    storage_folder = "pretzel-crime/";
                    break;
                    case "액션":
                    storage_folder = "pretzel-action/";
                    break;
                    case "애니메이션":
                    storage_folder = "pretzel-ani/";
                    break;
                    case "공포":
                    storage_folder = "pretzel-horror/";
                    break;
                    default:
                    break;
                }
                
                String video_ext = movieVO.getMovie().getContentType(); // 파일의 형식 ex) JPG
                String video_name = URLEncoder.encode(movieVO.getMovie().getOriginalFilename(), "UTF-8").replaceAll("\\+", "%20");
                // Cloud에 비디오 업로드
                BlobInfo blobVideoInfo = storage.createFrom(
                    BlobInfo.newBuilder(bucketName, storage_folder+video_name)
                    .setContentType(video_ext)
                    .build(),
                    movieVO.getMovie().getInputStream());
                    movieVO.setMovie_url(storage_folder+video_name);
                    movieVO.setMovie_del_name(storage_folder+movieVO.getMovie().getOriginalFilename());
                    movieVO.setSubtitle_url("");
                    movieVO.setSub_del_name("");

                // Cloud에 자막 업로드
                if (movieVO.getSubtitle() != null) {
                    String subtitle_ext = movieVO.getSubtitle().getContentType();
                    String subtitle_name = URLEncoder.encode(movieVO.getSubtitle().getOriginalFilename(), "UTF-8").replaceAll("\\+", "%20");
                    BlobInfo blobSubtitleInfo = storage.createFrom(
                        BlobInfo.newBuilder(bucketName, storage_folder+subtitle_name)
                        .setContentType(subtitle_ext)
                        .build(),
                        movieVO.getSubtitle().getInputStream());
                        movieVO.setSubtitle_url(storage_folder+subtitle_name);
                        movieVO.setSub_del_name(storage_folder+movieVO.getSubtitle().getOriginalFilename());

                        // 자막 번역 및 업로드 (추가)
                        Map<String, String> translatedFiles = translateAndUploadSubtitles(storage, bucketName, storage_folder, movieVO.getSubtitle()); // 이기찬 자막 번역 및 업로드 메소드 호출
                        movieVO.setTranslated_subtitles(translatedFiles); // 이기찬 번역된 자막 파일의 URL을 무비브이오에 저장
                }
            }
            
            Map<String, String> detail = tmdbTools.detail(movie_id);
            movieVO.setTmdb_title(detail.get("tmdb_title"));
            movieVO.setPoster_url(detail.get("poster_url"));
            movieVO.setBackdrop_url(detail.get("backdrop_url"));
            movieVO.setRuntime(detail.get("runtime"));
            movieVO.setRelease_date(detail.get("release_date"));
            movieVO.setSynopsis(tmdbTools.translations(movie_id));
            movieVO.setTrailer_url(tmdbTools.trailer(movie_id));
            movieVO.setMovie_grade(tmdbTools.certification(movie_id));
            movieVO.setAdmin_id(jwtDecode.getAdmin_id());
            movieVO.setSynchro("1");
            
            movieService.movie_update(movieVO);

            return ResponseEntity.ok(1);
        } catch (Exception e) {
            return ResponseEntity.ok(0);
        }
    }

    @GetMapping("/delete_movie")
    public ResponseEntity<?> delete_movie(@RequestHeader("Authorization") String token, @RequestParam("movie_idx") String movie_idx) throws IOException {
        try {
            MovieVO movie_info = movieService.movie_info(movie_idx);
            JwtDecode jwtDecode = new JwtDecode(token); 
            // 스토리지 생성
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/ict-pretzel-43373d904ced.json"));
            Storage storage = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .setProjectId(id).build().getService();
            // 영화 파일 삭제
            Blob blob = storage.get(bucketName, movie_info.getMovie_del_name());
            BlobId blobId = blob.getBlobId();
            boolean deleted = storage.delete(blobId);
            // 자막 파일 삭제
            Blob sub_blob = storage.get(bucketName, movie_info.getSub_del_name());
            BlobId sub_blobId = sub_blob.getBlobId();
            boolean sub_deleted = storage.delete(sub_blobId);
            
            MovieVO movieVO = new MovieVO();
            movieVO.setMovie_idx(movie_idx);
            movieVO.setAdmin_id(jwtDecode.getAdmin_id());
            movieService.movie_delete(movieVO);

            return ResponseEntity.ok(1);
        } catch (Exception e) {
            return ResponseEntity.ok(0);
        }
    }

    @GetMapping("/synchro_movie")
    public ResponseEntity<?> synchro_movie(@RequestHeader("Authorization") String token) throws IOException {
        try {
            List<MovieVO> movie_list = movieService.movie_list();
            for (MovieVO k : movie_list) {
                if (!tmdbTools.detail(k.getMovie_id()).get("tmdb_title").equalsIgnoreCase(k.getTmdb_title())) {
                    movieService.movie_synchro(k.getMovie_idx());
                }
            }
            return ResponseEntity.ok(1);
        } catch (Exception e) {
            return ResponseEntity.ok(0);
        }
    }

    @Autowired
    private Paging paging;

    @GetMapping("/list_movie")
    public ResponseEntity<?> list_movie(@RequestParam("keyword") String keyword, 
            @RequestParam(value = "cPage", defaultValue = "1") String cPage) throws IOException {
        try {
            // 페이징 기법
            int count = movieService.movie_count();
            paging.setTotalRecord(count);

            if (paging.getTotalRecord() <= paging.getNumPerPage()) {
                paging.setTotalPage(1);
            } else {
                paging.setTotalPage(paging.getTotalRecord() / paging.getNumPerPage());
                if (paging.getTotalRecord() % paging.getNumPerPage() != 0) {
                    paging.setTotalPage(paging.getTotalPage() + 1);
                }
            }

            paging.setNowPage(Integer.parseInt(cPage));

            paging.setOffset(paging.getNumPerPage() * (paging.getNowPage() - 1));

            paging.setBeginBlock((int) ((paging.getNowPage() - 1) 
                    / paging.getPagePerBlock()) * paging.getPagePerBlock() + 1);

            paging.setEndBlock(paging.getBeginBlock() + paging.getPagePerBlock() - 1);

            if (paging.getEndBlock() > paging.getTotalPage()) {
                paging.setEndBlock(paging.getTotalPage());
            }

            //  DB 갔다오기
            List<MovieVO> movie_list = new ArrayList();
            if (keyword == null || keyword.equals("")) {
                movie_list = movieService.movie_list(paging);
                
            } else {
                paging.setKeyword(keyword);
                movie_list = movieService.search_list(paging);
                count = movieService.search_count(keyword);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("movie_list", movie_list);
            result.put("paging", paging);
            result.put("count", count);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.ok(0);
        }
    }

    // 이기찬

    // 자막 번역 및 업로드 메소드
    private Map<String, String> translateAndUploadSubtitles(Storage storage, String bucketName, String storage_folder, MultipartFile subtitle) throws IOException { // 이기찬 자막 파일을 번역하고 구글스토리지에 업로드하는 메소드
        Map<String, String> translatedFiles = new HashMap<>(); // 이기찬 번역된 파일의 URL을 저장할 맵
        String subtitleContent = new String(subtitle.getBytes(), "UTF-8"); // 이기찬 자막 파일의 내용을 UTF-8 문자열로 읽기

        List<String> languages = Arrays.asList("ko", "ja", "zh", "en", "fr", "de", "es", "it", "pt", "ru", "hi"); // 이기찬 번역할 언어 목록
        for (String lang : languages) { // 이기찬 각 언어에 대해 번역 수행
            String translatedContent = translateText(preprocessText(subtitleContent), lang); // 이기찬 텍스트 전처리 및 번역
            translatedContent = postprocessText(translatedContent); // 이기찬 텍스트 후처리
            String translatedFileName = subtitle.getOriginalFilename().replace(".srt", "_" + lang + ".vtt"); // 이기찬 번역된 파일의 이름 생성
            BlobInfo blobInfo = storage.create( // 이기찬 번역된 파일을 스토리지에 업로드
                BlobInfo.newBuilder(bucketName, storage_folder + translatedFileName).setContentType("text/vtt").build(), // 이기찬 BlobInfo 객체 생성
                translatedContent.getBytes() // 이기찬 번역된 파일의 내용을 바이트 배열로 변환하여 업로드
            ); // 이기찬
            translatedFiles.put(lang, storage_folder + translatedFileName); // 이기찬 번역된 파일의 URL을 맵에 저장
        } // 이기찬
        return translatedFiles; // 이기찬 번역된 파일의 URL이 저장된 맵을 반환
    } // 이기찬

    // 텍스트 전처리 메소드 (추가)
    private String preprocessText(String text) { // 이기찬 번역 전 텍스트를 전처리하는 메소드
        text = text.replaceAll("\\[.*?\\]", "");  // 이기찬 
        text = text.replaceAll("\\(.*?\\)", "");  // 이기찬 
        text = text.replaceAll("\\{.*?\\}", "");  // 이기찬 
        text = text.replaceAll("\\<.*?\\>", "");  // 이기찬 
        text = text.strip();  // 이기찬 
        return text;  // 이기찬 전처리된 텍스트 반환
    } // 이기찬

    // 텍스트 후처리 메소드 (추가)
    private String postprocessText(String text) { // 이기찬 번역 후 텍스트를 후처리하는 메소드
        text = text.replaceAll("\\s+", " ");  // 이기찬 여러 개의 공백을 하나의 공백으로 대체
        text = text.strip();  // 이기찬 텍스트 양쪽 끝의 공백 제거
        text = text.substring(0, 1).toUpperCase() + text.substring(1);  // 이기찬 첫 글자를 대문자로 변환
        return text;  // 이기찬 후처리된 텍스트 반환
    } // 이기찬

    // 구글 번역 API를 사용한 번역 메소드 (추가)
    private String translateText(String text, String targetLanguage) { // 이기찬 구글번역api를 사용하여 텍스트를 번역하는 메소드
        if ("ko".equals(targetLanguage)) { // 타겟 언어가 한국어인 경우 번역 생략 
            return text; // 이기찬
        } // 이기찬
        Translate translate = TranslateOptions.getDefaultInstance().getService(); // 번역 클라이언트 생성 
        Translation translation = translate.translate( // 이기찬 번역 수행
            text, // 이기찬 
            Translate.TranslateOption.sourceLanguage("ko"), // 소스 언어 설정 
            Translate.TranslateOption.targetLanguage(targetLanguage), // 타겟 언어 설정 
            Translate.TranslateOption.format("text") //  텍스트 형식 설정
        ); // 이기찬
        return translation.getTranslatedText(); // 번역된 텍스트 반환 
    } // 이기찬
}
