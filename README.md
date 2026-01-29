# BGM Recommender

Spring Boot 기반의 매장 환경 조건(공간/인테리어/밀도/목적 + 자동 감지되는 날씨/계절/시간대)에 맞춰
DB 후보 중 **1곡을 추천**하고, 사용자의 "선택(학습)"을 누적 반영하는 데모 시스템입니다.

## 빠른 실행

### 1) 오프라인/망분리에서도 되는 데모 모드

> 외부 날씨 API를 아예 쓰지 않고, H2 메모리 DB로 바로 확인합니다.

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=demo
```

브라우저:
- `http://localhost:8081/`
- H2 콘솔: `http://localhost:8081/h2-console`

### 2) 기본 모드 (날씨 자동 감지 사용)

필수:
- **JDK 21**
- 인터넷(의존성 다운로드 + 날씨 API 호출)

OpenWeather 키를 환경변수로 넣은 뒤 실행:

**Windows (PowerShell)**
```powershell
setx OPENWEATHER_API_KEY "YOUR_KEY"
```

**macOS/Linux**
```bash
export OPENWEATHER_API_KEY="YOUR_KEY"
```

실행:
```bash
mvn spring-boot:run
```

## API

- `POST /api/store/recommend`
- `POST /api/store/feedback`

정적 테스트 UI:
- `GET /bgm.html`

## 설정 포인트

- `bgm.weather.enabled` (기본 `true`)
  - 키가 없거나 외부 호출 실패 시에도 **자동으로 cloudy로 폴백**하도록 구현되어 있습니다.
