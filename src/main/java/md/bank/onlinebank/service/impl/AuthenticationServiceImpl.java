package md.bank.onlinebank.service.impl;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.UserDTO;
import md.bank.onlinebank.entity.User;
import md.bank.onlinebank.enums.Role;
import md.bank.onlinebank.exception.UserException;
import md.bank.onlinebank.repository.UserRepository;
import md.bank.onlinebank.service.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(UserDTO input) {
        var user = userRepository.findByUsername(input.getUsername());
        if (user.isPresent()) {
            throw new UserException("Username is already in use");
        }
        User newUser = new User();
        if(input.getRole() == null)
            newUser.setRole(Role.USER);
        else
            newUser.setRole(input.getRole());
        newUser.setUsername(input.getUsername());
        newUser.setPassword(passwordEncoder.encode(input.getPassword()));
        userRepository.save(newUser);
    }

    @Override
    public User authenticate(UserDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }
}
