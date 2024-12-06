package com.springboot.apiserver.user.images.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.List;

public interface UserImagesRepository extends JpaRepository<UserImages, Value.Int> {
    List<UserImages> findByUserId(int userId);
}
