package com.vvss.FlavorFiesta.example_for_homework;

import com.vvss.FlavorFiesta.controllers.AuthController;
import com.vvss.FlavorFiesta.models.Comment;
import com.vvss.FlavorFiesta.models.Recipe;
import com.vvss.FlavorFiesta.models.Review;
import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.services.*;
import com.vvss.FlavorFiesta.test_utils.TestControllerIntegrationTest;
import io.micrometer.observation.Observation;
import org.hamcrest.Matchers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.vvss.FlavorFiesta.payloads.request.SignupRequest;
import com.vvss.FlavorFiesta.payloads.response.MessageResponse;
import com.vvss.FlavorFiesta.security.basicauth.BasicAuthToken;
import com.vvss.FlavorFiesta.util.RankedItem;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class BigBangIntegrationTest extends TestControllerIntegrationTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private RecipeService recipeService;
    @Autowired
    private UserService userService;
    @Autowired
    private RankingService rankingService;

    private User user;
    private User anotherUser;

    @BeforeAll
    void setUp(){
        user = new User("username", "email", "password");
        userService.saveUser(user);
        anotherUser = new User("username2", "email2", "password2");
        userService.saveUser(anotherUser);

        Recipe recipe = new Recipe(user, "Recipe", "ingredients", "instructions");
        recipeService.saveRecipe(recipe);
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment(recipe, user, "My comment 1"));
        comments.add(new Comment(recipe, user, "My comment 2"));
        comments.add(new Comment(recipe, user, "My comment 3"));
        comments.forEach(commentService::saveComment);
    }

    @AfterAll
    void tearDown(){
        commentService.getAllComments().forEach(commentService::deleteComment);
        recipeService.getAllRecipes().forEach(recipeService::deleteRecipe);
        userService.getAllUsers().forEach(userService::deleteUser);
    }

    @Test
    public void testBigBangIntegration() throws Exception {
        BasicAuthToken authToken = new BasicAuthToken(user.getUsername(), "password");
        HttpHeaders headers = new HttpHeaders();
        headers.put("Authorization", List.of(authToken.toAuthorizationHeader()));
        List<RankedItem<User>> expectedRankedUsers = rankingService.getUserRankingWithMostComments();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/comments/?ownerId=" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/rankings/top-commenters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(expectedRankedUsers.size())));
    }
}
