package com.example.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
   @Query("SELECT r.document, COUNT(r) FROM Request r GROUP BY r.document")
    List<Object[]> countRequestsByDocumentType();
    
    @Query("SELECT COUNT(r) FROM Request r WHERE r.document LIKE '%Pending%'")
    Long countPendingRequests();
}
