package com.ng.bsa.service;

import com.ng.bsa.entities.Role;
import com.ng.bsa.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;


    public Role findByName(String name){
        return roleRepository.findByName(name).get();
    }


}
