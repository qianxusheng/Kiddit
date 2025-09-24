package com.example.Kiddit.Controller;

import com.example.Kiddit.Service.CommentVoteService;
import com.example.Kiddit.Entity.VoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle voting on comments.
 */
@RestController
@RequestMapping("/api")
public class CommentVoteController {

    @Autowired
    private CommentVoteService commentVoteService;

    /**
     * Endpoint to cast or cancel a vote on a comment.
     * 
     * @param commentId ID of the comment
     * @param userId ID of the user
     * @param voteType Type of the vote (UP or DOWN)
     * @return HTTP 200 OK if vote is handled
     */
    @PostMapping("/comments/{commentId}/votes")
    public ResponseEntity<Void> commentVote(
            @PathVariable Long commentId,
            @RequestParam Long userId,
            @RequestParam VoteType voteType
    ) {
        commentVoteService.vote(commentId, userId, voteType);
        return ResponseEntity.ok().build();
    }
}
