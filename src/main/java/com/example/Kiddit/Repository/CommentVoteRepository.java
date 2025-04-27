package com.example.Kiddit.Repository;

import com.example.Kiddit.Entity.CommentVote;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.VoteType;
import com.example.Kiddit.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface CommentVoteRepository extends JpaRepository<CommentVote, Long> {
    Optional<CommentVote> findByUserAndComment(User user, Comment comment);

    // Batch query the total upvotes and downvotes for multiple comments.
    @Query("SELECT c.comment.commentId, SUM(CASE WHEN c.voteType = com.example.Kiddit.Entity.VoteType.UP THEN 1 ELSE 0 END) AS upvotes, " +
           "SUM(CASE WHEN c.voteType = com.example.Kiddit.Entity.VoteType.DOWN THEN 1 ELSE 0 END) AS downvotes " +
           "FROM CommentVote c WHERE c.comment.commentId IN :commentIds GROUP BY c.comment.commentId")
    List<Object[]> countVotesByCommentIn(@Param("commentIds") List<Long> commentIds);

    // Batch query the vote status (UP or DOWN) of a user for multiple comments.
    @Query("SELECT c.comment.commentId, c.user.userId, c.voteType " +
           "FROM CommentVote c WHERE c.user.userId = :userId AND c.comment.commentId IN :commentIds")
    List<Object[]> findByUserAndCommentIn(@Param("userId") Long userId,
                                          @Param("commentIds") List<Long> commentIds);
}
