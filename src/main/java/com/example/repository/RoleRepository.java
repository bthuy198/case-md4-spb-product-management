package com.example.repository;

import com.example.model.dto.RoleDTO;
import com.example.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT NEW com.example.model.dto.RoleDTO (" +
            "r.id, " +
            "r.code" +
            ") " +
            "FROM Role AS r "
    )
    List<RoleDTO> getAllRoleDTO();
}
