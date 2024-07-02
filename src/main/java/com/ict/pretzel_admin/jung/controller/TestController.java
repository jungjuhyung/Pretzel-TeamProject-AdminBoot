package com.ict.pretzel_admin.jung.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import io.jsonwebtoken.io.IOException;
import java.io.FileInputStream;



@RestController
@RequestMapping("/test")
public class TestController {
	
    @Value("${spring.cloud.gcp.storage.bucket}") // application.properties에 써둔 bucket 이름
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}") // application.properties에 써둔 bucket 이름
    private String id;

	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam("query") String query, @RequestParam("year") String year) {
		try {
			String encode_query = URLEncoder.encode(query, "UTF-8").replaceAll("\\+", "%20");
			String encode_year = URLEncoder.encode(year, "UTF-8").replaceAll("\\+", "%20");
			System.out.println(encode_query);
			System.out.println("check"+encode_year);
			String apiURL = "";
			if (year.equals("")) {
				apiURL = "https://api.themoviedb.org/3/search/movie?query="+encode_query+"&include_adult=true&language=ko-kr&language=en-US";
			}else{
				apiURL = "https://api.themoviedb.org/3/search/movie?query="+encode_query+"&year="+encode_year+"&include_adult=true&language=ko-kr&language=en-US";
			}
			System.out.println(apiURL);
			URL url = new URL(apiURL);
			String api_key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmE1OThkMzg4OTgwZjBlMTJjNmU1N2RkYjRmNjFlNyIsInN1YiI6IjY2NzEzMGNlNDA1YjNhMjk3MDZhYWFlNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cT8hOciOWfO-qUWSh_fzqQzVburxqSAqwdXoaTgHz1E";
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer "+api_key);
			int responeseCode = conn.getResponseCode();
			System.out.println(responeseCode);
			if(responeseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br =
						new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				String line ="";
				StringBuffer sb = new StringBuffer();
				while((line=br.readLine()) !=null) {
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

	@GetMapping("/detail")
	public String detail(@RequestParam("movie_id") String movie_id) {
		try {
			String apiURL = "https://api.themoviedb.org/3/movie/"+movie_id+"?language=ko-kr&language=en-US";
			String api_key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmE1OThkMzg4OTgwZjBlMTJjNmU1N2RkYjRmNjFlNyIsInN1YiI6IjY2NzEzMGNlNDA1YjNhMjk3MDZhYWFlNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cT8hOciOWfO-qUWSh_fzqQzVburxqSAqwdXoaTgHz1E";
			URL url = new URL(apiURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			// 헤더 요청
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer "+api_key);
			int responeseCode = conn.getResponseCode();
			System.out.println(responeseCode);
			if(responeseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br =
						new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				
				String line ="";
				StringBuffer sb = new StringBuffer();
				while((line=br.readLine()) !=null) {
					sb.append(line);
				}
				String result = sb.toString();
				return result;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
	@GetMapping("/translations")
	public String translations(@RequestParam("movie_id") String movie_id) {
		try {
			String apiURL = "https://api.themoviedb.org/3/movie/"+movie_id+"/translations?language=ko-kr&language=en-US";
			String api_key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmE1OThkMzg4OTgwZjBlMTJjNmU1N2RkYjRmNjFlNyIsInN1YiI6IjY2NzEzMGNlNDA1YjNhMjk3MDZhYWFlNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cT8hOciOWfO-qUWSh_fzqQzVburxqSAqwdXoaTgHz1E";
			URL url = new URL(apiURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			// 헤더 요청
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer "+api_key);
			int responeseCode = conn.getResponseCode();
			System.out.println(responeseCode);
			if(responeseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br =
						new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
				
				String line ="";
				StringBuffer sb = new StringBuffer();
				while((line=br.readLine()) !=null) {
					sb.append(line);
				}
				String result = sb.toString();
				System.out.println(result);
				return result;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
	@GetMapping("/credits")
	public String credits(@RequestParam("movie_id") String movie_id) {
		try {
			String apiURL = "https://api.themoviedb.org/3/movie/"+movie_id+"/credits?language=ko-kr&language=en-US";
			String api_key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmE1OThkMzg4OTgwZjBlMTJjNmU1N2RkYjRmNjFlNyIsInN1YiI6IjY2NzEzMGNlNDA1YjNhMjk3MDZhYWFlNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cT8hOciOWfO-qUWSh_fzqQzVburxqSAqwdXoaTgHz1E";
			URL url = new URL(apiURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			// 헤더 요청
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer "+api_key);
			int responeseCode = conn.getResponseCode();
			System.out.println(responeseCode);
			if(responeseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br =
						new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				
				String line ="";
				StringBuffer sb = new StringBuffer();
				while((line=br.readLine()) !=null) {
					sb.append(line);
				}
				String result = sb.toString();
				System.out.println(result);
				return result;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
	
	@GetMapping("/trailer")
	public String trailer(@RequestParam("movie_id") String movie_id) {
		try {
			String apiURL = "https://api.themoviedb.org/3/movie/"+movie_id+"/videos";
			String api_key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmE1OThkMzg4OTgwZjBlMTJjNmU1N2RkYjRmNjFlNyIsInN1YiI6IjY2NzEzMGNlNDA1YjNhMjk3MDZhYWFlNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cT8hOciOWfO-qUWSh_fzqQzVburxqSAqwdXoaTgHz1E";
			URL url = new URL(apiURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			// 헤더 요청
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer "+api_key);
			int responeseCode = conn.getResponseCode();
			System.out.println(responeseCode);
			if(responeseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br =
						new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				
				String line ="";
				StringBuffer sb = new StringBuffer();
				while((line=br.readLine()) !=null) {
					sb.append(line);
				}
				String result = sb.toString();
				System.out.println(result);
				return result;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
	
	@GetMapping("/certification")
	public String certification(@RequestParam("movie_id") String movie_id) {
		try {
			String apiURL = "https://api.themoviedb.org/3/movie/"+movie_id+"/release_dates";
			String api_key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIwYmE1OThkMzg4OTgwZjBlMTJjNmU1N2RkYjRmNjFlNyIsInN1YiI6IjY2NzEzMGNlNDA1YjNhMjk3MDZhYWFlNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.cT8hOciOWfO-qUWSh_fzqQzVburxqSAqwdXoaTgHz1E";
			URL url = new URL(apiURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("GET");
			
			// 헤더 요청
			conn.setRequestProperty("accept", "application/json");
			conn.setRequestProperty("Authorization", "Bearer "+api_key);
			int responeseCode = conn.getResponseCode();
			System.out.println(responeseCode);
			if(responeseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader br =
						new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				
				String line ="";
				StringBuffer sb = new StringBuffer();
				while((line=br.readLine()) !=null) {
					sb.append(line);
				}
				String result = sb.toString();
				System.out.println(result);
				return result;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}

    @GetMapping("/file_del")
	public ResponseEntity<?> file_del(@RequestParam String filePath) throws IOException {
		try {
            // 스토리지 파일 업로드
            GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/ict-pretzel-43373d904ced.json"));
            Storage storage = StorageOptions.newBuilder()
            .setCredentials(credentials)
            .setProjectId(id).build().getService();
			// 파일 삭제
			Blob blob = storage.get(bucketName, filePath);
			BlobId blobId = blob.getBlobId();
			boolean deleted = storage.delete(blobId);
            
			if (deleted) {
				return ResponseEntity.ok("1");
			} else {
				return ResponseEntity.ok("0");
			}
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.ok("0");
		}
	}
}
