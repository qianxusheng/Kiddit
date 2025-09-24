package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.CommentDTO;
import com.example.Kiddit.Entity.Comment;
import com.example.Kiddit.Entity.Post;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Retrieves a paginated list of comments for a specific post.
     *
     * @param postId the ID of the post whose comments are to be fetched
     * @param page the page number to retrieve (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a Page of CommentDTOs corresponding to the comments on the specified post
     */
    public Page<CommentDTO> getCommentsByPost(Long postId, int page, int size) {
        Post post = new Post();
        post.setPostId(postId);

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentRepository.findByPost(post, pageable);

        return commentPage.map(comment -> new CommentDTO(
                comment.getCommentId(),
                comment.getContent(),
                comment.getCreatedByUser().getFirstName(),
                comment.getCreatedByUser().getLastName(),
                comment.getCreatedAt()
        ));
    }

    /**
     * Adds a new comment to a specific post by a user.
     *
     * @param postId the ID of the post to comment on
     * @param userId the ID of the user adding the comment
     * @param content the content of the comment
     * @return a CommentDTO representing the added comment
     * @throws RuntimeException if the user is not found
     */
    public CommentDTO addComment(Long postId, Long userId, String content) {
        Post post = new Post();
        post.setPostId(postId);
        
        User user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setCreatedByUser(user);
        comment.setContent(content);
        
        comment = commentRepository.save(comment);
        return new CommentDTO(
                comment.getCommentId(),
                comment.getContent(),
                user.getFirstName(),
                user.getLastName(),
                comment.getCreatedAt()
        );
    }
}
