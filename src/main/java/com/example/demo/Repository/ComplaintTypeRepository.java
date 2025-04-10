package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ComplaintType;
import com.example.demo.model.Role;

public interface ComplaintTypeRepository extends JpaRepository<ComplaintType, String> {

    Role save(ComplaintTypeRepository complaintTypeRepository);
}
