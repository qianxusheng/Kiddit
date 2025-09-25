package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.CommentDTO;
import com.example.Kiddit.Entity.InappropriateComment;
import com.example.Kiddit.Entity.Comment;
import com.example.Kiddit.Entity.Post;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.VoteType;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Repository.CommentRepository;
import com.example.Kiddit.Repository.InappropriateCommentRepository;
import com.example.Kiddit.Repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentVoteService commentVoteService;

    @Autowired
    private InappropriateCommentRepository inappropriateCommentRepository;
    
    // private final GptService gptService;

    // @Autowired
    // public CommentService(GptService gptService) {
    //     this.gptService = gptService;
    // }

    /**
     * Retrieves a paginated list of comments for a specific post.
     *
     * @param postId the ID of the post whose comments are to be fetched
     * @param page the page number to retrieve (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a Page of CommentDTOs corresponding to the comments on the specified post
     */
    public Page<CommentDTO> getCommentsByPost(Long postId, int page, int size) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postId));
        
        Long userId = AuthUtils.getCurrentUserId();
        Page<Comment> comments = commentRepository.findByPost(post, PageRequest.of(page, size));
        List<Long> commentIds = comments.getContent().stream()
                                .map(Comment::getCommentId)
                                .collect(Collectors.toList());

        Map<Long, int[]> voteCountsMap = commentVoteService.getVoteCounts(commentIds);
        Map<Long, VoteType> userVoteStatusMap = commentVoteService.getUserVoteStatus(commentIds, userId);
                                
        return comments.map(comment->{
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreatedByFirstName(comment.getCreatedByUser().getFirstName());
            commentDTO.setCreatedByLastName(comment.getCreatedByUser().getLastName());
            commentDTO.setCreatedAt(comment.getCreatedAt());

            int[] voteCounts = voteCountsMap.getOrDefault(comment.getCommentId(), new int[]{0, 0});
            commentDTO.setUpvotes(voteCounts[0]);
            commentDTO.setDownvotes(voteCounts[1]);
    
            commentDTO.setUserVoteStatus(userVoteStatusMap.getOrDefault(comment.getCommentId(), null));

            return commentDTO;
        });
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
    
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setCreatedByFirstName(user.getFirstName());
        dto.setCreatedByLastName(user.getLastName());
        dto.setCreatedAt(comment.getCreatedAt());
    
        return dto;
    }

    /**
     * This method saves an inappropriate comment to the database with a label and timestamp.
     *
     * @param content The content of the inappropriate comment
     * @param label The label (e.g., Offensive, Hate Speech, etc.)
     */
    private void saveInappropriateComment(String content, String label) {
        InappropriateComment inappropriateComment = new InappropriateComment();
        inappropriateComment.setContent(content);
        inappropriateComment.setLabel(label);

        // Save to the database
        inappropriateCommentRepository.save(inappropriateComment);
    }
    
    /**
     * This method checks the appropriateness of the provided comment using GPT.
     * If the comment is inappropriate, it saves the comment to the database with a label.
     *
     * @param commentContent The content of the comment to be checked
     * @param userId The ID of the user who posted the comment
     * @param postId The ID of the post to which the comment belongs
     * @return The final response regarding the appropriateness of the comment
     */
    @Transactional
    public Map<String, String> handleComment(String commentContent, Long userId, Long postId) {
        Map<String, String> response = new HashMap<>();
        
        // Check with GPT whether the comment is appropriate (DISABLED)
        // String gptResponse = gptService.chatWithGpt(commentContent);

        // Parse the GPT response JSON (DISABLED)
        // JSONObject gptResponseJson = new JSONObject(gptResponse);
        // String label = gptResponseJson.optString("label");
        // String suggestion = gptResponseJson.optString("suggestion", "");  // Default to empty if no suggestion
        // If the comment is inappropriate, store it with a label and suggestion (DISABLED)
        // if (!label.equals("Appropriate")) {
        //     // Save the inappropriate comment with its label
        //     saveInappropriateComment(commentContent, label);
        // } else {
            // If the comment is appropriate, add it to the post (FOR NOW ALWAYS APPROPRIATE)
            addComment(postId, userId, commentContent);
        // }
        
        // Return appropriate response (can be empty if no issue)
        response.put("label", "Appropriate");
        response.put("suggestion", "");

        return response;
    }
    
}
