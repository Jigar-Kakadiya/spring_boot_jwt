package com.example.spring_boot_jwt.repository;

import com.example.spring_boot_jwt.model.CST;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSTRepository extends JpaRepository<CST,Long> {
}
