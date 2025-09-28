package telran.java55.security;

import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import telran.java55.service.FarmerDetailsServiceImpl;
import telran.java55.service.UserServiceImpl;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;
    
    private final UserDetailsService userDetailsService;
    
    private final FarmerDetailsServiceImpl farmerDetailsService;

    public JwtRequestFilter(
            @Lazy UserServiceImpl userService,
            @Lazy FarmerDetailsServiceImpl farmerDetailsService,
            JwtUtil jwtUtil,
            @Qualifier("userServiceImpl") UserDetailsService userDetailsService
    ) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.farmerDetailsService = farmerDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        String method = request.getMethod();

       
        boolean isPublic =
                "OPTIONS".equalsIgnoreCase(method)
                || path.equals("/api/auth/user/login")
                || path.equals("/api/auth/farmer/login")
                || path.equals("/api/auth/user/register")
                || path.equals("/api/auth/farmer/register")
                || path.startsWith("/api/public/")
                || path.startsWith("/uploads/")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui");

        if (isPublic) {
            chain.doFilter(request, response);
            return;
        }

       
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authorization token is missing or invalid");
            return;
        }

        String token = tokenHeader.substring(7);

        if (userService.isTokenBlacklisted(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token has been revoked. Please log in again.");
            return;
        }

        try {
            final String email = jwtUtil.extractEmail(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                final UserDetails userDetails =
                        (path.startsWith("/api/surprisebags") || path.startsWith("/api/farmer"))
                                ? farmerDetailsService.loadUserByUsername(email)
                                : userDetailsService.loadUserByUsername(email);

                
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            
            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper(request) {
                @Override
                public Principal getUserPrincipal() {
                    return () -> {
                        try {
                            return jwtUtil.extractEmail(token);
                        } catch (Exception e) {
                            return null;
                        }
                    };
                }
            };

            chain.doFilter(wrappedRequest, response);

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Token expired");
        } catch (Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Invalid token");
        }
    }
}
