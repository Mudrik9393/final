package com.example.demo.Services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.ComplaintsRepository;
import com.example.demo.Repository.RequestRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.model.Complaints;

@Service
public class DashboardService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RequestRepository requestRepository;
    
    @Autowired
    private ComplaintsRepository complaintsRepository;
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // User stats
        stats.put("totalUsers", userRepository.count());
        stats.put("recentUsers", getRecentUserCount()); // Custom method
        
        // Request stats
        stats.put("totalRequests", requestRepository.count());
        stats.put("pendingRequests", requestRepository.countPendingRequests());
        
        // Complaint stats
        stats.put("totalComplaints", complaintsRepository.count());
        stats.put("recentComplaints", getRecentComplaintCount()); // Custom method
        
        return stats;
    }
    
    private Long getRecentUserCount() {
        // Approximation using max ID
        Long maxId = userRepository.findMaxUserId();
        return maxId != null ? userRepository.countUsersAfterId(maxId - 10) : 0L;
    }
    
    private Long getRecentComplaintCount() {
        // Approximation using max ID
        Long maxId = complaintsRepository.findMaxComplaintId();
        return maxId != null ? complaintsRepository.countComplaintsAfterId(maxId - 5) : 0L;
    }
    
    public List<Map<String, Object>> getRequestDistribution() {
        List<Object[]> results = requestRepository.countRequestsByDocumentType();
        return results.stream()
            .map(obj -> Map.of(
                "type", obj[0],
                "count", obj[1]
            ))
            .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getComplaintDistribution() {
        List<Object[]> results = complaintsRepository.countComplaintsByType();
        return results.stream()
            .map(obj -> Map.of(
                "type", obj[0],
                "count", obj[1]
            ))
            .collect(Collectors.toList());
    }
}