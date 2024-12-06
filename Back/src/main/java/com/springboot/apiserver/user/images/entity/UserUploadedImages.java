package com.springboot.apiserver.user.images.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="user_uploaded_images")
@Getter
@Setter
@NoArgsConstructor
public class UserUploadedImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userUploadedImagesKey;

    @Column(name="USER_ID",nullable = false)
    private int userId;

    @Column(name="USER_UPLOADED_IMAGE",nullable = false)
    private String userUploadedImage;

    @Column(name="UPLOADED_AT")
    private LocalDateTime uploadedAt;
}
