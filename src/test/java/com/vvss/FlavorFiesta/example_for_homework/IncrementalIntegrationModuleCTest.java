package com.vvss.FlavorFiesta.example_for_homework;

import com.vvss.FlavorFiesta.controllers.AuthController;
import com.vvss.FlavorFiesta.models.Comment;
import com.vvss.FlavorFiesta.models.Recipe;
import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.payloads.request.SignupRequest;
import com.vvss.FlavorFiesta.payloads.response.MessageResponse;
import com.vvss.FlavorFiesta.repositories.CommentRepository;
import com.vvss.FlavorFiesta.services.CommentService;
import com.vvss.FlavorFiesta.services.RankingService;
import com.vvss.FlavorFiesta.services.UserService;
import com.vvss.FlavorFiesta.util.RankedItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IncrementalIntegrationModuleCTest {
    // Integration Test for Module C (P -> A -> B -> C)
    @InjectMocks
    private AuthController authController;
    @Mock
    private CommentService commentService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserService userService;
    @Mock
    private RankingService rankingService;

    @Test
    public void testIncrementalIntegrationModuleC() {
        SignupRequest request1 = new SignupRequest("username", "email", "password");
        User user1 = new User("username", "email", "password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        User user2 = new User("username2", "email2", "password2");

        Recipe recipe1 = new Recipe(user1, "Recipe 1", "ingredients", "instructions");
        Recipe recipe2 = new Recipe(user2, "Recipe 2", "ingredients", "instructions");

        // register (and have access to all userService): P->A
        ResponseEntity<?> responseEntity = authController.registerUser(request1);
        MessageResponse messageResponse = (MessageResponse) responseEntity.getBody();
        assertEquals("User registered successfully!", messageResponse.getMessage());
        when(userService.emailExists("email")).thenReturn(true);
        when(userService.usernameExists("username")).thenReturn(true);
        assertTrue(userService.emailExists(user1.getEmail()));
        assertTrue(userService.usernameExists(user1.getUsername()));

        // comments: P->A->B
        Comment comment1 = new Comment(recipe1, user1, "My comment 1");
        Comment comment2 = new Comment(recipe1, user1, "My comment 2");
        Comment comment3 = new Comment(recipe1, user1, "My comment 3");
        Comment comment4 = new Comment(recipe1, user1, "My comment 4");
        Comment comment5 = new Comment(recipe2, user2, "My comment 5");
        Comment comment6 = new Comment(recipe2, user2, "My comment 6");
        comment1.setId(1L);
        comment2.setId(2L);
        comment3.setId(3L);
        comment4.setId(4L);
        comment5.setId(5L);
        comment6.setId(6L);
        List<Comment> comments = List.of(comment1, comment2, comment3, comment4, comment5, comment6);
        commentService.saveComment(comment1);
        commentService.saveComment(comment2);
        commentService.saveComment(comment3);
        commentService.saveComment(comment4);
        commentService.saveComment(comment5);
        commentService.saveComment(comment6);
        // recipe1 has 4 comments, and it's written by user1
        // recipe2 has 2 comments, and it's written by user2

        // get the rank of all users by the nr of comments: P->A->B->C
        List<RankedItem<User>> userResulted = rankingService.getUserRankingWithMostComments();
        Map<User, Long> mapUsers = new HashMap<>();
        mapUsers.put(user1, 4L);
        mapUsers.put(user2, 2L);
        when(commentService.getAllComments()).thenReturn(comments);
        assertEquals(commentService.getAllComments().stream()
                .collect(Collectors.groupingBy(Comment::getOwner, Collectors.counting())), mapUsers);
        List<RankedItem<User>> userList = rankingService.rankItems(mapUsers);
        assertEquals(userResulted, userList);
    }
}
