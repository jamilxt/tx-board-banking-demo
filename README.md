# Spring Tx Board Banking Demo

This is a complete banking demo application that showcases all features of the Spring Tx Board transaction monitoring library.

## Overview

The demo simulates a banking system with customers, accounts, transfers, and ledger entries. It exercises every Spring Tx Board feature through realistic banking scenarios.

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+
- Internet access (for JitPack dependency)

### Running the Demo

1. **Start the application:**
   ```cmd
   cd demo
   mvn spring-boot:run
   ```

2. **Access the interfaces:**
   - **Spring Tx Board UI:** http://localhost:8080/tx-board/ui
   - **Swagger UI:** http://localhost:8080/swagger-ui/index.html
   - **H2 Database Console:** http://localhost:8080/h2-console
   - **Demo Info:** http://localhost:8080/bank/info

## Automated Testing Scripts

For easy testing of all endpoints, use the provided cross-platform scripts in the `scripts/` folder:

### Windows Users
```cmd
# Command Prompt
scripts\test-endpoints.bat

# PowerShell (Recommended)
scripts\test-endpoints.ps1
```

### Linux/Mac Users
```bash
# Make executable first
chmod +x scripts/test-endpoints.sh
./scripts/test-endpoints.sh
```

### What the Scripts Test
The automated scripts will execute all 8 demo scenarios in sequence:
1. **Application Info** - Basic endpoint verification
2. **Healthy Transfer** - Normal transaction (INFO level)
3. **Slow Transfer** - Duration warnings (WARN level)
4. **Connection Hold** - Connection occupancy warnings
5. **Rollback Scenario** - Transaction failure handling
6. **Nested Transactions** - Complex transaction trees
7. **N+1 Query Problem** - Query efficiency warnings
8. **TransactionTemplate** - Programmatic transactions

**📁 See `scripts/README-SCRIPTS.md` for detailed script documentation and troubleshooting.**

## Manual Demo Scenarios

If you prefer to test endpoints manually, execute these commands to trigger different Spring Tx Board features:

#### 1. Healthy Transfer (INFO logging)
```cmd
curl -X POST http://localhost:8080/bank/transfer
```
- **Expected:** INFO log with transaction details
- **Features:** Basic transaction lifecycle capture, connection tracking

#### 2. Slow Transfer (WARN on transaction duration)
```cmd
curl -X POST http://localhost:8080/bank/transfer/slow
```
- **Expected:** WARN log due to >500ms duration
- **Features:** Duration threshold detection, alarming

#### 3. Connection Hold Transfer (WARN on connection occupancy)
```cmd
curl -X POST http://localhost:8080/bank/transfer/hold-connection
```
- **Expected:** WARN log due to >250ms connection hold time
- **Features:** Connection occupancy tracking, alarming

#### 4. Failed Transfer (Rollback demonstration)
```cmd
curl -X POST http://localhost:8080/bank/transfer/rollback
```
- **Expected:** ROLLED_BACK status in logs and UI
- **Features:** Transaction rollback tracking

#### 5. Nested Transactions (Transaction tree)
```cmd
curl -X POST http://localhost:8080/bank/transfer/nested
```
- **Expected:** Nested transaction tree in DETAILS logs
- **Features:** Parent/child transaction relationships, different propagation behaviors

#### 6. N+1 Query Detection
```cmd
curl http://localhost:8080/bank/portfolio/nplus1
```
- **Expected:** WARN log about potential N+1 pattern
- **Features:** N+1 query pattern detection

#### 7. TransactionTemplate Usage
```cmd
curl -X POST http://localhost:8080/bank/accrual/template
```
- **Expected:** Transaction captured without @Transactional annotation
- **Features:** TransactionTemplate support

## Observing Results

### Console Logs
Watch the application console for:
- **INFO logs** for healthy transactions
- **WARN logs** for slow/problematic transactions
- **Nested transaction trees** (when log-type=DETAILS)

### Spring Tx Board UI
Visit http://localhost:8080/tx-board/ui to see:
- Real-time transaction list
- Filtering by status, propagation, isolation
- Duration distribution charts
- Transaction summaries

### REST API
Query the Spring Tx Board API directly:

```cmd
# Get transaction summary
curl http://localhost:8080/api/spring-tx-board/tx-summary

# Get transaction logs (paginated, sorted by duration)
curl "http://localhost:8080/api/spring-tx-board/tx-logs?page=0&size=10&sort=duration,DESC"

# Filter transactions
curl "http://localhost:8080/api/spring-tx-board/tx-logs?status=COMMITTED&search=transfer"

# Get duration distribution charts
curl http://localhost:8080/api/spring-tx-board/tx-charts

# Get alarming thresholds
curl http://localhost:8080/api/spring-tx-board/config/alarming-threshold
```

## Configuration Toggle Examples

### Switch to Simple Logging
Edit `application.yml`:
```yaml
sdlc.pro.spring.tx.board.log-type: SIMPLE
```
Restart and run scenarios to see simplified log format.

### Adjust Thresholds
```yaml
sdlc.pro.spring.tx.board.alarming-threshold:
  transaction: 200  # Lower threshold = more WARNs
  connection: 100
```

### Change Duration Buckets
```yaml
sdlc.pro.spring.tx.board.duration-buckets: [50, 200, 500, 1000, 3000]
```

## Database Schema

The demo uses H2 in-memory database with these tables:
- `customers` - Bank customers
- `accounts` - Customer accounts with balances
- `ledger_entries` - All account movements
- `transfers` - Money transfer records
- `audit_logs` - Transfer audit trail

Access H2 console at http://localhost:8080/h2-console:
- **JDBC URL:** `jdbc:h2:mem:bankdemo`
- **Username:** `sa`
- **Password:** (empty)

## Features Demonstrated

| Feature | Scenario | Endpoint | Expected Behavior |
|---------|----------|----------|-------------------|
| Basic transaction tracking | Healthy transfer | `POST /bank/transfer` | INFO log, captured metrics |
| Duration threshold | Slow transfer | `POST /bank/transfer/slow` | WARN log, >500ms duration |
| Connection threshold | Connection hold | `POST /bank/transfer/hold-connection` | WARN log, >250ms connection hold |
| Rollback tracking | Failed transfer | `POST /bank/transfer/rollback` | ROLLED_BACK status |
| Nested transactions | Nested services | `POST /bank/transfer/nested` | Transaction tree in logs |
| N+1 detection | Portfolio query | `GET /bank/portfolio/nplus1` | N+1 warning |
| TransactionTemplate | Interest accrual | `POST /bank/accrual/template` | Template transaction capture |
| Isolation levels | Various services | All endpoints | READ_COMMITTED, REPEATABLE_READ |
| Propagation | Risk/Audit services | `POST /bank/transfer/nested` | REQUIRED, REQUIRES_NEW |

## Testing the Demo

Run all scenarios in sequence:
```cmd
curl -X POST http://localhost:8080/bank/transfer
curl -X POST http://localhost:8080/bank/transfer/slow  
curl -X POST http://localhost:8080/bank/transfer/hold-connection
curl -X POST http://localhost:8080/bank/transfer/rollback
curl -X POST http://localhost:8080/bank/transfer/nested
curl http://localhost:8080/bank/portfolio/nplus1
curl -X POST http://localhost:8080/bank/accrual/template
```

Then check:
1. Console logs for INFO/WARN patterns
2. UI at http://localhost:8080/tx-board/ui for visual data
3. REST API responses for programmatic access

## Troubleshooting

**Port 8080 already in use:**
Add to `application.yml`: `server.port: 8081`

**JitPack dependency issues:**
Ensure internet connectivity and Maven can access repositories.

**No transactions appearing:**
Check that `sdlc.pro.spring.tx.board.enable: true` in configuration.
