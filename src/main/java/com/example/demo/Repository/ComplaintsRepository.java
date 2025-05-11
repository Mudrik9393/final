package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.demo.model.Complaints;


    public interface ComplaintsRepository extends JpaRepository<Complaints, String> {
}

