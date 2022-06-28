package com.metsoft.autharticle.service;

import com.metsoft.autharticle.model.RegisterDto;
import com.metsoft.autharticle.model.User;
import com.metsoft.autharticle.model.UserDetailsDto;
import com.metsoft.autharticle.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new UserDetailsDto(user);
    }
    public void  registerUser(RegisterDto registerDto){
        registerDto.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepository.save(registerDto.registerDtoToUser(registerDto));
    }
}