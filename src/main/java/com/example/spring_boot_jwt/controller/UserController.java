package com.example.spring_boot_jwt.controller;

import com.example.spring_boot_jwt.model.AuthenticationReponse;
import com.example.spring_boot_jwt.model.AuthenticationRequest;
import com.example.spring_boot_jwt.model.CName;
import com.example.spring_boot_jwt.model.CST;
import com.example.spring_boot_jwt.model.User;
import com.example.spring_boot_jwt.repository.UserRepository;
import com.example.spring_boot_jwt.service.MyUserDetailsService;
import com.example.spring_boot_jwt.service.UserService;
import com.example.spring_boot_jwt.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    // register user
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user) {
        return userService.save(user);
    }

    // for login
    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody AuthenticationRequest user) {
        System.out.println("hiii");
        User user1 = userRepository.findUserByEmail(user.getUserName());
        List<SimpleGrantedAuthority> role = null;
        final Authentication authentication;
        try {
            role = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user1.getRole()));
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword(), role)
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
        final String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationReponse(jwt));
    }

    // for testing
    @GetMapping("/hellouser")
    private String user() {
        return "hellouser";
    }

    //for testing
    @GetMapping("/helloadmin")
    private String admin() {
        return "helloadmin";
    }

    // change the firstname and lastname using updateUser method with user login authentication by token.
    @PutMapping("/update/{id}")
    private ResponseEntity<Object> updateUser(@PathVariable("id") Long id, @RequestBody CName cName) {
        return userService.updateUser(id, cName);
    }

    // generate customer support ticket by user with authentication token
    @PostMapping("/create/cst")
    private ResponseEntity<Object> createCst(@RequestBody CST cst) {
        return userService.createCst(cst);
    }

    // admin user request to list customer support tickets by admin authentication token
    @GetMapping("/admin/cstlist")
    private ResponseEntity<Object> csts() {
        return userService.cstList();
    }
}


