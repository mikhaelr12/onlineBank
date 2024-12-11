package md.bank.onlinebank.controller;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.AccountDTO;
import md.bank.onlinebank.service.AccountService;
import md.bank.onlinebank.service.impl.TokenExtractServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;
    private TokenExtractServiceImpl tokenExtractService;


    @GetMapping
    public ResponseEntity<AccountDTO> getAccount(@RequestHeader("Authorization") String token) {
        String jwt = tokenExtractService.getToken(token);
        return ResponseEntity.ok(accountService.getAccount(jwt));
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDTO accountDTO,
                                           @RequestHeader("Authorization") String token) {

        String jwt = tokenExtractService.getToken(token);
        accountService.createAccount(jwt, accountDTO);
        return ResponseEntity.ok("Account created");
    }

    @PutMapping("/deposit")
    public ResponseEntity<?> depositAccount(@RequestHeader("Authorization") String token,
                                            @RequestBody AccountDTO accountDTO) {

        String jwt = tokenExtractService.getToken(token);
        accountService.depositAccount(jwt,accountDTO);
        return ResponseEntity.ok("Money deposited");
    }

    @PutMapping("/{id}/currency")
    public ResponseEntity<?> changeCurrency(@RequestHeader("Authorization") String token,
                                            @PathVariable Long id,
                                            @RequestBody AccountDTO accountDTO) {

        String jwt = tokenExtractService.getToken(token);
        accountService.changeCurrency(jwt,id,accountDTO);
        return ResponseEntity.ok("Currency changed");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@RequestHeader("Authorization") String token,
                                           @PathVariable Long id){
        String jwt = tokenExtractService.getToken(token);
        accountService.deleteAccount(jwt,id);
        return ResponseEntity.ok("Account deleted");
    }
}
