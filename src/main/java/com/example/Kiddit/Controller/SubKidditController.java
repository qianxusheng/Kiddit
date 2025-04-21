package com.example.Kiddit.Controller;

import com.example.Kiddit.DataTransferObject.SubKidditDTO;
import com.example.Kiddit.Service.SubKidditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subkiddits")
public class SubKidditController {

    @Autowired
    private SubKidditService subKidditsService;

    /**
     * Get a paginated list of SubKiddits (communities) under a specific category.
     *
     * @param categoryId the ID of the category
     * @param page       the page number to retrieve (default is 0)
     * @param size       the number of items per page (default is 10)
     * @return a Page of SubKidditDTOs belonging to the specified category
     */
    @GetMapping("/category/{categoryId}")
    public Page<SubKidditDTO> getSubKidditsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return subKidditsService.getSubKidditsByCategory(categoryId, page, size);
    }

    /**
     * Add a new SubKiddit (community). Testing 
     *
     * This endpoint corresponds to the "AddSubKiddit" button functionality.
     *
     * @param subKidditDTO the SubKiddit object to be added
     * @return the created SubKidditDTO
     */
    @PostMapping
    public SubKidditDTO addSubKiddit(@RequestBody SubKidditDTO subKidditDTO) {
        return subKidditsService.addSubKiddit(subKidditDTO);
    }
}
