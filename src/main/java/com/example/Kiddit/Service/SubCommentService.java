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

