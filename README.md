# Spring Tx Board Banking Demo

This is a **complete banking demo application** that showcases all features of the Spring Tx Board transaction monitoring library. The demo simulates a real banking system with customers, accounts, transfers, and transactions to demonstrate how Spring Tx Board monitors and visualizes transaction performance.

> **Note:** This demo is a separate application that uses the Spring Tx Board library. When you use the library in your own projects, this demo code is not included.

## What You'll See

- **Real-time transaction monitoring** through the Spring Tx Board UI
- **Banking operations** like account creation, money transfers, and balance inquiries
- **Transaction performance metrics** including duration, status, and database connection usage
- **Interactive dashboards** showing transaction health and performance trends

## Prerequisites

Before running this demo, make sure you have:

- **Java 17 or higher** installed on your computer
- **Maven 3.6 or higher** installed
- **Internet connection** (to download dependencies)

### Check Your Java and Maven Installation

Open your command prompt and run these commands:

```cmd
java -version
mvn -version
```

If either command fails, you'll need to install Java and/or Maven first.

## Step-by-Step Setup Instructions

### Step 1: Get the Source Code

Clone the repository and navigate to the project:

```cmd
git clone https://github.com/Mamun-Al-Babu-Shikder/spring-tx-board.git
cd spring-tx-board
```

### Step 2: Install Spring Tx Board Library Locally

Before running the demo, you need to install the Spring Tx Board library to your local Maven repository:

```cmd
mvn clean install
```

This command will:
- Compile the Spring Tx Board library
- Run all tests
- Install the library to your local Maven repository (`~/.m2/repository/`)

**Expected output:** You should see `BUILD SUCCESS` at the end.

### Step 3: Navigate to Demo Directory

```cmd
cd tx-board-banking-demo
```

### Step 4: Run the Demo Application

```cmd
mvn spring-boot:run
```

**Wait for startup:** The application will start and you'll see log messages. Wait until you see something like:
```
Started TxBoardBankingDemoApplication in X.XXX seconds
```

### Step 5: Access the Application

Once the application is running, open your web browser and visit:

#### Main Interfaces

- **🏦 Banking Demo UI:** http://localhost:8080
- **📊 Spring Tx Board Dashboard:** http://localhost:8080/tx-board/ui
- **📋 API Documentation:** http://localhost:8080/swagger-ui/index.html
- **💾 Database Console:** http://localhost:8080/h2-console

#### Spring Tx Board Dashboard Features

The Spring Tx Board dashboard shows:
- **Transaction List:** All database transactions with timing and status
- **Performance Metrics:** Average duration, success rates, slow transactions
- **Real-time Updates:** Live transaction monitoring as you use the banking demo
- **Filtering Options:** Filter by status, duration, method name, etc.

## Testing the Demo

### Option A: Manual Testing

1. **Create a Customer:**
   - Use the banking interface to create a new customer
   - Watch the Spring Tx Board dashboard for transaction logs

2. **Create Bank Accounts:**
   - Create checking and savings accounts
   - Monitor transaction performance in real-time

3. **Make Transfers:**
   - Transfer money between accounts
   - Observe how Spring Tx Board captures transaction duration and status

### Option B: Automated Testing Scripts

For comprehensive testing, use the provided scripts in the `scripts/` folder:

#### Windows Users:
```cmd
# Using Command Prompt
scripts\test-endpoints.bat

# Using PowerShell (Recommended)
scripts\test-endpoints.ps1
```

#### Linux/Mac Users:
```bash
# Make executable first
chmod +x scripts/test-endpoints.sh
./scripts/test-endpoints.sh
```

### What the Scripts Test

The automated scripts will execute these banking scenarios:

1. **Customer Registration** - Create new customers
2. **Account Creation** - Create checking and savings accounts
3. **Money Deposits** - Add money to accounts
4. **Account Transfers** - Transfer money between accounts
5. **Balance Inquiries** - Check account balances
6. **Transaction History** - View transaction logs
7. **Bulk Operations** - Test performance with multiple transactions
8. **Error Scenarios** - Test insufficient funds and validation errors

**Watch the Spring Tx Board dashboard while the scripts run** to see real-time transaction monitoring!

## Understanding the Transaction Logs

While testing, observe these Spring Tx Board features:

### Transaction Duration
- **Green transactions:** Fast operations (< 1000ms)
- **Yellow/Red transactions:** Slower operations that may need attention

### Transaction Status
- **COMMITTED:** Successful transactions
- **ROLLED_BACK:** Failed transactions (like insufficient funds)

### Performance Metrics
- **Database connections used**
- **Query execution time**
- **Thread information**
- **Method names and parameters**

## Stopping the Application

To stop the demo application:
1. Go back to your command prompt where the application is running
2. Press `Ctrl + C`
3. Wait for the application to shut down gracefully

## Troubleshooting

### Common Issues

**Port 8080 already in use:**
```cmd
# Find what's using port 8080
netstat -ano | findstr 8080

# Kill the process (replace PID with actual process ID)
taskkill /PID <PID> /F
```

**Maven build fails:**
- Make sure you ran `mvn clean install` from the main project directory first
- Check that Java 17+ is installed and `JAVA_HOME` is set correctly

**Application won't start:**
- Ensure no other applications are using port 8080
- Check that the Spring Tx Board library was installed successfully in Step 2

### Getting Help

If you encounter issues:
1. Check the console output for error messages
2. Verify all prerequisites are installed correctly
3. Make sure you followed each step in order
4. Check the [main project issues](https://github.com/Mamun-Al-Babu-Shikder/spring-tx-board/issues) for common problems

## What's Next?

After exploring this demo:

1. **Study the code** to understand how Spring Tx Board integrates with Spring Boot applications
2. **Add Spring Tx Board to your own projects** using the instructions in the [main README](../README.md)
3. **Customize the monitoring** by adjusting configuration properties for your needs

## Demo Features Showcase

This banking demo specifically demonstrates:

- ✅ **Automatic transaction detection** with `@Transactional` methods
- ✅ **Performance monitoring** for database operations
- ✅ **Real-time dashboards** showing transaction health
- ✅ **Duration buckets** for performance analysis
- ✅ **Connection monitoring** for database efficiency
- ✅ **Error handling** and rollback scenarios
- ✅ **Configurable alerting** for slow transactions
- ✅ **Multiple transaction types** (read, write, transfer operations)

---

**Enjoy exploring Spring Tx Board with this banking demo!** 🏦📊
