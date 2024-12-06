package com.springboot.apiserver.user.texts.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTextsRepository extends JpaRepository<UserTexts, Long> {
    List<UserTexts> findByUserId(int userId);
}
