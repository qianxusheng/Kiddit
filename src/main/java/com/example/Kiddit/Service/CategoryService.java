package com.example.Kiddit.Service;

import com.example.Kiddit.Entity.Category;
import com.example.Kiddit.Repository.CategoryRepository;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Entity.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository; 

    public CategoryService(CategoryRepository categoryRepository, 
                           UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Category> getUserCategories(User user) {
        return user.getCategories();
    }

    public Category getCategoryInformation(int categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void addUserCategories(int userId, List<Integer> categoryIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        for (Integer categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    
            user.getCategories().add(category);  
        }

        userRepository.save(user);
    }

    public boolean removeUserCategory(int userId, int categoryId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found."));
    
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new IllegalArgumentException("Category not found."));
    
        boolean removed = user.getCategories().remove(category);
    
        if (removed) {
            userRepository.save(user); 
            return true;
        }

        return false;
    }

    public Page<Category> getUserCategoriesForUser(int userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        return categoryRepository.findByUsersContaining(user, pageable);
    }

}
