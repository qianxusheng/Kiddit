package com.example.Kiddit.Service;

import com.example.Kiddit.Entity.Category;
import com.example.Kiddit.Repository.CategoryRepository;
import com.example.Kiddit.Repository.UserRepository;
import com.example.Kiddit.Entity.User;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * Retrieves the list of categories associated with a given user.
     *
     * @param user the user whose categories are to be retrieved
     * @return a list of categories associated with the user
     */
    public List<Category> getUserCategories(User user) {
        return user.getCategories();
    }

    /**
     * Retrieves detailed information about a specific category by its ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return the Category object corresponding to the given ID
     * @throws IllegalArgumentException if the category is not found
     */
    public Category getCategoryInformation(int categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    /**
     * Retrieves all categories from the repository.
     *
     * @return a list of all categories
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * Adds categories to a user's profile based on the provided list of category IDs.
     *
     * @param userId the ID of the user to whom categories will be added
     * @param categoryIds a list of category IDs to be added to the user's profile
     * @throws IllegalArgumentException if the user or any category is not found
     */
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

    /**
     * Removes a specific category from a user's profile.
     *
     * @param userId the ID of the user whose category will be removed
     * @param categoryId the ID of the category to be removed
     * @return true if the category was successfully removed, false otherwise
     * @throws IllegalArgumentException if the user or the category is not found
     */
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

    /**
     * Retrieves a paginated list of categories associated with a given user.
     *
     * @param userId the ID of the user whose categories are to be retrieved
     * @param page the page number to retrieve (default is 0)
     * @param size the number of items per page (default is 10)
     * @return a Page of Category objects associated with the user
     * @throws IllegalArgumentException if the user is not found
     */
    public Page<Category> getUserCategoriesForUser(int userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        return categoryRepository.findByUsersContaining(user, pageable);
    }

}
