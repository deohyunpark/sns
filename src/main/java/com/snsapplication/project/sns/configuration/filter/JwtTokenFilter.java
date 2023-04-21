package com.snsapplication.project.sns.configuration.filter;

import com.snsapplication.project.sns.model.User;
import com.snsapplication.project.sns.service.UserService;
import com.snsapplication.project.sns.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String key;
    private final UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더안의 토큰의 claims에서 userName 뽑아내기
        //먼저 헤더를 뽑아내기
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        //만약 헤더가 null이거나 Bearer로 시작하지 않을 경우
        if (header == null || !header.startsWith("Bearer ")) {
            log.error("Error occurs while getting header. header is null or invalid");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String token = header.split(" ")[1].trim();
            if(JwtTokenUtils.isExpired(token, key)){
                log.error("Key is expired");
                filterChain.doFilter(request,response);
                return;
            }

            String userName = JwtTokenUtils.userName(token, key);
            User user = userService.loadUserByUserName(userName);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (RuntimeException e) {
            log.error("Error occurs while validation. {}", e.toString());
            filterChain.doFilter(request,response);
            return;
        }
        filterChain.doFilter(request,response);
    }

}
