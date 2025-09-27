#!/bin/bash

# Banking Demo API Test Scripts for Linux/Mac
# Make sure the application is running on http://localhost:8080

echo "============================================"
echo "Banking Demo API Test Scripts"
echo "============================================"
echo

BASE_URL="http://localhost:8080/bank"

echo "Testing application info endpoint..."
curl -X GET "$BASE_URL/info" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "1. Healthy Transfer (INFO level)"
echo "============================================"
curl -X POST "$BASE_URL/transfer" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "2. Slow Transfer (WARN on duration)"
echo "============================================"
curl -X POST "$BASE_URL/transfer/slow" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "3. Transfer with Connection Hold (WARN)"
echo "============================================"
curl -X POST "$BASE_URL/transfer/hold-connection" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "4. Transfer Rollback Scenario"
echo "============================================"
curl -X POST "$BASE_URL/transfer/rollback" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "5. Nested Transaction Transfer"
echo "============================================"
curl -X POST "$BASE_URL/transfer/nested" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "6. Portfolio N+1 Query Problem (WARN)"
echo "============================================"
curl -X GET "$BASE_URL/portfolio/nplus1" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "7. Accrual with TransactionTemplate"
echo "============================================"
curl -X POST "$BASE_URL/accrual/template" -H "Content-Type: application/json"
echo
echo

echo "============================================"
echo "All tests completed!"
echo "Check Spring Tx Board UI: http://localhost:8080/tx-board/ui"
echo "Check Swagger UI: http://localhost:8080/swagger-ui/index.html"
echo "============================================"

read -p "Press Enter to continue..."
