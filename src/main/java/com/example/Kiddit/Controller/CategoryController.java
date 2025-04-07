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

    @GetMapping("/users/{userId}/categories/all")
    public ResponseEntity<List<Category>> getUserCategories(@PathVariable int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<Category> categories = categoryService.getUserCategories(user);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/users/{userId}/categories")
    public ResponseEntity<Page<Category>> getUserCategories(
            @PathVariable int userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Category> categories = categoryService.getUserCategoriesForUser(userId, page, size);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<Category> getCategoryInformation(@PathVariable int categoryId) {
        Category category = categoryService.getCategoryInformation(categoryId);
        return ResponseEntity.ok(category);
    }

    @GetMapping("/profile/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/profile/{userId}/categories")
    public ResponseEntity<String> addUserCategories(@PathVariable int userId, @RequestBody Map<String, List<Integer>> categoryIdsMap) {
        try {
            List<Integer> categoryIds = categoryIdsMap.get("categoryIds");
            if (categoryIds == null || categoryIds.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No categories selected.");
            }
            categoryService.addUserCategories(userId, categoryIds);
    
            return ResponseEntity.ok("User categories updated successfully");
    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

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
