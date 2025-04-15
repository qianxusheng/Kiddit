package com.example.Kiddit.Service;

import com.example.Kiddit.Entity.SubKiddit;
import com.example.Kiddit.Repository.SubKidditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.Kiddit.DataTransferObject.SubKidditDTO;

@Service
public class SubKidditService {

    @Autowired
    private SubKidditRepository subKidditRepository;

    /**
     * Retrieves a paginated list of SubKiddits for a given category ID.
     * 
     * @param categoryId the ID of the category to filter SubKiddits by
     * @param page the page number to retrieve (starting from 0)
     * @param size the number of SubKiddits per page
     * @return a Page of SubKidditDTOs containing SubKiddits for the specified category
     */
    public Page<SubKidditDTO> getSubKidditsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Creates a Pageable object for pagination
        Page<SubKiddit> subKidditsPage = subKidditRepository.findByCategory_CategoryId(categoryId, pageable); // Fetches the SubKiddits from the repository

        // Converts each SubKiddit entity to SubKidditDTO directly in the map
        return subKidditsPage.map(subKiddit -> new SubKidditDTO(
                subKiddit.getSubkidditId(),
                subKiddit.getSubject(),
                subKiddit.getDescription(),
                (long) subKiddit.getCategory().getCategoryId(),
                subKiddit.getCategory().getSubject(),
                subKiddit.getCreatedAt(),
                (long) subKiddit.getCreatedByUser().getUserId(),
                subKiddit.getCreatedByUser().getFirstName(),
                subKiddit.getCreatedByUser().getLastName()
        ));
    }
}
