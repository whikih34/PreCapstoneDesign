package com.springboot.apiserver.user.images.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="user_images")
@Getter
@Setter
@NoArgsConstructor
public class UserImages {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userImageId;
    //주의! userImageId는 FK인 User ID와 다릅니다. 이 테이블용 PK입니다.

    @Column(name="USER_ID", nullable = false)
    private int userId;

    @Column(name="USER_IMAGE")
    private String userImage;
    //실제로 user가 과거에 만들었던 이미지
    //userImage aws S3 반환 URL의 형태로 String

    @Column(name="SAMPLE_IMAGE")
    private String sampleImage;
    //과거에 만들었던 이미지를 만들 때 썼던 이미지
    //sampleImage는 aws S3 반환 URL의 형태로 String

    @Column(name = "PROMPT")
    private String prompt;

    @Column(name="CREATED_AT")
    private LocalDateTime createdAt;
    //언제 생성했는지 담는 변수
    //LocalDateTime <-> MySQL DATETIME
    //yyyy-mm-dd hh:mm:ss

    @Column(name="TAKE_TIME")
    private Double takeTime;
}
