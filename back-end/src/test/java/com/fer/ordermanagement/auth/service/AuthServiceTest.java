package com.fer.ordermanagement.auth.service;

import com.fer.ordermanagement.auth.dto.AuthResponse;
import com.fer.ordermanagement.auth.dto.LoginRequest;
import com.fer.ordermanagement.auth.dto.RegisterRequest;
import com.fer.ordermanagement.auth.entity.Role;
import com.fer.ordermanagement.auth.entity.User;
import com.fer.ordermanagement.auth.enums.RoleName;
import com.fer.ordermanagement.auth.repository.RoleRepository;
import com.fer.ordermanagement.auth.repository.UserRepository;
import com.fer.ordermanagement.auth.security.JwtUtil;
import com.fer.ordermanagement.auth.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtUtil jwtUtil;
    @Mock private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;
    private UserDetails mockUserDetails;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest("admin", "123456");
        registerRequest = new RegisterRequest("newuser", "newuser@gmail.com", "123456", "New User", "0909999999");

        mockRole = new Role();
        mockRole.setName(RoleName.STAFF);

        // Mock UserDetails với role STAFF
        mockUserDetails = new org.springframework.security.core.userdetails.User(
                "admin",
                "encodedPassword",
                List.of(new SimpleGrantedAuthority("STAFF"))
        );
    }

    //LOGIN

    @Test
    @DisplayName("Login: Nên ném exception khi sai username hoặc password")
    void login_ShouldThrowException_WhenBadCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    @DisplayName("Login: Nên trả về token khi đăng nhập thành công")
    void login_ShouldReturnToken_WhenValidCredentials() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userDetailsService.loadUserByUsername("admin")).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken(mockUserDetails)).thenReturn("mock-jwt-token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("admin", response.getUsername());
        assertEquals("STAFF", response.getRole());
    }

    //REGISTER

    @Test
    @DisplayName("Register: Nên ném RuntimeException khi username đã tồn tại")
    void register_ShouldThrowException_WhenUsernameExists() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Register: Nên ném RuntimeException khi email đã tồn tại")
    void register_ShouldThrowException_WhenEmailExists() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@gmail.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Register: Nên ném RuntimeException khi không tìm thấy role mặc định")
    void register_ShouldThrowException_WhenDefaultRoleNotFound() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(roleRepository.findByName(RoleName.STAFF)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Register: Nên lưu user và trả về token khi dữ liệu hợp lệ")
    void register_ShouldSaveUserAndReturnToken_WhenValid() {
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("newuser@gmail.com")).thenReturn(false);
        when(roleRepository.findByName(RoleName.STAFF)).thenReturn(Optional.of(mockRole));
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userDetailsService.loadUserByUsername("newuser")).thenReturn(mockUserDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("mock-jwt-token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("STAFF", response.getRole());
        verify(userRepository).save(any(User.class));
    }
}