package com.sdlc.pro.txboard.demo.bank.controller;

import com.sdlc.pro.txboard.demo.bank.entity.Transfer;
import com.sdlc.pro.txboard.demo.bank.service.AccrualService;
import com.sdlc.pro.txboard.demo.bank.service.PortfolioService;
import com.sdlc.pro.txboard.demo.bank.service.TransferFacade;
import com.sdlc.pro.txboard.demo.bank.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/bank")
@Tag(name = "Banking Operations", description = "Banking demo endpoints to showcase Spring Tx Board transaction monitoring features")
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
    @Operation(
        summary = "Execute a healthy transfer",
        description = "Performs a standard money transfer between two accounts. This represents a healthy transaction scenario that will show as INFO level in Spring Tx Board."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transfer.class))),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Transfer> transfer() {
        // Use predefined accounts from seed data
        Transfer result = transferService.transfer(1L, 2L, new BigDecimal("100.00"), "Demo healthy transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 2: Slow transfer (WARN on tx duration)
    @PostMapping("/transfer/slow")
    @Operation(
        summary = "Execute a slow transfer",
        description = "Performs a deliberately slow money transfer that will trigger transaction duration warnings in Spring Tx Board. Useful for testing WARN level logging."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Slow transfer completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transfer.class))),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Transfer> transferSlow() {
        Transfer result = transferService.transferSlow(1L, 3L, new BigDecimal("50.00"), "Demo slow transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 3: Long-held connection (WARN on connection occupancy)
    @PostMapping("/transfer/hold-connection")
    @Operation(
        summary = "Execute transfer with long-held connection",
        description = "Performs a transfer while holding database connection for extended time. This will trigger connection occupancy warnings in Spring Tx Board."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transfer with connection hold completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transfer.class))),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Transfer> transferHoldConnection() {
        Transfer result = transferService.transferHoldingConnection(2L, 4L, new BigDecimal("75.00"), "Demo connection hold transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 4: Rollback/Error paths
    @PostMapping("/transfer/rollback")
    @Operation(
        summary = "Execute transfer that will rollback",
        description = "Attempts a transfer that is designed to fail and rollback. This demonstrates error handling and rollback scenarios in Spring Tx Board."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rollback scenario completed (with error message)",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Expected failure for demonstration")
    })
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
    @Operation(
        summary = "Execute nested transaction transfer",
        description = "Performs a transfer using nested transactions and multiple services. This creates a transaction tree structure visible in Spring Tx Board logs."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Nested transaction transfer completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transfer.class))),
        @ApiResponse(responseCode = "400", description = "Invalid transfer request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Transfer> transferNested() {
        Transfer result = transferFacade.transferWithNestedServices(3L, 4L, new BigDecimal("200.00"), "Demo nested transfer");
        return ResponseEntity.ok(result);
    }

    // Scenario 7: N+1 detection (WARN)
    @GetMapping("/portfolio/nplus1")
    @Operation(
        summary = "Trigger N+1 query problem",
        description = "Executes a portfolio query that demonstrates the N+1 query problem. This will trigger WARN level alerts in Spring Tx Board for inefficient database access patterns."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Portfolio data retrieved (with N+1 queries)",
            content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> portfolioNPlusOne() {
        Map<String, Object> result = portfolioService.customerPortfolioNPlusOne();
        return ResponseEntity.ok(result);
    }

    // Scenario 8: TransactionTemplate
    @PostMapping("/accrual/template")
    @Operation(
        summary = "Execute accrual using TransactionTemplate",
        description = "Performs daily interest accrual using Spring's TransactionTemplate for programmatic transaction management. Shows template-based transaction handling in Spring Tx Board."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Daily interest accrual completed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> accrualTemplate() {
        accrualService.postDailyInterest(transactionTemplate);
        return ResponseEntity.ok("Daily interest accrual completed");
    }

    // Helper endpoint to get demo info
    @GetMapping("/info")
    @Operation(
        summary = "Get demo application information",
        description = "Returns information about the Banking Demo Application including available endpoints and useful URLs for monitoring and debugging."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Demo information retrieved successfully",
            content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Map<String, String>> getDemoInfo() {
        Map<String, String> info = new java.util.HashMap<>();
        info.put("message", "Banking Demo Application");
        info.put("txBoardUI", "http://localhost:8080/tx-board/ui");
        info.put("h2Console", "http://localhost:8080/h2-console");
        info.put("swaggerUI", "http://localhost:8080/swagger-ui/index.html");
        info.put("apiDocs", "http://localhost:8080/v3/api-docs");
        info.put("endpoints", "POST /bank/transfer, /bank/transfer/slow, /bank/transfer/hold-connection, /bank/transfer/rollback, /bank/transfer/nested, GET /bank/portfolio/nplus1, POST /bank/accrual/template");
        return ResponseEntity.ok(info);
    }
}
