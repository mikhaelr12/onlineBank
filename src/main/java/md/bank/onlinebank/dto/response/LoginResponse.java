package md.bank.onlinebank.dto.response;

import lombok.*;

@Getter @Setter
public class LoginResponse {
    private String token;
    private Long expiresIn;
}
