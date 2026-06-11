# Board Wave Home Server Deployment

이 디렉터리는 `board_cafe_kiosk_2603_home` 복사본을 홈서버 Docker 환경에서 배포하기 위한 전용 설정입니다.

## 구성

- `app`: Spring Boot 애플리케이션
- `mariadb`: 운영 트랜잭션 + Spring Batch 메타데이터
- `pgvector`: AI 검색용 PostgreSQL + vector extension

## 1. 준비

홈서버에 아래 경로를 준비합니다.

```bash
sudo mkdir -p /srv/data/boardwave/upload
sudo mkdir -p /srv/data/boardwave/mariadb
sudo mkdir -p /srv/data/boardwave/pgvector
sudo chown -R $USER:$USER /srv/data/boardwave
```

## 2. 환경파일 생성

```bash
cd /srv/docker/boardwave/deployment/home-server
cp .env.example .env
```

`.env`에서 최소한 아래 값은 운영용으로 수정합니다.

- `APP_DOMAIN`
- `APP_WEBSOCKET_ALLOWED_ORIGIN_PATTERNS`
- `MARIADB_ROOT_PASSWORD`
- `MARIADB_PASSWORD`
- `POSTGRES_PASSWORD`
- `OPENAI_API_KEY`
- `TOSS_PAYMENTS_SECRET_KEY`
- `TOSS_PAYMENTS_CLIENT_KEY`
- `SPRING_MAIL_USERNAME`
- `SPRING_MAIL_PASSWORD`

## 3. 실행

```bash
docker compose up -d --build
```

확인:

```bash
docker compose ps
docker logs boardwave-app --tail 100
```

## 4. Nginx Proxy Manager 연결

새 Proxy Host를 추가합니다.

- Domain Names: `app.mkserver.cloud`
- Scheme: `http`
- Forward Hostname / IP: `192.168.0.50`
- Forward Port: `.env`의 `APP_HTTP_PORT` 값 (`기본 8085`)
- Websockets Support: `On`
- Block Common Exploits: `Off`로 시작 후 필요하면 조정

SSL:

- Request a new SSL Certificate
- Force SSL: `On`
- HTTP/2 Support: `On`

## 5. 특징

- 기존 Elastic Beanstalk 배포 파일은 유지됩니다.
- 홈서버용 설정은 `deployment/home-server` 아래에서만 관리합니다.
- WebSocket Origin은 `APP_WEBSOCKET_ALLOWED_ORIGIN_PATTERNS` 환경변수로 제어합니다.
- 업로드 경로는 윈도우 경로 대신 `/data/upload`로 통일됩니다.
