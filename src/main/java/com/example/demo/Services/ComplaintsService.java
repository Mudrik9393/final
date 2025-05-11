package com.example.demo.Services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.Repository.ComplaintsRepository;
import com.example.demo.model.Complaints;

@Service
public class ComplaintsService {

    private final ComplaintsRepository complaintsRepository;

    public ComplaintsService(ComplaintsRepository complaintsRepository) {
        this.complaintsRepository = complaintsRepository;
    }

    public List<Complaints> getAllComplaints() {  
        return complaintsRepository.findAll();  
    }

    public Complaints createComplaint(Complaints complaints) {
        return complaintsRepository.save(complaints);
    }
}
