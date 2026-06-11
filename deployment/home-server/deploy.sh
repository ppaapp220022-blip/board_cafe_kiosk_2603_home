#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
DEPLOY_PATH="${BOARDWAVE_DEPLOY_PATH:-/srv/docker/boardwave/deployment/home-server}"
ENV_SOURCE="${BOARDWAVE_ENV_FILE:-/srv/config/boardwave/home-server.env}"

if [[ ! -f "$ENV_SOURCE" ]]; then
  echo "Missing deploy env file: $ENV_SOURCE" >&2
  exit 1
fi

mkdir -p "$DEPLOY_PATH"
cp "$ENV_SOURCE" "$DEPLOY_PATH/.env"

cd "$DEPLOY_PATH"
docker compose up -d mariadb pgvector
APP_IMAGE="${APP_IMAGE:?APP_IMAGE is required}" docker compose pull app
APP_IMAGE="${APP_IMAGE}" docker compose up -d app
docker image prune -f
