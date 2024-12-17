package md.bank.onlinebank.dto;

import lombok.*;
import md.bank.onlinebank.enums.Role;

@Data @Builder
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Role role;
}
