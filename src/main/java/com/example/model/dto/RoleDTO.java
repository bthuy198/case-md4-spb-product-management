package com.example.model.dto;

import com.example.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    @NotNull(message = "Role must be not null")
    private Long id;
    private String code;

    public Role toRole() {
        return new Role()
                .setId(id)
                .setCode(code);
    }
}
