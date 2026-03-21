# Load Tests (k6)

## Prerequisites

Install k6: https://grafana.com/docs/k6/latest/set-up/install-k6/

```bash
# macOS
brew install k6

# Ubuntu/Debian
sudo gpg -k
sudo gpg --no-default-keyring --keyring /usr/share/keyrings/k6-archive-keyring.gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys C5AD17C747E3415A3642D57D77C6C491D6AC1D68
echo "deb [signed-by=/usr/share/keyrings/k6-archive-keyring.gpg] https://dl.k6.io/deb stable main" | sudo tee /etc/apt/sources.list.d/k6.list
sudo apt-get update && sudo apt-get install k6
```

## Running Tests

Make sure all services are running (via docker-compose + gradle bootRun).

```bash
# Product service load test (no auth required)
k6 run product-service-load.js

# Auth service load test
k6 run auth-service-load.js

# Order flow load test (requires JWT token)
k6 run -e JWT_TOKEN=<your-access-token> order-flow-load.js

# Override base URL
k6 run -e BASE_URL=http://localhost:8080 product-service-load.js
```

## Thresholds

| Metric | Target |
|--------|--------|
| p(95) response time | < 500ms (products), < 800ms (auth), < 1000ms (orders) |
| p(99) response time | < 1000ms (products), < 1500ms (auth), < 2000ms (orders) |
| Error rate | < 5% (products), < 10% (auth/orders) |
