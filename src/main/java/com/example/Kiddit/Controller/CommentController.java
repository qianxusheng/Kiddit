package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.CommentDTO;
import com.example.Kiddit.Service.CommentService;
import com.example.Kiddit.Service.GptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.example.Kiddit.DataTransferObject.CommentRequestDTO;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // @Autowired
    // private GptService gptService;
    /**
     * Get a paginated list of comments for a specific post.
     *
     * @param postId the ID of the post
     * @param page the page number to retrieve (default is 0)
     * @param size the number of comments per page (default is 10)
     * @return paginated list of CommentDTOs
     */
    @GetMapping("/{postId}/comments")
    public Page<CommentDTO> getCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,   // Default to page 0
            @RequestParam(defaultValue = "10") int size    // Default to 10 items per page
    ) {
        return commentService.getCommentsByPost(postId, page, size);
    }

    /**
     * Add a new comment to a specific post.
     *
     * @param postId the ID of the post to which the comment will be added
     * @param commentRequest a DTO containing userId and comment content
     * @return string
     */
    @PostMapping("/{postId}/comments")
    public ResponseEntity<Map<String, String>> addComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDTO commentRequest
    ) {
        // Call handleComment to check appropriateness and save or reject comment
        Map<String, String> response = commentService.handleComment(
                commentRequest.getContent(),
                commentRequest.getUserId(),
                postId
        );
    
        return ResponseEntity.ok(response);  // Return the result to the client
    }
    
    /**
     * Test endpoint to verify GPT service functionality
     */
    // @PostMapping("/test-gpt")
    // public String testGpt(@RequestParam String commentContent) {
    //     try {
    //         return gptService.chatWithGpt(commentContent);
    //     } catch (Exception e) {
    //         return "Error: " + e.getMessage();
    //     }
    // }
}
