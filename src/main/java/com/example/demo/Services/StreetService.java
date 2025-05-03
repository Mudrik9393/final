package com.example.demo.Services;

import java.util.List;
import com.example.demo.Repository.StreetRepository;

import com.example.demo.model.Street;

public class StreetService {
   
    private final StreetRepository streetRepository;  

    public StreetService(StreetRepository streetRepository) {  
        this.streetRepository = streetRepository;  
    }  

    public List<Street> getAllStreet() {  
        return streetRepository.findAll();  
    }

     public Street createComplaint(Street street){
        return streetRepository.save(street);
    }

}
