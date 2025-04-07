package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.SubCommentDTO; 
import com.example.Kiddit.Service.SubCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SubCommentController {

    @Autowired
    private SubCommentService subCommentService;

    @GetMapping("/comment/{commentId}/subcomments")
    public ResponseEntity<Page<SubCommentDTO>> getSubCommentsByCommentId(
            @PathVariable Long commentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<SubCommentDTO> subComments = subCommentService.getSubCommentsByCommentId(commentId, page, size);
        return ResponseEntity.ok(subComments);
    }
}

