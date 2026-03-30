#!/bin/bash
# ============================================================
#   LOKA Backend — Start All Services
#   Run from: LOKA/backend/
# ============================================================

set -e

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
GOLD='\033[0;33m'
NC='\033[0m'

echo ""
echo -e "${GOLD}  ██╗      ██████╗ ██╗  ██╗ █████╗ ${NC}"
echo -e "${GOLD}  ██║     ██╔═══██╗██║ ██╔╝██╔══██╗${NC}"
echo -e "${GOLD}  ██║     ██║   ██║█████╔╝ ███████║${NC}"
echo -e "${GOLD}  ██║     ██║   ██║██╔═██╗ ██╔══██║${NC}"
echo -e "${GOLD}  ███████╗╚██████╔╝██║  ██╗██║  ██║${NC}"
echo -e "${GOLD}  ╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝${NC}"
echo ""
echo -e "${BLUE}  Luxury eCommerce Platform — Backend Startup${NC}"
echo -e "${BLUE}  ============================================${NC}"
echo ""

# Check MySQL
echo -e "${YELLOW}► Checking MySQL...${NC}"
if ! mysql -u root -proot -e "SELECT 1;" &>/dev/null; then
    echo -e "${RED}✗ MySQL is not running or credentials are wrong.${NC}"
    echo -e "  Please start MySQL and ensure root/root credentials work."
    echo -e "  Or update application.yml files with correct credentials."
    exit 1
fi
echo -e "${GREEN}✓ MySQL is running${NC}"

# Create databases
echo -e "${YELLOW}► Setting up databases...${NC}"
mysql -u root -proot < setup-database.sql 2>/dev/null || true
echo -e "${GREEN}✓ Databases ready${NC}"

# Build all services
echo ""
echo -e "${YELLOW}► Building all services (this may take a minute)...${NC}"
for service in eureka-server api-gateway user-service product-service order-service; do
    echo -e "  Building ${service}..."
    cd $service
    mvn clean package -DskipTests -q
    cd ..
done
echo -e "${GREEN}✓ All services built${NC}"

echo ""
echo -e "${YELLOW}► Starting services...${NC}"

# 1. Eureka Server
echo -e "  Starting Eureka Server on port 8761..."
cd eureka-server
java -jar target/eureka-server-1.0.0.jar &
EUREKA_PID=$!
cd ..
sleep 12
echo -e "${GREEN}  ✓ Eureka Server started (PID: $EUREKA_PID)${NC}"

# 2. API Gateway
echo -e "  Starting API Gateway on port 8080..."
cd api-gateway
java -jar target/api-gateway-1.0.0.jar &
GATEWAY_PID=$!
cd ..
sleep 8
echo -e "${GREEN}  ✓ API Gateway started (PID: $GATEWAY_PID)${NC}"

# 3. User Service
echo -e "  Starting User Service on port 8081..."
cd user-service
java -jar target/user-service-1.0.0.jar &
USER_PID=$!
cd ..
sleep 8
echo -e "${GREEN}  ✓ User Service started (PID: $USER_PID)${NC}"

# 4. Product Service
echo -e "  Starting Product Service on port 8082..."
cd product-service
java -jar target/product-service-1.0.0.jar &
PRODUCT_PID=$!
cd ..
sleep 8
echo -e "${GREEN}  ✓ Product Service started (PID: $PRODUCT_PID)${NC}"

# 5. Order Service
echo -e "  Starting Order Service on port 8083..."
cd order-service
java -jar target/order-service-1.0.0.jar &
ORDER_PID=$!
cd ..
sleep 8
echo -e "${GREEN}  ✓ Order Service started (PID: $ORDER_PID)${NC}"

echo ""
echo -e "${GOLD}  ════════════════════════════════════════${NC}"
echo -e "${GOLD}  All LOKA services are running!${NC}"
echo -e "${GOLD}  ════════════════════════════════════════${NC}"
echo ""
echo -e "  ${GREEN}Eureka Dashboard:${NC}  http://localhost:8761"
echo -e "  ${GREEN}API Gateway:${NC}       http://localhost:8080"
echo -e "  ${GREEN}User Service:${NC}      http://localhost:8081"
echo -e "  ${GREEN}Product Service:${NC}   http://localhost:8082"
echo -e "  ${GREEN}Order Service:${NC}     http://localhost:8083"
echo ""
echo -e "  ${YELLOW}Demo Login:${NC} demo@loka.com / password123"
echo ""
echo -e "  Press Ctrl+C to stop all services"
echo ""

# Wait
trap "echo ''; echo -e '${RED}Stopping all LOKA services...${NC}'; kill $EUREKA_PID $GATEWAY_PID $USER_PID $PRODUCT_PID $ORDER_PID 2>/dev/null; exit 0" SIGINT SIGTERM
wait
