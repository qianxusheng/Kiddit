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

    public Page<SubKidditDTO> getSubKidditsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SubKiddit> subKidditsPage = subKidditRepository.findByCategory_CategoryId(categoryId, pageable);

        return subKidditsPage.map(this::convertToDTO); 
    }

    private SubKidditDTO convertToDTO(SubKiddit subKiddit) {
        SubKidditDTO dto = new SubKidditDTO();
        dto.setSubkidditId(subKiddit.getSubkidditId());
        dto.setSubject(subKiddit.getSubject());
        dto.setDescription(subKiddit.getDescription());
        dto.setCategoryId((long) subKiddit.getCategory().getCategoryId());
        dto.setCategoryName(subKiddit.getCategory().getSubject()); 
        dto.setCreatedAt(subKiddit.getCreatedAt());
        dto.setCreatedByUserId((long) subKiddit.getCreatedByUser().getUserId());
        dto.setCreatedByFirstName(subKiddit.getCreatedByUser().getFirstName());
        dto.setCreatedByLastName(subKiddit.getCreatedByUser().getLastName());
        return dto;
    }
}
