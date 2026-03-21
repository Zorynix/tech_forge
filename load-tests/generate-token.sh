#!/bin/bash
# Generates a JWT token for k6 load testing using the dev secret from auth-service/.env
# Usage: ./generate-token.sh
# Then:  k6 run -e JWT_TOKEN=$(./generate-token.sh) order-flow-load.js

SECRET="techmarket-dev-secret-key-change-in-production-min-32-chars!!"
USER_ID="00000000-0000-4000-a000-000000000001"
PHONE="+79991234567"

# Header: {"alg":"HS384"} — JJWT auto-selects HS384 for keys 48-63 bytes (secret is 61 bytes)
HEADER=$(echo -n '{"alg":"HS384"}' | base64 -w0 | tr '+/' '-_' | tr -d '=')

# Payload with 24h expiration
NOW=$(date +%s)
EXP=$((NOW + 86400))
PAYLOAD=$(echo -n "{\"sub\":\"${USER_ID}\",\"phone\":\"${PHONE}\",\"type\":\"access\",\"roles\":[\"ROLE_USER\"],\"iat\":${NOW},\"exp\":${EXP}}" | base64 -w0 | tr '+/' '-_' | tr -d '=')

# Sign with HMAC-SHA384
SIGNATURE=$(echo -n "${HEADER}.${PAYLOAD}" | openssl dgst -sha384 -hmac "${SECRET}" -binary | base64 -w0 | tr '+/' '-_' | tr -d '=')

echo "${HEADER}.${PAYLOAD}.${SIGNATURE}"
