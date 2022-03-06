package com.mycompany.jwtdemo.filtr;

import com.mycompany.jwtdemo.service.CustomUserDetailService;
import com.mycompany.jwtdemo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//call this filter only once per request
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        //Get the token from the request header
        //validate the token

        String bearerToken = httpServletRequest.getHeader("Authorization");
        String username = null;
        String token = null;

        //check the token if exist or has bearer text
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){

            //extract jwt token from bearer token
            token = bearerToken.substring(7);
            try{
                //Extract username from the token
                username = jwtUtil.extractUsername(token);

                //get user detail for this user
                UserDetails userDetails= customUserDetailService.loadUserByUsername(username);

                //Security checks
                if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null){

                    UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(upat);

                }
                else{
                    System.out.println("Invalid token ");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Invalid bearer token format");
        }
        //if all is well forward the filter request to the requested endpoint
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
