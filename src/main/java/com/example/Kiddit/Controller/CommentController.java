package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.CommentDTO;
import com.example.Kiddit.Service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Kiddit.DataTransferObject.CommentRequestDTO;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Get a paginated list of comments for a specific post.
     *
     * @param postId the ID of the post
     * @param page the page number to retrieve (default is 0)
     * @param size the number of comments per page (default is 10)
     * @return paginated list of CommentDTOs
     */
    @GetMapping
    public ResponseEntity<Page<CommentDTO>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,   // Default to page 0
            @RequestParam(defaultValue = "10") int size    // Default to 10 items per page
    ) {
        Page<CommentDTO> comments = commentService.getCommentsByPost(postId, page, size);
        return ResponseEntity.ok(comments);
    }

    /**
     * Add a new comment to a specific post.
     *
     * @param postId the ID of the post to which the comment will be added
     * @param commentRequest a DTO containing userId and comment content
     * @return the added CommentDTO
     */
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDTO commentRequest
    ) {
        CommentDTO comment = commentService.addComment(
                postId,
                commentRequest.getUserId(),
                commentRequest.getContent()
        );
        return ResponseEntity.ok(comment);
    }
}
