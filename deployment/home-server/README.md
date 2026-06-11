# Board Wave Home Server Deployment

이 디렉터리는 `board_cafe_kiosk_2603_home` 프로젝트를 홈서버 Docker 환경에 배포하기 위한 단일 운영 경로입니다.

## 구성

- `app`: GitHub Actions가 GHCR에 올린 Spring Boot 이미지
- `mariadb`: 운영 트랜잭션 + Spring Batch 메타데이터
- `pgvector`: AI 검색용 PostgreSQL + vector extension

## 1. 준비

홈서버에 아래 경로를 준비합니다.

```bash
sudo mkdir -p /srv/data/boardwave/upload
sudo mkdir -p /srv/data/boardwave/mariadb
sudo mkdir -p /srv/data/boardwave/pgvector
sudo mkdir -p /srv/docker/boardwave/deployment/home-server
sudo chown -R $USER:$USER /srv/data/boardwave /srv/docker/boardwave
```

레포를 홈서버에 배치합니다.

```bash
cd /srv/docker/boardwave
git clone https://github.com/ppaapp220022-blip/board_cafe_kiosk_2603_home.git .
```

## 2. 환경파일 생성

```bash
cd /srv/docker/boardwave/deployment/home-server
cp .env.example .env
```

`.env`에서 최소한 아래 값은 운영용으로 수정합니다.

- `APP_IMAGE`
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
- `PORTFOLIO_SUPER_KEY_ID`
- `PORTFOLIO_SUPER_KEY_OTP`
- `PORTFOLIO_SUPER_KEY_TEMP_PASSWD`

## 3. GitHub Container Registry 로그인

배포 서버에서 GHCR 이미지를 받을 수 있어야 합니다.

```bash
echo '<GHCR_READ_TOKEN>' | docker login ghcr.io -u '<GHCR_USERNAME>' --password-stdin
```

권장:

- `GHCR_USERNAME`: GitHub 사용자명
- `GHCR_READ_TOKEN`: `read:packages` 권한이 있는 토큰

## 4. 최초 실행

```bash
docker compose pull app
docker compose up -d
```

확인:

```bash
docker compose ps
docker logs boardwave-app --tail 100
```

## 5. GitHub Actions 기반 CI/CD

이 저장소는 두 개의 워크플로우를 사용합니다.

- `ci.yml`: 테스트와 `bootJar` 검증
- `deploy-home-server.yml`: GHCR 이미지 푸시 후 홈서버 self-hosted runner 배포

필요한 준비:

- GitHub 저장소의 self-hosted runner를 홈서버에 설치
- runner 라벨에 `boardwave-home` 추가
- 홈서버에 실제 운영 환경파일 배치

운영 환경파일 경로:

```bash
/srv/config/boardwave/home-server.env
```

배포 경로:

```bash
/srv/docker/boardwave/deployment/home-server
```

배포 흐름:

1. `main` 푸시
2. GitHub Actions가 테스트 수행
3. Docker 이미지를 `ghcr.io/ppaapp220022-blip/board_cafe_kiosk_2603_home:main` 으로 푸시
4. 홈서버 self-hosted runner가 job 실행
5. `/srv/config/boardwave/home-server.env`를 `.env`로 복사
6. `docker compose pull app && docker compose up -d app` 실행

### self-hosted runner 설치 메모

GitHub 저장소에서:

- `Settings`
- `Actions`
- `Runners`
- `New self-hosted runner`

를 열고 Linux x64 runner를 홈서버에 설치합니다.

권장 라벨:

```text
boardwave-home
```

## 6. Nginx Proxy Manager 연결

새 Proxy Host를 추가합니다.

- Domain Names: `boardwave.mkserver.cloud`
- Scheme: `http`
- Forward Hostname / IP: 홈서버 내부 IP
- Forward Port: `.env`의 `APP_HTTP_PORT` 값 (`기본 8085`)
- Websockets Support: `On`
- Block Common Exploits: `Off`로 시작 후 필요하면 조정

SSL:

- Request a new SSL Certificate
- Force SSL: `On`
- HTTP/2 Support: `On`

## 7. 운영 메모

- 홈서버용 설정은 `deployment/home-server` 아래에서만 관리합니다.
- WebSocket Origin은 `APP_WEBSOCKET_ALLOWED_ORIGIN_PATTERNS` 환경변수로 제어합니다.
- 업로드 경로는 `/data/upload`로 통일됩니다.
- 수동 재배포가 필요하면 `docker compose pull app && docker compose up -d app` 를 실행하면 됩니다.
