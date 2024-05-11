package ru.hisoakende.coursework;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import ru.hisoakende.coursework.models.User;
import ru.hisoakende.coursework.repositories.UserRepository;
import ru.hisoakende.coursework.services.AuthenticationService;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void testLoadUserByUsernameUserFound() {
        User user = new User();
        user.setUsername("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        UserDetails userDetails = authenticationService.loadUserByUsername("user");
        assertNotNull(userDetails);
        assertEquals(" test@example.com ", userDetails.getUsername());
    }
    @Test(expected = Exception.class)
    public void testLoadUserByUsernameUserNotFound () {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        authenticationService.loadUserByUsername("user");
    }
}