package com.example.Kiddit.Controller;

import com.example.Kiddit.Service.PostVoteService;
import com.example.Kiddit.Entity.VoteType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostVoteController {

    @Autowired
    private PostVoteService postVoteService;

    /**
     * Endpoint to cast or cancel a vote on a post.
     * 
     * @param postId ID of the post
     * @param userId ID of the user
     * @param voteType Type of the vote (UP or DOWN)
     * @return HTTP 200 OK if vote is handled
     */
    @PostMapping("/posts/{postId}/votes")
    public ResponseEntity<Void> postVote(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestParam VoteType voteType
    ) {
        postVoteService.vote(postId, userId, voteType);
        return ResponseEntity.ok().build();
    }
}
