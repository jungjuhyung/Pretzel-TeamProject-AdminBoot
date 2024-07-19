package com.ict.pretzel_admin.lee.translateTool;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
// 이기찬 구글 번역 API
import com.google.cloud.translate.Translate; // 이기찬 구글 번역 api 
import com.google.cloud.translate.TranslateOptions; // 이기찬 구글 번역 api 
import com.google.cloud.translate.Translation; // 이기찬 구글 번역 api 

import io.jsonwebtoken.io.IOException;

import java.util.List;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

@Component
public class TranslateTool {

    // 자막 번역 및 업로드 메소드
    public void translateAndUploadSubtitles(Storage storage, String bucketName, String storage_folder, MultipartFile subtitle) throws IOException, UnsupportedEncodingException, java.io.IOException {
        // 자막 파일의 내용을 UTF-8 문자열로 읽기
		String subtitleContent = new String(subtitle.getBytes(), "UTF-8");
		
		// 번역할 언어 목록
        List<String> languages = Arrays.asList("ko", "ja", "zh", "en", "fr", "de", "es", "it", "pt", "ru", "hi"); // 번역할 언어 목록
        for (String lang : languages) {
            String translatedContent = translateText(preprocessText(subtitleContent), lang); // 텍스트 전처리 및 번역
            translatedContent = postprocessText(translatedContent); // 텍스트 후처리
            
			translatedContent = "WEBVTT\n\n" + translatedContent;
			
			// 번역된 파일의 이름 생성
			String translatedFileName = subtitle.getOriginalFilename().replace(".srt", "_" + lang + ".vtt");
            //번역된 파일을 스토리지에 업로드
			BlobInfo blobInfo = storage.create(
                BlobInfo.newBuilder(bucketName, storage_folder + translatedFileName).setContentType("text/vtt").build(),
                translatedContent.getBytes() // 번역된 파일의 내용을 바이트 배열로 변환하여 업로드
            );
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
    private String translateText(String text, String targetLanguage) { // 구글번역api를 사용하여 텍스트를 번역하는 메소드
        if ("ko".equals(targetLanguage)) { // 타겟 언어가 한국어인 경우 번역 생략 
            return text;
        }
        Translate translate = TranslateOptions.getDefaultInstance().getService(); // 번역 클라이언트 생성 
        Translation translation = translate.translate(
            text,
            Translate.TranslateOption.sourceLanguage("ko"), // 소스 언어 설정 
            Translate.TranslateOption.targetLanguage(targetLanguage), // 타겟 언어 설정 
            Translate.TranslateOption.format("text") //  텍스트 형식 설정
        );
        return translation.getTranslatedText(); // 번역된 텍스트 반환 
    }
}
