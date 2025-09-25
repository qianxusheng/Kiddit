package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.SubCommentDTO;
import com.example.Kiddit.Entity.Comment;
import com.example.Kiddit.Entity.InappropriateComment;
import com.example.Kiddit.Entity.SubComment;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Repository.CommentRepository;
import com.example.Kiddit.Repository.InappropriateCommentRepository;
import com.example.Kiddit.Repository.SubCommentRepository;
import com.example.Kiddit.Repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubCommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private SubCommentRepository subCommentRepository;

    @Autowired
    private InappropriateCommentRepository inappropriateCommentRepository;

    @Autowired
    private UserRepository userRepository;
    
    // private final GptService gptService;

    // @Autowired
    // public SubCommentService(GptService gptService) {
    //     this.gptService = gptService;
    // }

    /**
     * Retrieves a paginated list of subcomments for a specific comment.
     * 
     * @param commentId the ID of the comment
     * @param page the page number to retrieve (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a Page of SubCommentDTOs for the specified comment
     */
    public Page<SubCommentDTO> getSubCommentsByCommentId(Long commentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SubComment> subComments = subCommentRepository.findByComment_CommentId(commentId, pageable);

        return subComments.map(subComment -> new SubCommentDTO(
                subComment.getSubCommentId(),
                subComment.getContent(),
                subComment.getCreatedByUser().getFirstName(),
                subComment.getCreatedByUser().getLastName(),
                subComment.getCreatedAt()
            )
        );
    }

    /**
     * Add a sub-comment to an existing comment.
     * 
     * @param parentCommentId the ID of the parent comment
     * @param userId the ID of the user creating the sub-comment
     * @param content the content of the sub-comment
     * @return the created SubComment DTO
     */
    public SubCommentDTO addSubComment(Long parentCommentId, Long userId, String content) {
        // 1. Get the parent comment from the database
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found with ID: " + parentCommentId));

        // 2. Get the user from the database
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // 3. Create the new SubComment
        SubComment subComment = new SubComment();
        subComment.setContent(content);
        subComment.setCreatedByUser(user);
        subComment.setComment(parentComment); // Linking sub-comment to the parent comment

        // 4. Set the current time for sub-comment creation
        subComment.setCreatedAt(LocalDateTime.now());

        // 5. Save the sub-comment to the database
        subComment = subCommentRepository.save(subComment);

        return new SubCommentDTO(
                subComment.getSubCommentId(),
                subComment.getContent(),
                subComment.getCreatedByUser().getFirstName(),
                subComment.getCreatedByUser().getLastName(),
                subComment.getCreatedAt()
            );
    }

    /**
     * This method saves an inappropriate comment to the database with a label and timestamp.
     *
     * @param content The content of the inappropriate comment
     * @param label The label (e.g., Offensive, Hate Speech, etc.)
     */
    private void saveInappropriateSubComment(String content, String label) {
        InappropriateComment inappropriateComment = new InappropriateComment();
        inappropriateComment.setContent(content);
        inappropriateComment.setLabel(label);

        // Save to the database
        inappropriateCommentRepository.save(inappropriateComment);
    }
    
    /**
     * This method checks the appropriateness of the provided sub-comment using GPT.
     * If the sub-comment is inappropriate, it saves the sub-comment to the database with a label.
     *
     * @param subCommentContent The content of the sub-comment to be checked
     * @param userId The ID of the user who posted the sub-comment
     * @param commentId The ID of the comment to which the sub-comment belongs
     * @return The final response regarding the appropriateness of the sub-comment
     */
    @Transactional
    public Map<String, String> handleSubComment(String subCommentContent, Long userId, Long commentId) {
        Map<String, String> response = new HashMap<>();
        
        // Check with GPT whether the sub-comment is appropriate (DISABLED)
        // String gptResponse = gptService.chatWithGpt(subCommentContent);

        // Parse the GPT response JSON (DISABLED)
        // JSONObject gptResponseJson = new JSONObject(gptResponse);
        // String label = gptResponseJson.optString("label");
        // String suggestion = gptResponseJson.optString("suggestion", "");  // Default to empty if no suggestion

        // If the sub-comment is inappropriate, store it with a label and suggestion (DISABLED)
        // if (!label.equals("Appropriate")) {
        //     // Save the inappropriate sub-comment with its label
        //     saveInappropriateSubComment(subCommentContent, label);
        // } else {
            // If the sub-comment is appropriate, add it to the comment (FOR NOW ALWAYS APPROPRIATE)
            addSubComment(commentId, userId, subCommentContent);
        // }

        // Return appropriate response (can be empty if no issue)
        response.put("label", "Appropriate");
        response.put("suggestion", "");

        return response;
    }
    
}
