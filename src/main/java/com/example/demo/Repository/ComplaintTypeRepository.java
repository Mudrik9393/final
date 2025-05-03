package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ComplaintType;


public interface ComplaintTypeRepository extends JpaRepository<ComplaintType, String> {

}
