package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.SubCommentDTO;
import com.example.Kiddit.Entity.SubComment;
import com.example.Kiddit.Repository.SubCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SubCommentService {

    @Autowired
    private SubCommentRepository subCommentRepository;

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
}
