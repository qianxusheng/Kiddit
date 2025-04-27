package com.example.Kiddit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Kiddit.Repository.PostVoteRepository;
import com.example.Kiddit.Repository.PostRepository;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Entity.VoteType;
import com.example.Kiddit.Entity.Post;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.PostVote;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostVoteService {

    @Autowired
    private PostVoteRepository postVoteRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Handles voting logic: allows user to vote or cancel vote on a post.
     * 
     * @param postId ID of the post
     * @param userId ID of the user
     * @param voteType Type of vote (UP or DOWN)
     */
    public void vote(Long postId, Long userId, VoteType voteType) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        postVoteRepository.findByUserAndPost(user, post).ifPresentOrElse(existingVote -> {
            if (existingVote.getVoteType() == voteType) {
                postVoteRepository.delete(existingVote); // cancel vote
            } else {
                existingVote.setVoteType(voteType);
                existingVote.setVotedAt(LocalDateTime.now());
                postVoteRepository.save(existingVote); // update vote
            }
        }, () -> {
            PostVote newVote = new PostVote();
            newVote.setPost(post);
            newVote.setUser(user);
            newVote.setVoteType(voteType);
            newVote.setVotedAt(LocalDateTime.now());
            postVoteRepository.save(newVote);
        });
    }

    /**
     * Retrieves a map of vote counts for a list of posts.
     * 
     * @param postIds the list of post IDs
     * @return a map of postId to vote counts (upvotes, downvotes)
     */
    public Map<Long, int[]> getVoteCounts(List<Long> postIds) {
        List<Object[]> voteCounts = postVoteRepository.countVotesByPostIn(postIds);

        Map<Long, int[]> voteCountsMap = new HashMap<>();
        for (Object[] voteCount : voteCounts) {
            Long postId = (Long) voteCount[0];
            int upvotes = ((Number) voteCount[1]).intValue();
            int downvotes = ((Number) voteCount[2]).intValue();
            voteCountsMap.put(postId, new int[]{upvotes, downvotes});
        }
        
        return voteCountsMap;
    }

    /**
     * Retrieves the user's vote status for each post.
     * 
     * @param postIds the list of post IDs
     * @param userId the ID of the user
     * @return a map of postId to user vote status
     */
    public Map<Long, VoteType> getUserVoteStatus(List<Long> postIds, Long userId) {
        List<Object[]> postVotes = postVoteRepository.findByUserAndPostIn(userId, postIds);

        Map<Long, VoteType> userVoteStatusMap = new HashMap<>();
        for (Object[] postVote : postVotes) {
            Long postId = (Long) postVote[0];
            VoteType voteType = (VoteType) postVote[2];
            userVoteStatusMap.put(postId, voteType);
        }

        return userVoteStatusMap;
    }
}   