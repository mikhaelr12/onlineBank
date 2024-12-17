package md.bank.onlinebank.service.impl;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.UserDTO;
import md.bank.onlinebank.dto.request.UserFilterDTO;
import md.bank.onlinebank.entity.User;
import md.bank.onlinebank.repository.UserRepository;
import md.bank.onlinebank.service.AdminService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<UserDTO> getAllUsers(UserFilterDTO filter) {
        List<User> users = userRepository.findAll();

        if(filter != null && filter.getRole() != null){
            users = users.stream()
                    .filter(u -> u.getRole().equals(filter.getRole())
                    ).toList();
        }

        if(filter != null && StringUtils.hasText(filter.getUsername())){
            users = users.stream()
                    .filter(u -> u.getUsername().toLowerCase().contains(filter.getUsername().toLowerCase())
                    ).collect(toList());
        }

        return users.stream().map(u -> UserDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .role(u.getRole())
                .build()
        ).toList();
    }
}
