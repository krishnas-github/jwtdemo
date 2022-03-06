package com.mycompany.jwtdemo.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {

    //This method actually does the validation for user existence
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.equals("John")){//here you can make a DB call with the help of a repository and do the valiidation
            return new User("John","secret",new ArrayList<>());// assume these are returned from DB upon success
        }
        else {
            throw new UsernameNotFoundException("User does not exist");
        }
    }
}
