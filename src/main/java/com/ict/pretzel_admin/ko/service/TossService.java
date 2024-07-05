package com.ict.pretzel_admin.ko.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ict.pretzel_admin.ko.mapper.TossMapper;
import com.ict.pretzel_admin.ko.vo.TossVO;

@Service
public class TossService {

    @Autowired
    private TossMapper tossMapper;

    @Value("${toss.secret.key}")
    private String secret_key;

    @Value("${toss.url}")
    private String toss_url;

    // 토스 리스트
    public ResponseEntity<?> tossList() {

        List<TossVO> toss_list = tossMapper.toss_list();

        return ResponseEntity.ok(toss_list);
    }

    // 토스 결제 취소
    public ResponseEntity<?> tossCancel(String toss_idx, String cancelReason){
        try {

            TossVO toss = tossMapper.toss_detail(toss_idx);

            // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
            Base64.Encoder encoder = Base64.getEncoder();
            byte[] encodedBytes = encoder.encode((secret_key + ":").getBytes(StandardCharsets.UTF_8));
            // 헤더에 보낼 인증 토큰
            String authorizations = "Basic " + new String(encodedBytes);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(toss_url + toss.getPaymentKey() +"/cancel"))
                .header("Authorization", authorizations)
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"" + cancelReason +"\"}"))
                .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
            JsonObject cancelObject = jsonObject.get("cancels").getAsJsonArray().get(0).getAsJsonObject();
            toss.setCancelAmount(cancelObject.get("cancelAmount").getAsInt());
            toss.setCanceledAt(cancelObject.get("canceledAt").getAsString());
            toss.setCancelReason(cancelReason);

            int result = tossMapper.toss_cancel(toss);

            if (result > 0) {
                System.out.println("결제 성공");
                return ResponseEntity.ok(toss);
            }
        } catch (Exception e) {
            System.out.println("tossCancel : " + e);
        }
        return ResponseEntity.ok(0);
    }


}
