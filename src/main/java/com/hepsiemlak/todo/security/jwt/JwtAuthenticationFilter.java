package com.hepsiemlak.todo.security.jwt;

import com.hepsiemlak.todo.security.service.UserDetailsImpl;
import com.hepsiemlak.todo.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JWTTokenProvider jwtTokenProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken =extractJwtFromRequest(request);

            if (StringUtils.hasText(jwtToken) && jwtTokenProvider.validateToken(jwtToken)){
                String id = jwtTokenProvider.getUserIdFromJWT(jwtToken);

                UserDetailsImpl user =  userDetailsService.loadUserById(id);
                if (user != null){
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(user,null
                                    ,user.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }catch (Exception e){
            return;
        }
        filterChain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer "))
            return headerAuth.substring(7);
        return null;
    }
}
