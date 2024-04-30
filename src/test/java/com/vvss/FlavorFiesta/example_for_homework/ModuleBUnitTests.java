package com.vvss.FlavorFiesta.example_for_homework;

import com.vvss.FlavorFiesta.models.Comment;
import com.vvss.FlavorFiesta.models.Recipe;
import com.vvss.FlavorFiesta.models.User;
import com.vvss.FlavorFiesta.repositories.CommentRepository;
import com.vvss.FlavorFiesta.services.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ModuleBUnitTests {
    // CommentService
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    public void testModuleB() {
        User user = new User("username", "email", "password");
        Recipe recipe = new Recipe(user, "Recipe 1", "ingredients", "instructions");
        Comment comment1 = new Comment(recipe, user, "My comment");
        comment1.setId(1L);
        Comment comment2 = new Comment(recipe, user, "My comment");
        comment2.setId(2L);
        List<Comment> comments = new ArrayList<>();
        comments.add(comment1);
        comments.add(comment2);

        // getCommentById, existing comment
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment1));
        Comment resultComm2 = commentService.getCommentById(comment1.getId());
        assertEquals(comment1, resultComm2);

        // getCommentById, non-existing comment
        when(commentRepository.findById(3L)).thenReturn(Optional.empty());
        Comment resultComm3 = commentService.getCommentById(3L);
        assertNull(resultComm3);

        // saveComment
        when(commentRepository.save(comment1)).thenReturn(comment1);
        commentService.saveComment(comment1);
        assertEquals(commentRepository.save(comment1), commentService.saveComment(comment1));

        // getAllComments
        when(commentRepository.findAll()).thenReturn(comments);
        assertEquals(commentRepository.findAll(), commentService.getAllComments());
    }
}
