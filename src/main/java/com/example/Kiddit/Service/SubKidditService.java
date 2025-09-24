package com.example.Kiddit.Service;

import com.example.Kiddit.Entity.SubkidditCategoryUserView;
import com.example.Kiddit.Repository.SubkidditCategoryUserViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.Kiddit.DataTransferObject.SubKidditDTO;

@Service
public class SubKidditService {

    @Autowired
    private SubkidditCategoryUserViewRepository subkidditCategoryUserViewRepository;

    public Page<SubKidditDTO> getSubKidditsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // Creates a Pageable object for pagination
        Page<SubkidditCategoryUserView> subKidditsPage = subkidditCategoryUserViewRepository.findByCategoryId(categoryId, pageable); // Fetches the SubKiddits from the view

        // Converts each SubKidditCategoryUserView entity to SubKidditDTO directly in the map
        return subKidditsPage.map(subKiddit -> new SubKidditDTO(
                subKiddit.getSubkidditId(),
                subKiddit.getSubkidditSubject(),
                subKiddit.getSubkidditDescription(),
                subKiddit.getCategoryId(),
                subKiddit.getCategorySubject(),
                subKiddit.getCreatedAt(),
                subKiddit.getCreatorUserId(),
                subKiddit.getCreatorFirstName(),
                subKiddit.getCreatorLastName()
        ));
    }

}
