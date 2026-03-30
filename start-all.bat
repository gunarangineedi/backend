@echo off
:: ============================================================
::   LOKA Backend — Start All Services (Windows)
::   Run from: LOKA\backend\
:: ============================================================

title LOKA Backend

echo.
echo   ================================
echo   LOKA Luxury eCommerce — Backend
echo   ================================
echo.

:: Setup databases
echo [1/5] Setting up MySQL databases...
mysql -u root -proot < setup-database.sql 2>nul
echo      Done.
echo.

:: Build all
echo [2/5] Building all services (may take a minute)...
for %%S in (eureka-server api-gateway user-service product-service order-service) do (
    echo      Building %%S...
    cd %%S
    call mvn clean package -DskipTests -q
    cd ..
)
echo      All built.
echo.

:: Start services in separate windows
echo [3/5] Starting Eureka Server (port 8761)...
start "LOKA-Eureka" cmd /k "cd eureka-server && java -jar target\eureka-server-1.0.0.jar"
timeout /t 15 /nobreak >nul

echo [4/5] Starting API Gateway (port 8080)...
start "LOKA-Gateway" cmd /k "cd api-gateway && java -jar target\api-gateway-1.0.0.jar"
timeout /t 8 /nobreak >nul

echo      Starting User Service (port 8081)...
start "LOKA-Users" cmd /k "cd user-service && java -jar target\user-service-1.0.0.jar"
timeout /t 8 /nobreak >nul

echo      Starting Product Service (port 8082)...
start "LOKA-Products" cmd /k "cd product-service && java -jar target\product-service-1.0.0.jar"
timeout /t 8 /nobreak >nul

echo      Starting Order Service (port 8083)...
start "LOKA-Orders" cmd /k "cd order-service && java -jar target\order-service-1.0.0.jar"
timeout /t 8 /nobreak >nul

echo.
echo   ================================
echo   All LOKA services are running!
echo   ================================
echo.
echo   Eureka Dashboard:  http://localhost:8761
echo   API Gateway:       http://localhost:8080
echo   User Service:      http://localhost:8081
echo   Product Service:   http://localhost:8082
echo   Order Service:     http://localhost:8083
echo.
echo   Demo Login: demo@loka.com / password123
echo.
pause
