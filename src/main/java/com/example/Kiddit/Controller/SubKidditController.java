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

    @GetMapping("/category/{categoryId}")
    public Page<SubKidditDTO> getSubKidditsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return subKidditsService.getSubKidditsByCategory(categoryId, page, size);
    }
}
