package com.example.spring_boot_jwt.service;

import com.example.spring_boot_jwt.model.CName;
import com.example.spring_boot_jwt.model.CST;
import com.example.spring_boot_jwt.model.User;
import com.example.spring_boot_jwt.repository.CSTRepository;
import com.example.spring_boot_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CSTRepository cstRepository;

    @Override
    public ResponseEntity<Object> save(User user) {
        try {

            user.setFirstName(user.getFirstName());
            String p = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(p);
            userRepository.save(user);
            return new ResponseEntity<>("User Register Successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> updateUser(Long id, CName cName) {
        User old_user = userRepository.findUsersByIdIs(id);
        User user1 = new User();
        try {
            if (old_user == null) {
                return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
            } else {
                user1.setEmail(old_user.getEmail());
                user1.setId(id);
                user1.setFirstName(cName.getFirstName());
                user1.setLastName(cName.getLastName());
                user1.setPassword(old_user.getPassword());
                user1.setRole(old_user.getRole());
                userRepository.save(user1);
                return new ResponseEntity<>(user1, HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> createCst(CST cst) {
        User user = userRepository.findUsersByIdIs(cst.getUserId());
        try {
            if (user == null){
                return new ResponseEntity<>("User Not Found", HttpStatus.NOT_FOUND);
            }
            else {
                cstRepository.save(cst);
                return new ResponseEntity<>(cst, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Object> cstList() {
        try {
            List<CST> cstList = cstRepository.findAll();
            if (cstList.isEmpty()) {
                return new ResponseEntity<>(cstList, HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(cstList, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

