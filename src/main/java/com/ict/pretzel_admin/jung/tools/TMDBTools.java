package com.ict.pretzel_admin.jung.tools;
import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.ict.pretzel_admin.vo.CastVO;
import com.ict.pretzel_admin.vo.CrewVO;

@Component
public class TMDBTools {

	public Map<String, String> detail(String movie_id) {
		try {
			System.out.println(movie_id);
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
				System.out.println(result);
                // JSON 파싱
				Map<String, String> map = new HashMap<String, String>();

                JSONObject jsonObject = new JSONObject(result);
                String tmdb_title = String.valueOf(jsonObject.get("original_title"));
                String poster_url = String.valueOf(jsonObject.get("poster_path"));
                String backdrop_url = String.valueOf(jsonObject.get("backdrop_path"));
                String runtime = String.valueOf(jsonObject.get("runtime"));
                String release_date = String.valueOf(jsonObject.get("release_date"));
				System.out.println(poster_url);
				System.out.println(backdrop_url);
				
				map.put("tmdb_title", tmdb_title);
				map.put("poster_url", poster_url);
				map.put("backdrop_url", backdrop_url);
				map.put("runtime", runtime);
				map.put("release_date", release_date);
                
				return map;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
	public String translations(String movie_id) {
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

				// JSON 문자열을 JSONObject로 변환
				JSONObject jsonObject = new JSONObject(result);

				JSONArray translations = jsonObject.getJSONArray("translations");
		
				String synopsis = "";
				for (int i = 0; i < translations.length(); i++) {
					JSONObject translation = translations.getJSONObject(i);
					if (translation.getString("iso_3166_1").equals("US")) {
						synopsis = String.valueOf(translation.getJSONObject("data").get("overview"));
						continue;
					}else if (translation.getString("iso_3166_1").equals("KR")) {
						synopsis = String.valueOf(translation.getJSONObject("data").get("overview"));
						break;
					}
				}
				return synopsis;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}

	public String trailer(String movie_id) {
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

				// JSON 문자열을 JSONObject로 변환
				JSONObject jsonObject = new JSONObject(result);

				JSONArray video_infos = jsonObject.getJSONArray("results");
		
				String trailer = "";
				for (int i = 0; i < video_infos.length(); i++) {
					JSONObject video_info = video_infos.getJSONObject(i);
					if (video_info.getString("type").equals("Official Trailer") && video_info.getString("site").equals("YouTube")) {
						trailer = String.valueOf(video_info.get("key"));
						break;
					}else if (video_info.getString("type").equals("Teaser Trailer") && video_info.getString("site").equals("YouTube")) {
						trailer = String.valueOf(video_info.get("key"));
						continue;
					}else if (video_info.getString("type").equals("Trailer") && video_info.getString("site").equals("YouTube")) {
						trailer = String.valueOf(video_info.get("key"));
						continue;
					}
				}
				return trailer;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
	
	
	public String certification(String movie_id) {
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

				// JSON 문자열을 JSONObject로 변환
				JSONObject jsonObject = new JSONObject(result);

				JSONArray certifications = jsonObject.getJSONArray("results");
		
				String movie_grade = "";
				for (int i = 0; i < certifications.length(); i++) {
					JSONObject certification = certifications.getJSONObject(i);
					if (certification.getString("iso_3166_1").equals("KR")) {
						JSONArray release_dates = certification.getJSONArray("release_dates");
						movie_grade = String.valueOf(release_dates.getJSONObject(0).get("certification"));
						break;
					}
				}
				return movie_grade;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}

	public List<CastVO> cast(String movie_id) {
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

				// JSON 문자열을 JSONObject로 변환
				JSONObject jsonObject = new JSONObject(result);

				JSONArray casts = jsonObject.getJSONArray("cast");
				
				List<CastVO> cast_list = new ArrayList<>();
				int cycle;
				if (casts.length() >= 10) {
					cycle = 10;
				}else{
					cycle = casts.length();
				}
				for (int i = 0; i < cycle; i++) {
					CastVO cvo = new CastVO();
					JSONObject cast = casts.getJSONObject(i);
					cvo.setOrders(String.valueOf(cast.get("order")));
					cvo.setCast_name(String.valueOf(cast.get("original_name")));
					cvo.setRole((String.valueOf(cast.get("character"))));
					if (String.valueOf(cast.get("profile_path")).equals("null")) {
						cvo.setCast_img("");
					}else{
						cvo.setCast_img(String.valueOf(cast.get("profile_path")));
					}
					cast_list.add(cvo);
					}
				return cast_list;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
	public List<CrewVO> crew(String movie_id) {
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

				// JSON 문자열을 JSONObject로 변환
				JSONObject jsonObject = new JSONObject(result);

				JSONArray crews = jsonObject.getJSONArray("crew");
				
				List<CrewVO> crew_list = new ArrayList<>();
				for (int i = 0; i < crews.length(); i++) {
					JSONObject crew = crews.getJSONObject(i);
					String job = String.valueOf(crew.get("job"));
					if (job.equalsIgnoreCase("Producer")) {
						CrewVO cvo = new CrewVO();
						cvo.setOrders(String.valueOf(1));
						cvo.setCrew_name(String.valueOf(crew.get("original_name")));
						cvo.setJob(job);
						if (String.valueOf(crew.get("profile_path")).equals("null")) {
							cvo.setCrew_img("");
						}else{
							cvo.setCrew_img(String.valueOf(crew.get("profile_path")));
						}
						crew_list.add(cvo);
					}else if (job.equalsIgnoreCase("Director")) {
						CrewVO cvo = new CrewVO();
						cvo.setOrders(String.valueOf(0));
						cvo.setCrew_name(String.valueOf(crew.get("original_name")));
						cvo.setJob(job);
						if (String.valueOf(crew.get("profile_path")).equals("null")) {
							cvo.setCrew_img("");
						}else{
							cvo.setCrew_img(String.valueOf(crew.get("profile_path")));
						}
						crew_list.add(cvo);
					}
				}
				return crew_list;
			}
			
		} catch (Exception e) {
			System.out.println("연결 실패");
		}
		return null;
	}
}
