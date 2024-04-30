package com.vvss.FlavorFiesta.example_for_homework;

import com.vvss.FlavorFiesta.controllers.AuthController;
import com.vvss.FlavorFiesta.models.Comment;
import com.vvss.FlavorFiesta.models.Recipe;
import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.payloads.request.SignupRequest;
import com.vvss.FlavorFiesta.payloads.response.MessageResponse;
import com.vvss.FlavorFiesta.repositories.CommentRepository;
import com.vvss.FlavorFiesta.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class IncrementalIntegrationModuleATest {

    // Integration Test for Module A (P -> A)
    // P = entire application
    // A = CommentRepository

    @InjectMocks
    private AuthController authController;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserService userService;

    @Test
    public void testIncrementalIntegrationModuleA() {
        User user = new User("username", "email", "password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // register user
        SignupRequest request = new SignupRequest("username", "email", "password");
        ResponseEntity<?> responseEntity = authController.registerUser(request);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();

        // verify register
        assertNotNull("ResponseEntity body is null", messageResponse);
        assertNotNull("MessageResponse is null", messageResponse.getMessage());
        assertEquals("User registered successfully!", messageResponse.getMessage());

        Recipe recipe1 = new Recipe(user, "Recipe 1", "ingredients", "instructions");
        Comment comment1 = new Comment(recipe1, user, "My comment1");
        comment1.setId(1L);
        commentRepository.save(comment1);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        assertEquals(comment1, commentRepository.findById(1L).orElseThrow());
        verify(commentRepository).findById(1L);
        commentRepository.deleteAllByRecipe(recipe1);
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), commentRepository.findById(1L));
        verify(commentRepository).deleteAllByRecipe(recipe1);
    }
}

