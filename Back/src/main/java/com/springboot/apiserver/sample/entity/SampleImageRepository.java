package com.springboot.apiserver.sample.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SampleImageRepository extends JpaRepository<SampleImage, Long> {
    @Query("SELECT s.SAMPLE_IMAGE FROM SampleImage s")
    List<String> findAllUrl();

}
