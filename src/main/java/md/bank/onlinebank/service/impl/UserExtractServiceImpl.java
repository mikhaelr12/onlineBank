package md.bank.onlinebank.service.impl;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.entity.User;
import md.bank.onlinebank.exception.UserException;
import md.bank.onlinebank.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserExtractServiceImpl {

    private JwtServiceImpl jwtService;
    private UserRepository userRepository;

    public User getUser(String jwtToken) {
        String username = jwtService.extractUsername(jwtToken);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("No user found"));
    }
}
