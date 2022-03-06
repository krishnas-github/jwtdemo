package com.mycompany.jwtdemo.controller;

import com.mycompany.jwtdemo.model.JwtRequest;
import com.mycompany.jwtdemo.model.JwtResponse;
import com.mycompany.jwtdemo.service.CustomUserDetailService;
import com.mycompany.jwtdemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class JwtController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomUserDetailService customUserDetailService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/generateToken")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){

        UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(),jwtRequest.getPassword());
        //authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUserName(),jwtRequest.getPassword()));
        authenticationManager.authenticate(upat);

        UserDetails userDetails = customUserDetailService.loadUserByUsername(jwtRequest.getUserName());

        String jwtToken = jwtUtil.generateToken(userDetails);

        //return ResponseEntity.ok(jwtResponse);
        JwtResponse jwtResponse = new JwtResponse(jwtToken);
        return new ResponseEntity<JwtResponse>(jwtResponse, HttpStatus.OK);

    }
}
