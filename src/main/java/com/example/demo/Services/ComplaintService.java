package com.example.demo.Services;

import java.util.List;

import com.example.demo.Repository.ComplaintRepository;
import com.example.demo.model.Complaint;


public class ComplaintService {
    private final ComplaintRepository complaintRepository;  

    public ComplaintService(ComplaintRepository complaintRepository) {  
        this.complaintRepository = complaintRepository;  
    }  

    public List<Complaint> getAllUser() {  
        return complaintRepository.findAll();  
    }

     public Complaint createComplaint(Complaint complaint){
        return complaintRepository.save(complaint);
    }

     
}