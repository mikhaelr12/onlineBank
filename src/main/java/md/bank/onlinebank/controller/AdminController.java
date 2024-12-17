package md.bank.onlinebank.controller;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.UserDTO;
import md.bank.onlinebank.dto.request.UserFilterDTO;
import md.bank.onlinebank.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestBody(required = false)UserFilterDTO filter) {
        return ResponseEntity.ok(adminService.getAllUsers(filter));
    }
}
