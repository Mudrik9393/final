package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Complaints;


    public interface ComplaintsRepository extends JpaRepository<Complaints, Long> {
    @Query("SELECT c.complaintName, COUNT(c) FROM Complaints c GROUP BY c.complaintName")
    List<Object[]> countComplaintsByType();
    
    @Query("SELECT MAX(c.id) FROM Complaints c")
    Long findMaxComplaintId();
    
    @Query("SELECT COUNT(c) FROM Complaints c WHERE c.id > :minId")
    Long countComplaintsAfterId(Long minId);
}


