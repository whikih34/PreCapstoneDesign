package com.springboot.apiserver.signup.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    //주의!! 이거는 진짜 ID가 아니고 DB상의 PK입니다.

    @Column(name="ID", unique = true, nullable = false)
    private String id;

    @Column(name = "PASSWORD", nullable = false)
    private String password;
}
