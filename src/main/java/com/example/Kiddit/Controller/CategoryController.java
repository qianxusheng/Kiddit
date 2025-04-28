package com.example.Kiddit.Controller;

import com.example.Kiddit.Entity.Category;
import com.example.Kiddit.Entity.User;
import com.example.Kiddit.Service.CategoryService;
import com.example.Kiddit.Repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    public CategoryController(CategoryService categoryService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    /**
     * Retrieve all categories associated with the specified user.
     * This returns the user's selected/favorite categories.
     *
     * @param userId the ID of the user
     * @return list of categories the user is associated with
     */
    @GetMapping("/users/{userId}/categories/all")
    public ResponseEntity<List<Category>> getUserCategories(@PathVariable int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Category> categories = categoryService.getUserCategories(user);
        return ResponseEntity.ok(categories);
    }

    /**
     * Retrieve paginated categories associated with the specified user.
     * Useful when the user has a large number of categories.
     *
     * @param userId the ID of the user
     * @param page the page number (default 0)
     * @param size the size of each page (default 10)
     * @return paginated categories of the user
     */
    @GetMapping("/users/{userId}/categories")
    public ResponseEntity<Page<Category>> getUserCategories(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Category> categories = categoryService.getUserCategoriesForUser(userId, page, size);
        return ResponseEntity.ok(categories);
    }

    /**
     * Retrieve detailed information about a specific category.
     *
     * @param categoryId the ID of the category
     * @return the category details
     */
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategoryInformation(@PathVariable int categoryId) {
        Category category = categoryService.getCategoryInformation(categoryId);
        return ResponseEntity.ok(category);
    }

    /**
     * Get a list of all available categories.
     * This can be used on the profile page for users to choose interests.
     *
     * @return list of all categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Add or update categories that a user is interested in.
     * Accepts a JSON body with a list of category IDs.
     *
     * @param userId the ID of the user
     * @param categoryIdsMap a JSON map with key "categoryIds" containing a list of category IDs
     * @return success or error message
     */
    @PutMapping("/profile/{userId}/categories")
    public ResponseEntity<Map<String, String>> addUserCategories(@PathVariable int userId, @RequestBody Map<String, List<Integer>> categoryIdsMap) {
        List<Integer> categoryIds = categoryIdsMap.get("categoryIds");
        
        categoryService.addUserCategories(userId, categoryIds);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User categories updated successfully");
        
        return ResponseEntity.ok(response);
    }

    /**
     * Remove a specific category from a user's selected categories.
     *
     * @param userId the ID of the user
     * @param categoryId the ID of the category to remove
     * @return message indicating success or failure
     */
    @DeleteMapping("/{userId}/categories/{categoryId}")
    public ResponseEntity<Map<String, String>> removeUserCategory(
            @PathVariable int userId,
            @PathVariable int categoryId) {
    
        boolean removed = categoryService.removeUserCategory(userId, categoryId);
    
        if (removed) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Category removed successfully"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "Category not found for the user"));
        }
    }
}
