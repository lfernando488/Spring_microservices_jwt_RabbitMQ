package com.lfernando488.auth;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.lfernando488.auth.entity.Permission;
import com.lfernando488.auth.entity.User;
import com.lfernando488.auth.repositories.PermissionRepository;
import com.lfernando488.auth.repositories.UserRepository;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PermissionRepository permissionRepository, BCryptPasswordEncoder passwordEncoder) {
		return args -> {
			initUsers(userRepository, permissionRepository, passwordEncoder);
		};
	}
	
	private void initUsers(UserRepository userRepository, PermissionRepository permissionRepository, BCryptPasswordEncoder passwordEncoder) {
		Permission permission = null;
		Permission findPermission = permissionRepository.findByDescription("Admin");
		if(findPermission ==null){
			permission = new Permission();
			permission.setDescription("Admin");
			permission = permissionRepository.save(permission);
		}else {
			permission = findPermission;
		}
		
		User admin = new User();
		admin.setUserName("lfernando");
		admin.setAccountNonExpired(true);
		admin.setAccountNonLocked(true);
		admin.setCredentialsNonExpired(true);
		admin.setEnabled(true);
		admin.setPassword(passwordEncoder.encode("root"));
		admin.setPermissions(Arrays.asList(permission));
		
		User find = userRepository.findByUserName("lfernando488");
		if(find == null){
			userRepository.save(admin);
		}
		
	}
	
}

