package com.sdlc.pro.txboard.demo.bank.controller;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import com.sdlc.pro.txboard.demo.bank.service.AccrualService;
import com.sdlc.pro.txboard.demo.bank.service.PortfolioService;
import com.sdlc.pro.txboard.demo.bank.service.TransferFacade;
import com.sdlc.pro.txboard.demo.bank.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/bank")
public class BankingController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private TransferFacade transferFacade;

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private AccrualService accrualService;

    @Autowired
    private TransactionTemplate transactionTemplate;

    // Scenario 1: Healthy transfer (INFO)
    @PostMapping("/transfer")
    public ResponseEntity<Transfer> transfer() {
        // Use predefined accounts from seed data
        Transfer result = transferService.transfer(1L, 2L, new BigDecimal("100.00"), "Demo healthy transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 2: Slow transfer (WARN on tx duration)
    @PostMapping("/transfer/slow")
    public ResponseEntity<Transfer> transferSlow() {
        Transfer result = transferService.transferSlow(1L, 3L, new BigDecimal("50.00"), "Demo slow transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 3: Long-held connection (WARN on connection occupancy)
    @PostMapping("/transfer/hold-connection")
    public ResponseEntity<Transfer> transferHoldConnection() {
        Transfer result = transferService.transferHoldingConnection(2L, 4L, new BigDecimal("75.00"), "Demo connection hold transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 4: Rollback/Error paths
    @PostMapping("/transfer/rollback")
    public ResponseEntity<String> transferRollback() {
        try {
            transferService.transferAndFail(1L, 2L, new BigDecimal("25.00"), "Demo failed transfer");
            return ResponseEntity.ok("This should not happen");
        } catch (Exception e) {
            return ResponseEntity.ok("Transfer failed as expected: " + e.getMessage());
        }
    }

    // Scenario 5: Nested transactions (tree in logs)
    @PostMapping("/transfer/nested")
    public ResponseEntity<Transfer> transferNested() {
        Transfer result = transferFacade.transferWithNestedServices(3L, 4L, new BigDecimal("200.00"), "Demo nested transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 7: N+1 detection (WARN)
    @GetMapping("/portfolio/nplus1")
    public ResponseEntity<Map<String, Object>> portfolioNPlusOne() {
        Map<String, Object> result = portfolioService.customerPortfolioNPlusOne();
        return ResponseEntity.ok(result);
    }

    // Scenario 8: TransactionTemplate
    @PostMapping("/accrual/template")
    public ResponseEntity<String> accrualTemplate() {
        accrualService.postDailyInterest(transactionTemplate);
        return ResponseEntity.ok("Daily interest accrual completed");
    }

    // Helper endpoint to get demo info
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getDemoInfo() {
        return ResponseEntity.ok(Map.of(
            "message", "Banking Demo Application",
            "txBoardUI", "http://localhost:8080/tx-board/ui",
            "h2Console", "http://localhost:8080/h2-console",
            "endpoints", "POST /bank/transfer, /bank/transfer/slow, /bank/transfer/hold-connection, /bank/transfer/rollback, /bank/transfer/nested, GET /bank/portfolio/nplus1, POST /bank/accrual/template"
        ));
    }
}
