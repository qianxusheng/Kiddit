package com.example.Kiddit.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Kiddit.Repository.CommentVoteRepository;
import com.example.Kiddit.Repository.CommentRepository;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Entity.VoteType;
import com.example.Kiddit.Entity.Comment;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.CommentVote;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Service
public class CommentVoteService {

    @Autowired
    private CommentVoteRepository commentVoteRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Handles voting logic: allows user to vote or cancel vote on a comment.
     * 
     * @param commentId ID of the comment
     * @param userId ID of the user
     * @param voteType Type of vote (UP or DOWN)
     */
    public void vote(Long commentId, Long userId, VoteType voteType) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        commentVoteRepository.findByUserAndComment(user, comment).ifPresentOrElse(existingVote -> {
            if (existingVote.getVoteType() == voteType) {
                commentVoteRepository.delete(existingVote); // cancel vote
            } else {
                existingVote.setVoteType(voteType);
                existingVote.setVotedAt(LocalDateTime.now());
                commentVoteRepository.save(existingVote); // update vote
            }
        }, () -> {
            CommentVote newVote = new CommentVote();
            newVote.setComment(comment);
            newVote.setUser(user);
            newVote.setVoteType(voteType);
            newVote.setVotedAt(LocalDateTime.now());
            commentVoteRepository.save(newVote);
        });
    }

    /**
     * Retrieves upvote and downvote counts for a list of comments.
     *
     * @param commentIds list of comment IDs to retrieve vote counts for
     * @return a map where each key is a commentId and the value is an int array: [0] = upvotes, [1] = downvotes
     */
    public Map<Long, int[]> getVoteCounts(List<Long> commentIds) {
        List<Object[]> voteCounts = commentVoteRepository.countVotesByCommentIn(commentIds);
        Map<Long, int[]> voteCountsMap = new HashMap<>();

        for (Object[] voteCount : voteCounts) {
            Long commentId = (Long) voteCount[0];
            int upvotes = ((Number) voteCount[1]).intValue();
            int downvotes = ((Number) voteCount[2]).intValue();
            voteCountsMap.put(commentId, new int[]{upvotes, downvotes});
        }

        return voteCountsMap;
    }

    /**
     * Retrieves the current user's vote status (UP or DOWN) for a list of comments.
     *
     * @param commentIds list of comment IDs to check vote status for
     * @param userId the ID of the user whose vote status is being retrieved
     * @return a map where each key is a commentId and the value is the VoteType (UP or DOWN) the user cast
     */
    public Map<Long, VoteType> getUserVoteStatus(List<Long> commentIds, Long userId) {
        List<Object[]> commentVotes = commentVoteRepository.findByUserAndCommentIn(userId, commentIds);

        Map<Long, VoteType> userVoteStatusMap = new HashMap<>();
        for (Object[] commentVote : commentVotes) {
            Long commentId = (Long) commentVote[0];
            VoteType voteType = (VoteType) commentVote[2];
            userVoteStatusMap.put(commentId, voteType);
        }

        return userVoteStatusMap;
    }
}
