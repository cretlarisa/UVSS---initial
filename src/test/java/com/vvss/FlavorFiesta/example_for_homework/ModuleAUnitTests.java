package com.vvss.FlavorFiesta.example_for_homework;

import com.vvss.FlavorFiesta.models.Comment;
import com.vvss.FlavorFiesta.models.Recipe;
import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ModuleAUnitTests {
    //CommentRepository
    @Mock
    private CommentRepository commentRepository;

    @Test
    public void testModuleA(){
        User user = new User("username", "email", "password");
        Recipe recipe1 = new Recipe(user, "Recipe 1", "ingredients", "instructions");
        Recipe recipe2 = new Recipe(user, "Recipe 2", "ingredients", "instructions");
        Comment comment1 = new Comment(recipe1, user, "My comment1");
        Comment comment2 = new Comment(recipe1, user, "My comment2");
        Comment comment3 = new Comment(recipe2, user, "My comment3");
        Comment comment4 = new Comment(recipe2, user, "My comment4");
        comment1.setId(1L);
        comment2.setId(2L);
        comment3.setId(3L);
        comment4.setId(4L);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);
        comments.add(comment3);
        comments.add(comment4);

        when(commentRepository.findAll()).thenReturn(comments);
        List<Comment> commentsResp1 = new ArrayList<>();
        commentsResp1.add(comment1);
        commentsResp1.add(comment2);

        assertEquals(comments, commentRepository.findAll());
        commentRepository.deleteAllByRecipe(recipe2);
        verify(commentRepository).deleteAllByRecipe(recipe2);
        when(commentRepository.findAll()).thenReturn(commentsResp1);
        verify(commentRepository).findAll();
        assertEquals(commentsResp1, commentRepository.findAll());
    }
}
