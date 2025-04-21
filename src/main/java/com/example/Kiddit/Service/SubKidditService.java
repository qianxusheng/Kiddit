package com.example.Kiddit.Service;

import com.example.Kiddit.Entity.SubKiddit;
import com.example.Kiddit.Repository.SubKidditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.Kiddit.DataTransferObject.SubKidditDTO;
import java.time.LocalDateTime;

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

        // Converts each SubKiddit entity to SubKidditDTO in the map
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

    /**
     * Adds a new SubKiddit entry.
     *
     * This method implements the "AddSubKiddit" feature.
     *
     * @param subKidditDTO the SubKiddit data provided by the client
     * @return the newly created SubKidditDTO
     */
    public SubKidditDTO addSubKiddit(SubKidditDTO subKidditDTO) {
        // Create a new SubKiddit entity and populate its fields from the DTO.
        SubKiddit subKiddit = new SubKiddit();
        subKiddit.setSubject(subKidditDTO.getSubject());
        subKiddit.setDescription(subKidditDTO.getDescription());
        // You might need to set category and createdByUser fields here, depending on your application design.
        subKiddit.setCreatedAt(LocalDateTime.now());
        
        // Save the entity
        SubKiddit savedSubKiddit = subKidditRepository.save(subKiddit);

        // Map the saved entity back to a DTO.
        // Here, if category and createdByUser are not set, those values may be null.
        return new SubKidditDTO(
                savedSubKiddit.getSubkidditId(),
                savedSubKiddit.getSubject(),
                savedSubKiddit.getDescription(),
                savedSubKiddit.getCategory() != null ? (long) savedSubKiddit.getCategory().getCategoryId() : null,
                savedSubKiddit.getCategory() != null ? savedSubKiddit.getCategory().getSubject() : null,
                savedSubKiddit.getCreatedAt(),
                savedSubKiddit.getCreatedByUser() != null ? (long) savedSubKiddit.getCreatedByUser().getUserId() : null,
                savedSubKiddit.getCreatedByUser() != null ? savedSubKiddit.getCreatedByUser().getFirstName() : null,
                savedSubKiddit.getCreatedByUser() != null ? savedSubKiddit.getCreatedByUser().getLastName() : null
        );
    }
}
