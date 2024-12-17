package md.bank.onlinebank.service;

import md.bank.onlinebank.dto.UserDTO;
import md.bank.onlinebank.dto.request.UserFilterDTO;

import java.util.List;

public interface AdminService {
    List<UserDTO> getAllUsers(UserFilterDTO filter);
}
