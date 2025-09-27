# Banking Demo API Testing Scripts

This directory contains scripts to test all the Banking Demo API endpoints that showcase Spring Tx Board transaction monitoring features.

## Prerequisites

1. **Application Running**: Make sure the Banking Demo application is running on `http://localhost:8080`
2. **Start the app**: Run `mvn spring-boot:run` from this directory
3. **cURL installed**: Required for .bat and .sh scripts (usually pre-installed on Linux/Mac)

## Available Scripts

### Windows Users

#### Option 1: Command Prompt (Batch Script)
```cmd
test-endpoints.bat
```

#### Option 2: PowerShell (Recommended for Windows)
```powershell
.\test-endpoints.ps1
```

### Linux/Mac Users

#### Shell Script
```bash
# Make executable first (Linux/Mac only)
chmod +x test-endpoints.sh
./test-endpoints.sh
```

## What Each Endpoint Tests

### 1. `/bank/info` - Application Information
- **Method**: GET
- **Purpose**: Returns demo app information and useful URLs
- **Spring Tx Board**: Basic endpoint information

### 2. `/bank/transfer` - Healthy Transfer
- **Method**: POST
- **Purpose**: Standard money transfer (Account 1 → Account 2, $100)
- **Spring Tx Board**: Shows **INFO** level transaction logging

### 3. `/bank/transfer/slow` - Slow Transfer
- **Method**: POST
- **Purpose**: Deliberately slow transfer (Account 1 → Account 3, $50)
- **Spring Tx Board**: Triggers **WARN** level for transaction duration

### 4. `/bank/transfer/hold-connection` - Connection Hold
- **Method**: POST
- **Purpose**: Transfer while holding DB connection (Account 2 → Account 4, $75)
- **Spring Tx Board**: Triggers **WARN** level for connection occupancy

### 5. `/bank/transfer/rollback` - Rollback Scenario
- **Method**: POST
- **Purpose**: Transfer designed to fail and rollback
- **Spring Tx Board**: Shows transaction rollback behavior

### 6. `/bank/transfer/nested` - Nested Transactions
- **Method**: POST
- **Purpose**: Complex transfer with nested services (Account 3 → Account 4, $200)
- **Spring Tx Board**: Shows transaction tree structure

### 7. `/bank/portfolio/nplus1` - N+1 Query Problem
- **Method**: GET
- **Purpose**: Demonstrates N+1 query anti-pattern
- **Spring Tx Board**: Triggers **WARN** level for inefficient queries

### 8. `/bank/accrual/template` - TransactionTemplate
- **Method**: POST
- **Purpose**: Uses programmatic transaction management
- **Spring Tx Board**: Shows TransactionTemplate usage

## After Running Scripts

1. **Spring Tx Board UI**: http://localhost:8080/tx-board/ui
   - View transaction logs and monitoring data
   - See WARN/INFO classifications
   - Analyze transaction trees and performance

2. **Swagger UI**: http://localhost:8080/swagger-ui/index.html
   - Interactive API documentation
   - Test endpoints manually
   - View detailed API specifications

3. **H2 Database Console**: http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:bankdemo`
   - Username: `sa`
   - Password: (empty)

## Sample Data

The application initializes with:
- **5 customers**: John Doe, Jane Smith, Bob Johnson, Alice Williams, Charlie Brown
- **8 accounts**: Various currencies (USD, GBP, EUR) with different balances
- **Account IDs 1-8** are available for transfers

## Troubleshooting

### Script Won't Run
- **Windows**: Run Command Prompt or PowerShell as Administrator
- **Linux/Mac**: Ensure script is executable: `chmod +x test-endpoints.sh`

### cURL Not Found
- **Windows**: Install cURL or use PowerShell script instead
- **Linux**: `sudo apt-get install curl` (Ubuntu/Debian)
- **Mac**: cURL is pre-installed

### Connection Refused
- Ensure application is running: `mvn spring-boot:run`
- Check if port 8080 is available
- Wait for application to fully start (look for startup messages)

### Application Not Starting
- Check Java version: `java -version` (requires Java 17+)
- Run: `mvn clean compile` to check for compilation errors
- Check logs for specific error messages

## Manual Testing with cURL

If you prefer manual testing, here are example commands:

```bash
# Get app info
curl -X GET "http://localhost:8080/bank/info"

# Execute healthy transfer
curl -X POST "http://localhost:8080/bank/transfer"

# Execute slow transfer
curl -X POST "http://localhost:8080/bank/transfer/slow"
```

## Development

The scripts test all scenarios designed to showcase Spring Tx Board's monitoring capabilities:
- Transaction duration monitoring
- Connection occupancy tracking
- N+1 query detection
- Transaction rollback handling
- Nested transaction visualization
- Programmatic transaction management
