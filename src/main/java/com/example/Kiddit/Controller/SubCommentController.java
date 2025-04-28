package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.SubCommentDTO;
import com.example.Kiddit.DataTransferObject.CommentRequestDTO;
import com.example.Kiddit.Service.SubCommentService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SubCommentController {

    @Autowired
    private SubCommentService subCommentService;

    /**
     * Get a paginated list of subcomments (replies) for a specific parent comment.
     *
     * @param commentId the ID of the parent comment
     * @param page the page number to retrieve (default is 0)
     * @param size the number of subcomments per page (default is 5)
     * @return a Page of SubCommentDTOs representing the replies to the given comment
     */
    @GetMapping("/comment/{commentId}/subcomments")
    public ResponseEntity<Page<SubCommentDTO>> getSubCommentsByCommentId(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<SubCommentDTO> subComments = subCommentService.getSubCommentsByCommentId(commentId, page, size);
        return ResponseEntity.ok(subComments);
    }

    /**
     * Add a sub-comment to an existing comment.
     *
     * @param parentCommentId the ID of the parent comment
     * @param userId the ID of the user creating the sub-comment
     * @param content the content of the sub-comment
     * @return ResponseEntity with the created SubComment
     */
    @PostMapping("/{parentCommentId}/sub-comments")
    public ResponseEntity<Map<String, String>> addSubComment(
            @PathVariable Long parentCommentId,
            @RequestBody CommentRequestDTO subCommentRequest
    ) {
        // Call the service method to add the sub-comment
        Map<String, String> response = subCommentService.handleSubComment(
                subCommentRequest.getContent(),
                subCommentRequest.getUserId(),
                parentCommentId
        );
        
        // Return the created sub-comment
        return ResponseEntity.ok(response);
    }
}
