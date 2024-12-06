package com.springboot.apiserver.user.images.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserUploadedImagesRepository extends JpaRepository<UserUploadedImages, Integer> {
}
