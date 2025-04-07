package com.example.Kiddit.DataTransferObject;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubKidditDTO {
    private Long subkidditId;         
    private String subject;           
    private String description;      
    private Long categoryId;          
    private String categoryName;      
    private LocalDateTime createdAt;  
    private Long createdByUserId;    
    private String createdByFirstName; 
    private String createdByLastName;  
}
