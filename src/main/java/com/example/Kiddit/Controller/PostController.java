package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.PostDTO;
import com.example.Kiddit.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{subKidditId}/posts")
    public Page<PostDTO> getPostsBySubKiddit(
            @PathVariable Long subKidditId,
            @RequestParam(defaultValue = "0") int page,  
            @RequestParam(defaultValue = "10") int size  
    ) {
        return postService.getPostsBySubKiddit(subKidditId, page, size);
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }
}
