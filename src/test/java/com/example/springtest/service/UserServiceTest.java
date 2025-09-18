package com.example.springtest.service;

import com.example.springtest.model.User;
import com.example.springtest.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private IUserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    public void should_throw_error_with_invalid_email() {
        String email = "savitar@gmail.com";
        String password = "123456";

        String expectedErrorMessage = "Invalid email";
        //Setup mock
        when(repository.findByEmail(email)).thenReturn(null);
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {userService.login(email, password);});
        assertEquals(expectedErrorMessage, thrownException.getMessage());
    }

    @Test
    public void should_throw_error_with_valid_email_but_wrong_password() {
        String email = "savitar@gmail.com";
        String password = "password";
        String wrongPassword = "wrongPassword";

        String expectedErrorMessage = "Wrong password";
        //Setup mock
        when(repository.findByEmail(email)).thenReturn(User.builder().email(email).password(password).build());
        IllegalArgumentException thrownException = assertThrows(IllegalArgumentException.class, () -> {userService.login(email, wrongPassword);});

        assertEquals(expectedErrorMessage, thrownException.getMessage());

    }

    @Test
    public void should_return_access_with_valid_email_and_password() {
        String email = "savitar@gmail.com";
        String password = "123456";
        String expectedOutput = "accessToken";

        //Setup mock
        when(repository.findByEmail(email)).thenReturn(User.builder().email(email).password(password).build());
        String result = userService.login(email, password);
        assertEquals(expectedOutput, result);
    }
}
