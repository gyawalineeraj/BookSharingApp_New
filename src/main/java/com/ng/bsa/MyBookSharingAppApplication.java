package com.ng.bsa;

import com.ng.bsa.entities.Role;
import com.ng.bsa.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.management.relation.RoleInfo;

@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@RequiredArgsConstructor
public class

giMyBookSharingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyBookSharingAppApplication.class, args);
	}

private final RoleRepository roleRepository;
	@PostConstruct
	public void doSth(){
		Role role = Role.builder()
				.name("ADMIN")
				.build();
try {
	roleRepository.save(role);
} catch (Exception e) {

}


    }

}
