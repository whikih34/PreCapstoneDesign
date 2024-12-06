package com.springboot.apiserver.user.images.controller;

import com.springboot.apiserver.s3.uploader.S3Uploader;
import com.springboot.apiserver.user.images.entity.UserImages;
import com.springboot.apiserver.user.images.entity.UserImagesRepository;
import com.springboot.apiserver.user.images.entity.UserUploadedImages;
import com.springboot.apiserver.user.images.entity.UserUploadedImagesRepository;
import com.springboot.apiserver.user.images.imagedto.UserImageResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserImageController {
    private final UserImagesRepository userImagesRepository;
    private final S3Uploader s3Uploader;
    private final UserUploadedImagesRepository userUploadedImagesRepository;

    @Autowired
    public UserImageController(UserImagesRepository userImagesRepository,S3Uploader s3Uploader
            ,UserUploadedImagesRepository userUploadedImagesRepository ) {
        this.userImagesRepository = userImagesRepository;
        this.userUploadedImagesRepository=userUploadedImagesRepository;
        this.s3Uploader = s3Uploader;
    }

    @GetMapping("/get-images")
    public List<UserImageResponseDto> getUserImages(@RequestParam("USER_ID")int userId) {
        List<UserImages> userImagesList = userImagesRepository.findByUserId(userId);
        List<UserImageResponseDto> responseDtoList = new ArrayList<>();

        for (UserImages userImage : userImagesList) {
            UserImageResponseDto dto = new UserImageResponseDto(userImage.getUserImageId(), userImage.getUserImage()
            ,userImage.getSampleImage(),userImage.getPrompt(),userImage.getCreatedAt());
            responseDtoList.add(dto);
        }

        return responseDtoList;
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file,
                                              @RequestParam("id") int userId) {
        System.out.println(file);
        System.out.println(userId);
        try {
            // S3에 업로드
            String imageUrl = s3Uploader.uploadUserFile(userId, file);

            // DB에 업로드 정보 저장
            UserUploadedImages uploadedImage = new UserUploadedImages();
            uploadedImage.setUserId(userId);
            uploadedImage.setUserUploadedImage(imageUrl);
            uploadedImage.setUploadedAt(LocalDateTime.now());
            userUploadedImagesRepository.save(uploadedImage);

            return ResponseEntity.ok(imageUrl);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to upload image: " + e.getMessage());
        }
    }

}
