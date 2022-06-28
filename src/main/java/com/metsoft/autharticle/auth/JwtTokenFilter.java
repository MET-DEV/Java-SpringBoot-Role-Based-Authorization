package com.metsoft.autharticle.auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private UserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        final String authHeader=req.getHeader("Authorization");
        String username=null;
        String token=null;

        if(authHeader !=null && authHeader.contains("Bearer")) {
            token=authHeader.substring(7);
            System.out.println(token);
            try {
                username=tokenManager.getUsernameFromToken(token);

            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        if (username != null && token != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (tokenManager.tokenValidate(token)) {
                UsernamePasswordAuthenticationToken upassToken =
                        tokenManager.getAuthentication(token, SecurityContextHolder.getContext().getAuthentication(), service.loadUserByUsername(username));

                upassToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(upassToken);
            }
        }
        chain.doFilter(req, res);
    }

}
