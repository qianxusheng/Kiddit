package com.example.Kiddit.Service;

import com.example.Kiddit.DataTransferObject.PostDTO;
import com.example.Kiddit.Entity.Post;
import com.example.Kiddit.Entity.SubKiddit;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Entity.VoteType;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Repository.PostRepository;
import com.example.Kiddit.Repository.SubKidditRepository;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SubKidditRepository subKidditRepository;

    @Autowired
    private PostVoteService postVoteService;

    /**
     * Creates a new post in the specified SubKiddit.
     * 
     * @param postDTO the PostDTO containing the details of the post to be created
     * @param subKidditId the ID of the SubKiddit where the post will be created
     * @return the created PostDTO
     */
    public PostDTO createPost(Long subKidditId, PostDTO postDto) {
        SubKiddit subKiddit = subKidditRepository.findById(subKidditId)
                .orElseThrow(() -> new RuntimeException("SubKiddit not found with ID: " + subKidditId));
        User user = userRepository.findByUserId(postDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + postDto.getUserId()));
        Post post = new Post();
        post.setSubject(postDto.getSubject());
        post.setDescription(postDto.getDescription());
        post.setSubkiddit(subKiddit);
        post.setCreatedAt(java.time.LocalDateTime.now());
        post.setCreatedByUser(user); // set user with ID
    
        Post savedPost = postRepository.save(post);
    
        PostDTO resultDto = new PostDTO();
        resultDto.setPostId(savedPost.getPostId());
        resultDto.setSubject(savedPost.getSubject());
        resultDto.setDescription(savedPost.getDescription());
        resultDto.setCreatedByFirstName(user.getFirstName());
        resultDto.setCreatedByLastName(user.getLastName());
        resultDto.setCreatedAt(savedPost.getCreatedAt());
    
        return resultDto;
    }
    
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
        
        Long userId = AuthUtils.getCurrentUserId();
        Page<Post> posts = postRepository.findBySubkiddit(subKiddit, PageRequest.of(page, size));
        
        // Collect all post IDs for batch vote and user vote status retrieval
        List<Long> postIds = posts.getContent().stream()
                                    .map(Post::getPostId)
                                    .collect(Collectors.toList());

        // Retrieve vote counts for the posts
        Map<Long, int[]> voteCountsMap = postVoteService.getVoteCounts(postIds);

        // Retrieve user vote status for the posts
        Map<Long, VoteType> userVoteStatusMap = postVoteService.getUserVoteStatus(postIds, userId);

        return posts.map(post -> {
            PostDTO postDTO = new PostDTO();
            postDTO.setPostId(post.getPostId());
            postDTO.setSubject(post.getSubject());
            postDTO.setDescription(post.getDescription());
            postDTO.setCreatedByFirstName(post.getCreatedByUser().getFirstName());
            postDTO.setCreatedByLastName(post.getCreatedByUser().getLastName());
            postDTO.setCreatedAt(post.getCreatedAt());
            postDTO.setUserId(userId);
            
            // Get upvotes and downvotes from the map
            int[] voteCounts = voteCountsMap.get(post.getPostId());
            if (voteCounts == null) {
                voteCounts = new int[]{0, 0}; // default values
            }
            
            postDTO.setUpvotes(voteCounts[0]);
            postDTO.setDownvotes(voteCounts[1]);

            // Set the user's vote status from the map
            postDTO.setUserVoteStatus(userVoteStatusMap.get(post.getPostId()));

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
