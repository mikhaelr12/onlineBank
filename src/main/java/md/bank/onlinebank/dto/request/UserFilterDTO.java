package md.bank.onlinebank.dto.request;

import lombok.Data;
import md.bank.onlinebank.enums.Role;

@Data
public class UserFilterDTO {
    private String username;
    private Role role;
}
