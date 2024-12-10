package md.bank.onlinebank.controller;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.AccountDTO;
import md.bank.onlinebank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;

    private String getToken(String token){
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(@RequestHeader("Authorization") String token) {
        String jwt = getToken(token);
        return ResponseEntity.ok(accountService.getAllAcounts(jwt));
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO,
                                           @RequestHeader("Authorization") String token) {
        String jwt = getToken(token);
        accountService.createAccount(jwt, accountDTO);
        return ResponseEntity.ok("Account created");
    }

    @PutMapping("/deposit/{id}")
    public ResponseEntity<?> depositAccount(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id,
                                            @RequestBody AccountDTO accountDTO) {
        String jwt = getToken(token);
        accountService.depositAccount(jwt,id,accountDTO);
        return ResponseEntity.ok("Money deposited");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeCurrency(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id,
                                            @RequestBody AccountDTO accountDTO) {
        String jwt = getToken(token);
        accountService.changeCurrency(jwt,id,accountDTO);
        return ResponseEntity.ok("Currency changed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String token,
                                           @PathVariable Long id){
        String jwt = getToken(token);
        accountService.deleteAccount(jwt,id);
        return ResponseEntity.ok("Account deleted");
    }
}
