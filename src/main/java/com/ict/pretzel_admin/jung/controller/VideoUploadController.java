package com.ict.pretzel_admin.jung.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.ict.pretzel_admin.jung.service.VideoUploadService;
import com.ict.pretzel_admin.vo.VideoUploadVO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/upload")
public class VideoUploadController {
    
    @Value("${spring.cloud.gcp.storage.bucket}") // application.properties에 써둔 bucket 이름
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id}") // application.properties에 써둔 bucket 이름
    private String id;

    @Autowired
    private VideoUploadService videoUploadService;

	@PostMapping("/video")
    public ResponseEntity<Void> updateMemberInfo(VideoUploadVO videoUploadVO) throws IOException {
        // 스토리지 파일 업로드
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("src/main/resources/ict-pretzel-43373d904ced.json"));
        Storage storage = StorageOptions.newBuilder()
        .setCredentials(credentials)
        .setProjectId(id).build().getService();

        String uuid = videoUploadVO.getVideo().getOriginalFilename()+"_"+UUID.randomUUID().toString();
        String ext = videoUploadVO.getVideo().getContentType(); // 파일의 형식 ex) JPG

		// Cloud에 영상 업로드
        BlobInfo blobInfo = storage.createFrom(
                BlobInfo.newBuilder(bucketName, uuid)
                        .setContentType(ext)
                        .build(),
                        videoUploadVO.getVideo().getInputStream());
        videoUploadVO.setMovie_url(uuid);
        
        videoUploadService.video_insert(videoUploadVO);
        videoUploadService.thema_insert(videoUploadVO);


        
        
        return new ResponseEntity(HttpStatus.OK);
    }
}
