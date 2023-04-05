package com.example.service.role;

import com.example.model.Role;
import com.example.model.dto.RoleDTO;
import com.example.service.IGeneralService;

import java.util.List;

public interface IRoleService extends IGeneralService<Role> {
    List<RoleDTO> getAllRoleDTO();
}
