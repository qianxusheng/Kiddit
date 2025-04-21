package com.example.Kiddit.DataTransferObject;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor  // Added so that frameworks can instantiate SubKidditDTO via reflection
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
