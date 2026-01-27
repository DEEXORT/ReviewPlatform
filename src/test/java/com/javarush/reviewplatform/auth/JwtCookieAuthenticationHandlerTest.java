package com.javarush.reviewplatform.auth;

import com.javarush.reviewplatform.auth.service.CustomUserDetails;
import com.javarush.reviewplatform.util.Constant;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtCookieAuthenticationHandlerTest {

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private CustomUserDetails userDetails;

    @InjectMocks
    private JwtCookieAuthenticationHandler handler;

    private final String mockToken = "test.jwt.token";

    @BeforeEach
    void setUp() {
        // Настраиваем поведение authentication.getPrincipal()
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Настраиваем роли (authorities)
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        doReturn(authorities).when(userDetails).getAuthorities();

        // Настраиваем генерацию токена
        when(jwtProvider.generateToken(eq(userDetails), anyList())).thenReturn(mockToken);
    }

    @Test
    void onAuthenticationSuccess_ShouldAddCookieAndRedirect() throws Exception {
        // GIVEN
        when(request.isSecure()).thenReturn(false);

        // WHEN
        handler.onAuthenticationSuccess(request, response, authentication);

        // THEN
        // 1. Проверяем создание Cookie через ArgumentCaptor
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());

        Cookie capturedCookie = cookieCaptor.getValue();
        assertEquals("token", capturedCookie.getName());
        assertEquals(mockToken, capturedCookie.getValue());
        assertEquals("/", capturedCookie.getPath());
        assertTrue(capturedCookie.isHttpOnly());
        assertFalse(capturedCookie.getSecure());
        assertEquals(86400, capturedCookie.getMaxAge());

        // 2. Проверяем вызов редиректа
        verify(response).sendRedirect(Constant.Path.PRODUCTS);
    }

    @Test
    void onAuthenticationSuccess_ShouldSetSecureCookie_WhenRequestIsSecure() throws Exception {
        // GIVEN
        when(request.isSecure()).thenReturn(true);

        // WHEN
        handler.onAuthenticationSuccess(request, response, authentication);

        // THEN
        ArgumentCaptor<Cookie> cookieCaptor = ArgumentCaptor.forClass(Cookie.class);
        verify(response).addCookie(cookieCaptor.capture());
        assertTrue(cookieCaptor.getValue().getSecure());
    }
}