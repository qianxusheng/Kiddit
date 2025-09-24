package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.PostDTO;
import com.example.Kiddit.Entity.Post;
import com.example.Kiddit.Entity.SubKiddit;
import com.example.Kiddit.Repository.PostRepository;
import com.example.Kiddit.Repository.SubKidditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubKidditRepository subKidditRepository;

    /**
     * Retrieves a paginated list of posts belonging to a specific SubKiddit.
     * 
     * @param subKidditId the ID of the SubKiddit
     * @param page the page number to retrieve (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a Page of PostDTOs for the specified SubKiddit
     */
    public Page<PostDTO> getPostsBySubKiddit(Long subKidditId, int page, int size) {
        SubKiddit subKiddit = subKidditRepository.findById(subKidditId)
                .orElseThrow(() -> new RuntimeException("SubKiddit not found with ID: " + subKidditId));

        Page<Post> posts = postRepository.findBySubkiddit(subKiddit, PageRequest.of(page, size));

        return posts.map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setSubject(post.getSubject());
            postDTO.setDescription(post.getDescription());
            postDTO.setCreatedByFirstName(post.getCreatedByUser().getFirstName());
            postDTO.setCreatedByLastName(post.getCreatedByUser().getLastName());
            postDTO.setCreatedAt(post.getCreatedAt());
            return postDTO;
        });
    }

    /**
     * Retrieves a specific post by its ID.
     * 
     * @param postId the ID of the post
     * @return the PostDTO corresponding to the post with the given ID
     */
    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
                
        PostDTO postDTO = new PostDTO();
        postDTO.setPostId(post.getPostId());
        postDTO.setSubject(post.getSubject());
        postDTO.setDescription(post.getDescription());
        postDTO.setCreatedByFirstName(post.getCreatedByUser().getFirstName());
        postDTO.setCreatedByLastName(post.getCreatedByUser().getLastName());
        postDTO.setCreatedAt(post.getCreatedAt());

        return postDTO;
    }
}
