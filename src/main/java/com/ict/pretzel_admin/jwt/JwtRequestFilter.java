package com.ict.pretzel_admin.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


// 토큰을 추출하고, 유효성 검사하여, 유효한 경우만 사용자 정보를 로드, 
// 즉, 보호된 리소스에 대한 접근이 가능한 사용자인지 확인
@Component
public class JwtRequestFilter extends OncePerRequestFilter{

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");
        String admin_id = null;
        String jwtToken = null;

        // 'Authorization' 헤더가 없거나 Bearer 토큰이 아니면 요청을 계속 진행합니다.
        // 토큰이 있으면서 Authorization 내용 Bearer 을 시작하면 
        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            // Bearer 토큰에서 JWT를 추출합니다.
            jwtToken = requestTokenHeader.substring(7);

            // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
            try {
                admin_id = jwtUtil.extractUsername(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            } catch (SignatureException e) {
                // Handle JWT signature exception
                response.setStatus(HttpServletResponse.SC_OK); // Set HTTP status to 200
                response.getWriter().write("0"); // Return 0 instead of an error message
                return;
            }
        }else{
            System.out.println("JWT 없음");
            logger.warn("JWT Token does not begin with Bearer String");
        }

        // 사용자 이름(아이디) 추출 , 현재 SecurityContext에 인증정보가 없는지 확인
        if(admin_id != null && SecurityContextHolder.getContext().getAuthentication() == null){
            // 사용자 이름(아이디)가지고 현재 DB 있는지 검사 
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(admin_id);

            if(jwtUtil.validateToken(jwtToken, userDetails)){
                // 인증객체 생성
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //인증객체에 추가 세부 정보를 설정
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));    

                 // 이 부분에서 컨트롤러로 요청을 넘기기 전에 추가 작업을 수행
                 SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);

    }

    
}
