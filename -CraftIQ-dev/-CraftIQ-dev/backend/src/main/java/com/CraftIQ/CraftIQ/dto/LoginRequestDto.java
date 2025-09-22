package com.CraftIQ.CraftIQ.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequestDto {
    private String email;
    private String password;
    private String role;
}
