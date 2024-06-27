package com.ict.pretzel_admin.jwt;

import java.util.Base64;
import org.json.JSONObject;
import lombok.Data;

@Data
public class JwtDecode {
    // 토큰 안에 있는 정보
    private String admin_id;

    public JwtDecode(String token) {
        try {
            // 토큰을 각 구간(Header, Payload, Signature)으로 분할
            String[] chunks = token.split("\\.");

            // 토큰의 형식이 유효하지 않을 경우
            if (chunks.length < 2) {
                throw new IllegalArgumentException("Invalid JWT token format");
            }

            // 패딩 추가
            String payload = new String(addPadding(chunks[1]));

            // 디코딩
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String decodedPayload = new String(decoder.decode(payload));

            // Payload 출력
            System.out.println("Payload: " + decodedPayload);

            // JSON 파싱
            JSONObject jsonPayload = new JSONObject(decodedPayload);
            this.admin_id = jsonPayload.optString("sub", "N/A");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String addPadding(String str) {
        int paddingNeeded = 4 - (str.length() % 4);
        if (paddingNeeded < 4) {
            str += "=".repeat(paddingNeeded);
        }
        return str;
    }
}
