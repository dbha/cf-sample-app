package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryListRepository extends JpaRepository<Diary, Long> {

    List<Diary> findByUser(String user);
}