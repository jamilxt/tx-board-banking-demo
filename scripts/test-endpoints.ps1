# Banking Demo API Test Scripts for Windows PowerShell
# Make sure the application is running on http://localhost:8080

Write-Host "============================================" -ForegroundColor Green
Write-Host "Banking Demo API Test Scripts (PowerShell)" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""

$BaseUrl = "http://localhost:8080/bank"
$Headers = @{ "Content-Type" = "application/json" }

Write-Host "Testing application info endpoint..." -ForegroundColor Yellow
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/info" -Method GET -Headers $Headers
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "1. Healthy Transfer (INFO level)" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/transfer" -Method POST -Headers $Headers
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "2. Slow Transfer (WARN on duration)" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/transfer/slow" -Method POST -Headers $Headers
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "3. Transfer with Connection Hold (WARN)" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/transfer/hold-connection" -Method POST -Headers $Headers
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "4. Transfer Rollback Scenario" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/transfer/rollback" -Method POST -Headers $Headers
    Write-Host $response -ForegroundColor Cyan
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "5. Nested Transaction Transfer" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/transfer/nested" -Method POST -Headers $Headers
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "6. Portfolio N+1 Query Problem (WARN)" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/portfolio/nplus1" -Method GET -Headers $Headers
    $response | ConvertTo-Json -Depth 3
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "7. Accrual with TransactionTemplate" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
try {
    $response = Invoke-RestMethod -Uri "$BaseUrl/accrual/template" -Method POST -Headers $Headers
    Write-Host $response -ForegroundColor Cyan
} catch {
    Write-Host "Error: $_" -ForegroundColor Red
}
Write-Host ""
Write-Host ""

Write-Host "============================================" -ForegroundColor Green
Write-Host "All tests completed!" -ForegroundColor Green
Write-Host "Check Spring Tx Board UI: http://localhost:8080/tx-board/ui" -ForegroundColor Yellow
Write-Host "Check Swagger UI: http://localhost:8080/swagger-ui/index.html" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Green

Read-Host "Press Enter to continue"
