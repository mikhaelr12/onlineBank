package md.bank.onlinebank.controller;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.TransactionDTO;
import md.bank.onlinebank.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private String getToken(String token){
        return token.startsWith("Bearer ") ? token.substring(7) : token;
    }

    @PutMapping
    public ResponseEntity<?> makeTransaction(@RequestHeader("Authorization") String token,
                                             @RequestBody TransactionDTO transactionDTO){
        String jwt = getToken(token);
        transactionService.makeTransaction(jwt, transactionDTO);
        return ResponseEntity.ok("Successful transaction");
    }
}
