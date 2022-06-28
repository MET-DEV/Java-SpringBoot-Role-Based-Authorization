package com.metsoft.autharticle.controller;

import com.metsoft.autharticle.auth.TokenManager;
import com.metsoft.autharticle.model.LoginDto;
import com.metsoft.autharticle.model.RegisterDto;
import com.metsoft.autharticle.repository.UserRepository;
import com.metsoft.autharticle.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/")
@RestController
public class UsersController {


    private AuthenticationManager authenticationManager;
    private TokenManager tokenManager;
    private UserDetailsServiceImpl userDetailsService;


    @Autowired
    public UsersController( AuthenticationManager authenticationManager, TokenManager tokenManager) {
        this.authenticationManager = authenticationManager;
        this.tokenManager = tokenManager;

    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginDto loginDto){
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = tokenManager.generateToken(authentication);
        return ResponseEntity.ok(token);

    }


    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto){
        userDetailsService.registerUser(registerDto);
        return ResponseEntity.ok("Kayıt başarılı");
    }

    @GetMapping("user")
    public String homePage(){
        return "Hello, this user";
    }

    @GetMapping("admin")
    public String newPage(){
        return "Hello, this is admin";
    }

}
