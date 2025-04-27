package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.PostDTO;
import com.example.Kiddit.Service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Create a new post in a specific SubKiddit (i.e., a community or category).
     *
     * @param subKidditId the ID of the SubKiddit
     * @param postDTO the PostDTO containing the details of the post to be created
     * @param principal the authenticated user's principal
     * @return the created PostDTO
     */
    @PostMapping("/{subKidditId}/posts")
    public ResponseEntity<PostDTO> createPost(
            @PathVariable Long subKidditId,
            @RequestBody PostDTO postDto) {
        
        PostDTO createdPost = postService.createPost(subKidditId, postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    /**
     * Get a paginated list of posts under a specific SubKiddit (i.e., a community or category).
     *
     * @param subKidditId the ID of the SubKiddit
     * @param page the page number to retrieve (default is 0)
     * @param size the number of posts per page (default is 10)
     * @return a Page object containing PostDTOs for the requested SubKiddit
     */
    @GetMapping("/{subKidditId}/posts")
    public Page<PostDTO> getPostsBySubKiddit(
            @PathVariable Long subKidditId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsBySubKiddit(subKidditId, page, size);
    }

    /**
     * Retrieve a specific post by its ID.
     *
     * @param postId the ID of the post
     * @return the PostDTO representing the post's details
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long postId) {
        PostDTO post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }
}
