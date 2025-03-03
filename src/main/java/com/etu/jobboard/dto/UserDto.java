package com.etu.jobboard.dto;

import com.etu.jobboard.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "email format cannot rgiht")
    private String email;

    private String password;

    @NotNull(message = "role cannot be empty")
    private User.Role role;

    // 从User实体转换为DTO
    public static UserDto fromEntity(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                // 不要传输密码
                .role(user.getRole())
                .build();
    }

    // 转换为User实体
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setName(this.name);
        user.setEmail(this.email);
        if (this.password != null && !this.password.isEmpty()) {
            user.setPassword(this.password);
        }
        user.setRole(this.role);
        return user;
    }
}
