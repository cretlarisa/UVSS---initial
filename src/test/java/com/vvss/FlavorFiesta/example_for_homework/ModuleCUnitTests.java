package com.vvss.FlavorFiesta.example_for_homework;

import com.vvss.FlavorFiesta.models.Comment;
import com.vvss.FlavorFiesta.models.Recipe;
import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.services.CommentService;
import com.vvss.FlavorFiesta.services.RankingService;
import com.vvss.FlavorFiesta.util.RankedItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ModuleCUnitTests {
    // RankingService, getUserRankingWithMostComments()
    @Mock
    private RankingService rankingService;

    @Test
    public void testModuleC() {
        User user1 = new User("username1", "email1", "password1");
        User user2 = new User("username2", "email2", "password2");
        User user3 = new User("username3", "email3", "password3");

        Recipe recipe1 = new Recipe(user1, "Recipe 1", "ingredients", "instructions");
        Recipe recipe2 = new Recipe(user2, "Recipe 2", "ingredients", "instructions");
        Recipe recipe3 = new Recipe(user2, "Recipe 3", "ingredients", "instructions");
        Recipe recipe4 = new Recipe(user3, "Recipe 4", "ingredients", "instructions");
        // recipe 1 and recipe 3 are written by user 2

        Comment comment1 = new Comment(recipe1, user1, "My comment");
        Comment comment2 = new Comment(recipe1, user1, "My comment");
        Comment comment3 = new Comment(recipe1, user1, "My comment");
        Comment comment4 = new Comment(recipe2, user2, "My comment");
        Comment comment5 = new Comment(recipe2, user2, "My comment");
        Comment comment6 = new Comment(recipe2, user2, "My comment");
        Comment comment7 = new Comment(recipe3, user3, "My comment");
        Comment comment8 = new Comment(recipe1, user1, "My comment");
        Comment comment9 = new Comment(recipe1, user1, "My comment");
        Comment comment10 = new Comment(recipe2, user2, "My comment");
        Comment comment11 = new Comment(recipe3, user2, "My comment");
        // recipe 1 has 5 comments
        // recipe 2 has 4 comments
        // recipe 3 has 2 comment
        // recipe 4 has 0 comments


        List<RankedItem<User>> userResulted = rankingService.getUserRankingWithMostComments();
        Map<User, Long> mapUsers = new HashMap<>();
        mapUsers.put(user1, 5L);
        mapUsers.put(user2, 6L);
        mapUsers.put(user3, 0L);
        List<RankedItem<User>> userList = rankingService.rankItems(mapUsers);
        // because recipe 2 and 3 have the same author, user2
        // and recipe 2 has 4 comments, recipe 3 has 2 comments
        // then user with most comments is user2, then user1, user3
        assertEquals(userResulted, userList);
    }
}
