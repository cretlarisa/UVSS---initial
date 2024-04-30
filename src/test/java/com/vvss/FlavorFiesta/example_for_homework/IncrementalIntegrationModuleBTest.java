package com.vvss.FlavorFiesta.example_for_homework;

import com.vvss.FlavorFiesta.controllers.AuthController;
import com.vvss.FlavorFiesta.controllers.RecipeController;
import com.vvss.FlavorFiesta.models.Comment;
import com.vvss.FlavorFiesta.models.Recipe;
import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.payloads.request.SignupRequest;
import com.vvss.FlavorFiesta.payloads.response.MessageResponse;
import com.vvss.FlavorFiesta.repositories.CommentRepository;
import com.vvss.FlavorFiesta.services.CommentService;
import com.vvss.FlavorFiesta.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IncrementalIntegrationModuleBTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;

    @InjectMocks
    private CommentService commentService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private CommentRepository commentRepository;


    // Integration Test for Module B (P -> A -> B or P -> B -> A)
    @Test
    public void testIncrementalIntegrationModuleB() {
        SignupRequest request = new SignupRequest("username", "email", "password");
        User user = new User("username", "email", "password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Recipe recipe1 = new Recipe(user, "Recipe 1", "ingredients", "instructions");
        Recipe recipe2 = new Recipe(user, "Recipe 2", "ingredients", "instructions");

        // register as user, overall application: P
        ResponseEntity<?> responseEntity = authController.registerUser(request);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());

        // verify commentRepository: P->A
        Comment comment1 = new Comment(recipe1, user, "My comment1");
        Comment comment2 = new Comment(recipe2, user, "My comment2");
        comment1.setId(1L);
        comment2.setId(2L);

        // write comment as user and use commentService: P->A->B
        when(commentRepository.save(comment1)).thenReturn(comment1);
        when(commentRepository.save(comment2)).thenReturn(comment2);
        commentService.saveComment(comment1);
        assertEquals(comment1, commentService.saveComment(comment1));
        commentService.saveComment(comment2);
        assertEquals(comment2, commentService.saveComment(comment2));

        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        Comment resultComm1 = commentService.getCommentById(comment1.getId());
        assertEquals(comment1, resultComm1);
        when(commentRepository.findById(2L)).thenReturn(Optional.of(comment2));
        Comment resultComm2 = commentService.getCommentById(comment2.getId());
        assertEquals(comment2, resultComm2);

        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        when(commentRepository.findAll()).thenReturn(comments);
        assertEquals(comments, commentService.getAllComments());
    }
}
