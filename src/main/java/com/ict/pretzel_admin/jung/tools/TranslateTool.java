package com.ict.pretzel_admin.jung.tools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
// 구글 번역 API
import com.google.cloud.translate.v3.LocationName;
import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.Translation;
import com.google.cloud.translate.v3.TranslationServiceClient;
import com.google.cloud.translate.v3.TranslationServiceSettings;

import io.jsonwebtoken.io.IOException;

import java.util.List;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

@Component
public class TranslateTool {
    @Value("${spring.cloud.gcp.storage.project-id}") // application.properties에 써둔 bucket 이름
    private String id;

    // 자막 번역 및 업로드 메소드
    public void translateAndUploadSubtitles(Storage storage, String bucketName, String storage_folder, 
    MultipartFile subtitle) throws IOException, UnsupportedEncodingException, java.io.IOException {
        // MultipartFile에서 InputStream을 얻어와서 문자열에 익숙한 BufferedReader로 받기
        BufferedReader srt_file = new BufferedReader(new InputStreamReader(subtitle.getInputStream(), StandardCharsets.UTF_8));

        // 파일 내용을 StringBuffer에 저장
        StringBuffer contentBuffer = new StringBuffer();
        String line;
        while ((line = srt_file.readLine()) != null) {
            contentBuffer.append(line).append("\n");
        }

        // 파일 내용 문자열을 얻는다
        String content = contentBuffer.toString();

        if (content.startsWith("\uFEFF")) {
            content = content.substring(1);
        }

        // 자막 블록으로 나누기 (빈 줄로 구분)
        String[] subs = content.split("\n\n");
		
		// 번역할 언어 목록
        List<String> languages = Arrays.asList("ko", "ja", "zh", "en", "fr", "de", "es", "it", "pt", "ru", "hi");
        for (String lang : languages) {
            int count = 1;
            String res;
            if (lang.equals("ko")) {
                System.out.println(lang+" 번역 시작");
                res ="WEBVTT\n\n"+content;
            }else{
                System.out.println(lang+" 번역 시작");
                StringBuffer sb = new StringBuffer();
                sb.append("WEBVTT\n\n");
                for (String k : subs) {
                    System.out.println(lang + " 번역 진행률 : "+ count + "/"+subs.length);
                    String translatedContent = translateText(k, lang);
                    sb.append(translatedContent+"\n\n");
                    ++count;
                }
                res = sb.toString();
            }
			
            System.out.println(lang + " 번역 파일 스토리지 업로드 중");
			// 번역된 파일의 이름 생성
			String translatedFileName = subtitle.getOriginalFilename().replace(".srt", "_" + lang + ".vtt");
            //번역된 파일을 스토리지에 업로드
			BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, storage_folder + translatedFileName).setContentType("application/octet-stream").build(),
                res.getBytes(StandardCharsets.UTF_8) // 번역된 파일의 내용을 바이트 배열로 변환하여 업로드
                );
            System.out.println(lang + " 번역 파일 스토리지 업로드 완료");
        }
    }

    // 텍스트 전처리 메소드 (추가)
    private String preprocessText(String text){
        text = text.replaceAll("\\[.*?\\]", "");
        text = text.replaceAll("\\(.*?\\)", "");
        text = text.replaceAll("\\{.*?\\}", "");
        text = text.replaceAll("\\<.*?\\>", "");
        text = text.strip();
        return text;  // 전처리된 텍스트 반환
    }

    // 텍스트 후처리 메소드 (추가)
    private String postprocessText(String text) {
        text = text.replaceAll("\\s+", " ");  // 여러 개의 공백을 하나의 공백으로 대체
        text = text.strip();  // 텍스트 양쪽 끝의 공백 제거
        text = text.substring(0, 1).toUpperCase() + text.substring(1);  // 첫 글자를 대문자로 변환
        return text;
    }

    // 구글 번역 API를 사용한 번역 메소드 (추가)
    private String translateText(String subs, String targetLanguage) throws java.io.IOException { // 구글번역api를 사용하여 텍스트를 번역하는 메소드
        StringBuffer sb = new StringBuffer();
        String res = "";
        List<String> parts = Arrays.asList(subs.split("\n"));
        String idx = parts.get(0);
        String time = parts.get(1);
        String original_text =  parts.subList(2, parts.size()).stream().collect(Collectors.joining(" "));
        String preprocessed_text = preprocessText(original_text);

        // GoogleCredentials 객체 생성
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/ict-pretzel-43373d904ced.json"));

        // TranslationServiceSettings 설정
        TranslationServiceSettings settings = TranslationServiceSettings.newBuilder()
            .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
            .build();

        try (TranslationServiceClient client = TranslationServiceClient.create(settings)) {
            LocationName parent = LocationName.of(id, "global");
            TranslateTextRequest request =
                TranslateTextRequest.newBuilder()
                    .setParent(parent.toString())
                    .setMimeType("text/plain")
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(preprocessed_text)
                    .build();
            
            TranslateTextResponse response = client.translateText(request);
      
            if (!response.getTranslationsList().isEmpty()) {
                Translation translation = response.getTranslationsList().get(0);
                res = postprocessText(translation.getTranslatedText());
            }
          }
        sb.append(idx).append("\n");
        sb.append(time).append("\n");
        sb.append(res);

        return sb.toString(); // 번역된 텍스트 반환 
    }
}
