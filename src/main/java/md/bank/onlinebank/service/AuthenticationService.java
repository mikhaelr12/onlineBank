package md.bank.onlinebank.service;

import md.bank.onlinebank.dto.UserDTO;
import md.bank.onlinebank.entity.User;

public interface AuthenticationService {
    void signup(UserDTO input);

    User authenticate(UserDTO input);
}
