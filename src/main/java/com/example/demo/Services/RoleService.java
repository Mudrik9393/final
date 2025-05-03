package com.example.demo.Services;

import java.util.List;
import com.example.demo.Repository.RoleRepository;
import com.example.demo.model.Role;

public class RoleService {
    
    private final RoleRepository roleRepository;  

    public RoleService(RoleRepository roleRepository) {  
        this.roleRepository = roleRepository;  
    }  

    public List<Role> getAllRole() {  
        return roleRepository.findAll();  
    }
}
