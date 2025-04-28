package com.example.Kiddit.Repository;

import com.example.Kiddit.Entity.PostVote;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, Long> {
    Optional<PostVote> findByUserAndPost(User user, Post post);

    // Batch query the total upvotes and downvotes for multiple posts.
    @Query("SELECT p.post.postId, SUM(CASE WHEN p.voteType = com.example.Kiddit.Entity.VoteType.UP THEN 1 ELSE 0 END) AS upvotes, " +
           "SUM(CASE WHEN p.voteType = com.example.Kiddit.Entity.VoteType.DOWN THEN 1 ELSE 0 END) AS downvotes " +
           "FROM PostVote p WHERE p.post.postId IN :postIds GROUP BY p.post.postId")
    List<Object[]> countVotesByPostIn(@Param("postIds") List<Long> postIds);

    // Batch query the vote status (UP or DOWN) of a user for multiple posts.
    @Query("SELECT p.post.postId, p.user.userId, p.voteType " +
           "FROM PostVote p WHERE p.user.userId = :userId AND p.post.postId IN :postIds")
    List<Object[]> findByUserAndPostIn(@Param("userId") Long userId, @Param("postIds") List<Long> postIds);
}
