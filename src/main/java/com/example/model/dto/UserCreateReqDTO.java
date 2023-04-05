package com.example.model.dto;

import com.example.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateReqDTO {
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(max = 20, message = "The maximum length of username is 20 characters")
    @Size(min = 4, message = "The minimum length of username is 4 characters")
    private String username;

    private String password;
    private RoleDTO role;

    public UserCreateReqDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public User toUser() {
        return new User()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setRole(role.toRole());
    }
}
