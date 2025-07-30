package com.deliverytech.delivery_api.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


class JwtAuthenticationFilterTest {

    private final JwtUtil jwtUtil = mock(JwtUtil.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);

    @BeforeEach // Use BeforeEach to set up the mock SecurityContext
    void setup() {
        // Clear the context before each test to ensure a clean state
        SecurityContextHolder.clearContext();
    }


    @Test
    void testDoFilterInternal_OncePerRequest() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoAuthorizationHeader() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidAuthorizationHeader() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Invalid Token");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_ValidToken() throws Exception {

        SecurityContext securityContext = mock(SecurityContext.class);

        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            // Configure SecurityContextHolder.getContext() to return your mock context
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
            HttpServletRequest request = mock(HttpServletRequest.class);
            HttpServletResponse response = mock(HttpServletResponse.class);
            FilterChain filterChain = mock(FilterChain.class);

            String token = "Bearer valid-token";
            when(request.getHeader("Authorization")).thenReturn(token);

            UserDetails userDetails = mock(UserDetails.class);
            when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
        } 
    }

    @Test
    void testDoFilterInternal_ExpiredToken() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userDetailsService);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String token = "Bearer expired-token";
        when(request.getHeader("Authorization")).thenReturn(token);

        when(jwtUtil.extractUsername(token.replace("Bearer ", ""))).thenThrow(ExpiredJwtException.class);

        // Create a mock for SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);

        // Mock the static SecurityContextHolder
        try (MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            // When SecurityContextHolder.getContext() is called, return our mock securityContext
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(securityContext);

            filter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
            // Verify that setAuthentication was never called on the mocked securityContext
            verify(securityContext, never()).setAuthentication(any(UsernamePasswordAuthenticationToken.class));
        }
    }

    @Test
    void testAutenticacaoComTokenValido() {
        // Crie um token válido
        String token = "Bearer valid-token";

        // Crie um usuário com informações válidas
        UserDetails userDetails = new User("username", "password", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        // Defina o token no contexto de segurança
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));

        // Verifique se o usuário está autenticado
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
}
