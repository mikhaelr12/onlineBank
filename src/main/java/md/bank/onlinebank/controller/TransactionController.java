package md.bank.onlinebank.controller;

import lombok.AllArgsConstructor;
import md.bank.onlinebank.dto.TransactionDTO;
import md.bank.onlinebank.dto.request.TransactionFilterDTO;
import md.bank.onlinebank.service.TransactionService;
import md.bank.onlinebank.service.impl.TokenExtractServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TokenExtractServiceImpl tokenExtractService;

    @PostMapping
    public ResponseEntity<?> makeTransaction(@RequestBody TransactionDTO transactionDTO,
                                             @RequestHeader("Authorization") String token) {
        String jwt = tokenExtractService.getToken(token);
        transactionService.makeTransaction(jwt, transactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionDTO);
    }

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAllTransactions(@RequestHeader("Authorization") String token,
                                                                   @RequestBody TransactionFilterDTO transactionFilterDTO) {
        String jwt = tokenExtractService.getToken(token);
        return ResponseEntity.ok(transactionService.getAllTransactions(jwt, transactionFilterDTO));
    }
}
