package com.lfernando488.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.lfernando488.auth.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{

	@Query("SELECT p from Permission p WHERE p.description =:description")
	Permission findByDescription(@Param("description") String desccription);
	
}
