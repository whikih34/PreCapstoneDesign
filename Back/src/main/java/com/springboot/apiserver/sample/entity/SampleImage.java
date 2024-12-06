package com.springboot.apiserver.sample.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="SAMPLE_IMAGE")
@Getter
@NoArgsConstructor
public class SampleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int SAMPLE_IMAGE_KEY;

    @Column(name="SAMPLE_IMAGE", nullable = false)
    private String SAMPLE_IMAGE;
}
