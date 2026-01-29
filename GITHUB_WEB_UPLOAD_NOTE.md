# GitHub 웹 업로드용 패키지 안내

이 폴더는 **GitHub 웹(Upload files)**에서 폴더 업로드 시 `.`로 시작하는 파일/폴더가 누락되는 문제를 피하려고,
**자바 소스/로직은 그대로 두고** 업로드 편의만 손본 버전입니다.

변경/제거된 것(코드 변경 없음):
- Maven Wrapper(`.mvn/`, `mvnw*`) 제거 → 대신 **로컬 Maven(`mvn`)**으로 실행
- dotfile들은 템플릿 파일로 이름만 변경:
  - `gitignore.template` (원래 `.gitignore`)
  - `gitattributes.template` (원래 `.gitattributes`)
  - `factorypath.template.xml` (원래 `.factorypath`)
  - `env.example` (원래 `.env.example`)

원본 dotfile이 필요하면, GitHub 업로드 후 **파일 이름만 다시 `.gitignore`처럼 바꾸면** 됩니다.
(내용은 템플릿 파일에 그대로 들어있습니다.)
