package com.example.spring_boot_jwt.service;

import com.example.spring_boot_jwt.model.CName;
import com.example.spring_boot_jwt.model.CST;
import com.example.spring_boot_jwt.model.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<Object> save(User user);


    ResponseEntity<Object> updateUser(Long id, CName cName);

    ResponseEntity<Object> createCst(CST cst);

    ResponseEntity<Object> cstList();
}
