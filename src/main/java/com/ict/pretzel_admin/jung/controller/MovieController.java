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
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ict.pretzel_admin.common.Paging;
import com.ict.pretzel_admin.jung.service.MovieService;
import com.ict.pretzel_admin.jung.tools.TMDBTools;
import com.ict.pretzel_admin.jung.tools.TranslateTool;
import com.ict.pretzel_admin.jwt.JwtDecode;
import com.ict.pretzel_admin.vo.CastVO;
import com.ict.pretzel_admin.vo.CrewVO;
import com.ict.pretzel_admin.vo.MovieVO;

import lombok.RequiredArgsConstructor;

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

    @Autowired
    private TranslateTool translateTool;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("query") String query, 
    @RequestParam("year") String year) {
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
            String api_key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmE1OThkMzg4OTgwZjBlMTJjNmU1N2RkYjRmNjFlNyIsInN1YiI6IjY2NzEzMGNlNDA1YjNhMjk3MDZhYWFlNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cT8hOciOWfO-qUWSh_fzqQzVburxqSAqwdXoaTgHz1E";
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
    public ResponseEntity<?> insert_movie(@RequestHeader("Authorization") String token, MovieVO movieVO) throws IOException {
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
            // Cloud에 비디오 업로드
            BlobInfo blobVideoInfo = storage.createFrom(
                BlobInfo.newBuilder(bucketName, storage_folder+movieVO.getMovie().getOriginalFilename())
                .setContentType(video_ext)
                .build(),
                movieVO.getMovie().getInputStream());
                String video_name = movieVO.getMovie().getOriginalFilename().replace(".mp4", "");
                movieVO.setMovie_url(storage_folder+video_name);
                movieVO.setStorage_name(video_name);

            // Cloud에 자막 업로드
            if (movieVO.getSubtitle() != null) {
                String subtitle_ext = movieVO.getSubtitle().getContentType();
                BlobInfo blobSubtitleInfo = storage.createFrom(
                    BlobInfo.newBuilder(bucketName, storage_folder+movieVO.getSubtitle().getOriginalFilename())
                    .setContentType(subtitle_ext)
                    .build(),
                    movieVO.getSubtitle().getInputStream());
                    // 자막 번역 및 업로드 (추가)
                    translateTool.translateAndUploadSubtitles(storage, bucketName, storage_folder, movieVO.getSubtitle());
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
                Blob blob = storage.get(bucketName, movie_info.getMovie_url()+".mp4");
                BlobId blobId = blob.getBlobId();
                boolean deleted = storage.delete(blobId);

                // 원본 .srt자막 파일 삭제
                Blob srt_blob = storage.get(bucketName, movie_info.getMovie_url()+".srt");
                BlobId srt_blobId = srt_blob.getBlobId();
                boolean srt_deleted = storage.delete(srt_blobId);

                // 번역 .vtt자막 파일 삭제
                List<String> languages = Arrays.asList("ko", "ja", "zh", "en", "fr", "de", "es", "it", "pt", "ru", "hi");
                for (String k : languages) {
                    Blob vtt_blob = storage.get(bucketName, movie_info.getMovie_url()+"_"+k+".vtt");
                    BlobId vtt_blobId = vtt_blob.getBlobId();
                    boolean vtt_deleted = storage.delete(vtt_blobId);
                }
                
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
                // Cloud에 비디오 업로드
                BlobInfo blobVideoInfo = storage.createFrom(
                    BlobInfo.newBuilder(bucketName, storage_folder+movieVO.getMovie().getOriginalFilename())
                    .setContentType(video_ext)
                    .build(),
                    movieVO.getMovie().getInputStream());
                    String video_name = movieVO.getMovie().getOriginalFilename().replace(".mp4", "");
                    movieVO.setMovie_url(storage_folder+video_name);
                    movieVO.setStorage_name(video_name);
                    
                // Cloud에 자막 업로드
                if (movieVO.getSubtitle() != null) {
                    String subtitle_ext = movieVO.getSubtitle().getContentType();
                    BlobInfo blobSubtitleInfo = storage.createFrom(
                        BlobInfo.newBuilder(bucketName, storage_folder+movieVO.getSubtitle().getOriginalFilename())
                        .setContentType(subtitle_ext)
                        .build(),
                        movieVO.getSubtitle().getInputStream());
                        // 자막 번역 및 업로드 (추가)
                        translateTool.translateAndUploadSubtitles(storage, bucketName, storage_folder, movieVO.getSubtitle());
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
            Blob blob = storage.get(bucketName, movie_info.getMovie_url()+".mp4");
            BlobId blobId = blob.getBlobId();
            boolean deleted = storage.delete(blobId);

           // 원본 .srt자막 파일 삭제
           Blob srt_blob = storage.get(bucketName, movie_info.getMovie_url()+".srt");
           BlobId srt_blobId = srt_blob.getBlobId();
           boolean srt_deleted = storage.delete(srt_blobId);

           // 번역 .vtt자막 파일 삭제
           List<String> languages = Arrays.asList("ko", "ja", "zh", "en", "fr", "de", "es", "it", "pt", "ru", "hi");
           for (String k : languages) {
               Blob vtt_blob = storage.get(bucketName, movie_info.getMovie_url()+"_"+k+".vtt");
               BlobId vtt_blobId = vtt_blob.getBlobId();
               boolean vtt_deleted = storage.delete(vtt_blobId);
           }

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
            List<MovieVO> movie_list = movieService.synchro_list();
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
            int count;
            if (keyword == null || keyword.equals("")) {
                count = movieService.movie_count();
            }else {
                count = movieService.search_count(keyword);
            }
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
    @GetMapping("/cast_list")
	public ResponseEntity<?> cast_list(@RequestParam("movie_idx") String movie_idx) {
		try {
			List<CastVO> result = movieService.cast_list(movie_idx);
			return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}
    @GetMapping("/emotion_insert")
	public ResponseEntity<?> emotion_insert(@RequestParam("movie_idx") String movie_idx) {
		try {
			int result = movieService.emotion_insert(movie_idx);
			return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}
    @GetMapping("/deepface_insert")
	public ResponseEntity<?> deepface_insert(@RequestParam("movie_idx") String movie_idx) {
		try {
			int result = movieService.deepface_insert(movie_idx);
			return ResponseEntity.ok(result);
			
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}

}
