package com.springboot.apiserver.user.texts.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Table(name="user_texts")
@Getter
@Setter
@NoArgsConstructor
public class UserTexts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userTextsId;

    @Column(name="USER_ID", nullable = false)
    private int userId;

    @Column(name="MOOD",nullable = false)
    private String mood;

    @Column(name="KEYWORD",nullable = false)
    private String keyword;

    @Column(name="TARGET",nullable = false)
    private String target;

    @Column(name="PRODUCT",nullable = false)
    private String product;

    @Column(name = "PROMPT",nullable = false)
    private String prompt;

    @Column(name = "GPT_MESSAGE",nullable = false)
    private String gptMessage;

    @Column(name = "CREATED_AT",nullable = false)
    private LocalDateTime createdAt;

}
