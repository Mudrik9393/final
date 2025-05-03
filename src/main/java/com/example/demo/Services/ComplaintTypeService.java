package com.example.demo.Services;

import java.util.List;
import com.example.demo.Repository.ComplaintTypeRepository;
import com.example.demo.model.ComplaintType;

public class ComplaintTypeService {
    
    private final ComplaintTypeRepository complaintTypeRepository;  

    public ComplaintTypeService(ComplaintTypeRepository complaintTypeRepository) {  
        this.complaintTypeRepository = complaintTypeRepository;  
    }  

    public List<ComplaintType> getAllUser() {  
        return complaintTypeRepository.findAll();  
    }

     public ComplaintType createComplaintType(ComplaintType complaintType){
        return complaintTypeRepository.save(complaintType);
    }

}
