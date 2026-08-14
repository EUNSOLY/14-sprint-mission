# 14-sprint-mission-4

## 🎯 학습 목표

- [x] Controller 레이어에 대한 이해 및 웹 API 구현
- [x] Postman 사용방식

## 기본 요구사항

- [x] DiscodeitApplication의 테스트 로직은 삭제
- [x] 지금까지 구현한 서비스 로직을 활용해 웹 API를 구현 이때, @RequestMapping만을 사용
- [x] 웹 API의 예외를 전역으로 처리하세요.
- [x] Postman을 활용해 컨트롤러를 테스트 및 Postman API 테스트 결과를 다음과 같이 export하여 PR에 첨부

## 웹 API 요구사항

### 👤 사용자 관리

- [x] 사용자 등록 / 수정 / 삭제
- [x] 전체 사용자 조회
- [x] 사용자 온라인 상태 업데이트

### 🔐 권한 관리

- [x] 사용자 로그인

### 📢 채널 관리

- [x] 공개 / 비공개 채널 생성
- [x] 공개 채널 정보 수정
- [x] 채널 삭제
- [x] 사용자가 조회 가능한 채널 목록 조회

### 💬 메시지 관리

- [x] 메시지 생성 / 수정 / 삭제
- [x] 특정 채널의 메시지 목록 조회

### 📬 메시지 수신 정보 관리

- [x] 채널별 메시지 수신 정보 생성 / 수정
- [x] 사용자별 메시지 수신 정보 조회

### 📁 바이너리 파일 관리

- [x] 바이너리 파일 단건 / 다건 조회

## 심화 요구사항

### 📦 정적 리소스 서빙

- [x] 사용자 목록 조회 API를 정적 리소스 화면과 연동
- [x] BinaryContent 파일 조회 API 구현
- [x] 제공된 정적 리소스를 활용하여 사용자 목록 화면 서빙

### 🤖 생성형 AI 활용

- [x] 생성형 AI(ChatGPT, Claude 등)를 활용하여 사용자 목록 화면 구현
- [x] 제공된 이미지와 유사한 UI를 생성하고 정적 리소스로 서빙

<br/>

----

# 14-sprint-mission-3

## 🎯 학습 목표

- [x] Spring Boot 마이그레이션 및 IoC Container / DI / Bean 이해
- [x] Lombok, DTO를 활용한 계층 리팩토링
- [x] 신규 도메인 모델링 및 Service 계층 고도화

## 기본 요구사항

- [x] Spring Initializr로 프로젝트 생성 (Gradle-Groovy, Java 17, Boot 3.4.0, `com.sprint.mission`/`discodeit`, Jar,
  Lombok/Spring Web) 후 기존 프로젝트에 병합
- [x] `application.properties` → `.yaml` 전환, `DiscodeitApplication` 실행 확인
- [x] `File*Repository`, `Basic*Service`를 Bean으로 등록
- [x] `JavaApplication`의 테스트 코드를 `DiscodeitApplication`으로 이관, Spring Context 기반 초기화로 교체
- [x] IoC Container / DI / Bean 개념 정리 → PR에 첨부
- [x] Lombok 적용 (`@Getter`, `@RequiredArgsConstructor`)

## 추가 기능 요구사항

- [x] 시간 필드 타입을 `Instant`로 통일
- [x] 신규 도메인 추가: `ReadStatus`(채널별 마지막 읽음 시간), `UserStatus`(마지막 접속 시간, 5분 이내 온라인 판단 메소드), `BinaryContent`(불변, updatedAt
  없음) + 각 Repository 인터페이스 선언
- [x] 모든 Service 파라미터를 DTO로 그룹화, Service 간 직접 의존 대신 Repository 직접 주입
- [x] `UserService`: 프로필 이미지 선택 등록, username/email 중복 검증, UserStatus 동시 생성, 조회 시 온라인 상태 포함·비밀번호 제외, 삭제 시 연관 도메인 함께 삭제
- [x] `AuthService`: username/password 기반 일치하는 유저가 있는지 검증 서비스 생성
- [x] `ChannelService`: PUBLIC/PRIVATE 생성 분리, 조회 시 최근 메시지 시간·참여자 정보 포함(`findAllByUserId`), PRIVATE 수정 불가, 삭제 시 연관 도메인 함께
  삭제
- [x] `MessageService`: 다중 첨부파일 등록, `findAllByChannelId`, 삭제 시 첨부파일 함께 삭제
- [x] `ReadStatusService`, `UserStatusService`, `BinaryContentService`: CRUD 구현/고도화 (존재·중복 검증 포함)
- [x] 신규 도메인(`ReadStatus`/`UserStatus`/`BinaryContent`)의 JCF/File Repository 구현체 작성

<br/>

----

# 14-sprint-mission-2

## 🎯 학습 목표

- [ ] **Git & GitHub**를 활용한 프로젝트 버전 관리
- [ ] **채팅 서비스 도메인 모델** 설계 및 Java 구현
- [ ] **인터페이스** 설계와 구현체 분리 구현
- [ ] **싱글톤 패턴(Singleton Pattern)** 구현 및 이해
- [ ] **Java Collections Framework (JCF)** 데이터 C/U/D (생성·수정·삭제)
- [ ] **Stream API**를 활용한 JCF 데이터 조회 및 가공

<br/>

> **🔥 [심화 목표]**
> - [ ] 모듈 간 의존 관계 이해 및 **팩토리 패턴(Factory Pattern)**을 활용한 의존성 관리

# 1차

## 기본 요구사항

### 프로젝트 초기화

- [x] IntelliJ를 통해 다음의 조건으로 Java 프로젝트를 생성합니다.
    - [x] IntelliJ에서 제공하는 프로젝트 템플릿 중 Java를 선택합니다.
    - [x] 프로젝트의 경로는 스프린트 미션 리포지토리의 경로와 같게 설정합니다.
        - 예를 들어 스프린트 미션 리포지토리의 경로가 `/some/path/1-sprint-mission` 이라면:
            - Name은 `1-sprint-mission`
            - Location은 `/some/path` 으로 설정합니다.
    - [x] `Create Git Repository` 옵션은 체크하지 않습니다.
    - [x] Build system은 Gradle을 사용합니다. Gradle DSL은 Groovy를 사용합니다.
    - [x] JDK 17을 선택합니다.
    - [x] GroupId는 `com.sprint.mission`로 설정합니다.
    - [x] ArtifactId는 수정하지 않습니다.
    - [x] `.gitignore`에 IntelliJ와 관련된 파일이 형상관리 되지 않도록 `.idea` 디렉토리를 추가합니다.

      ```
      ...
      .idea
      ...
      ```

### 도메인 모델링

- [x] 디스코드 서비스를 활용해보면서 각 도메인 모델에 필요한 정보를 도출하고, Java Class로 구현하세요.
    - [x] 패키지명: `com.sprint.mission.discodeit.entity`
    - [x] 도메인 모델 정의
        - [x] 공통
            - [x] `id`: 객체를 식별하기 위한 id로 UUID 타입으로 선언합니다.
            - [x] `createdAt`, `updatedAt`: 각각 객체의 생성, 수정 시간을 유닉스 타임스탬프로 나타내기 위한 필드로 Long 타입으로 선언합니다.
        - [x] User
        - [x] Channel
        - [x] Message
    - [x] 생성자
        - [x] `id`는 생성자에서 초기화하세요.
        - [x] `createdAt`는 생성자에서 초기화하세요.
        - [x] `id`, `createdAt`, `updatedAt`을 제외한 필드는 생성자의 파라미터를 통해 초기화하세요.
    - [x] 메소드
        - [x] 각 필드를 반환하는 `Getter` 함수를
          정의하세요.[JavaApplication.java](src/main/java/com/sprint/mission/JavaApplication.java)
        - [x] 필드를 수정하는 `update` 함수를 정의하세요.

### 서비스 설계 및 구현

- [x] 도메인 모델 별 CRUD(생성, 읽기, 모두 읽기, 수정, 삭제) 기능을 인터페이스로 선언하세요.
    - [x] 인터페이스 패키지명: `com.sprint.mission.discodeit.service`
    - [x] 인터페이스 네이밍 규칙: `[도메인 모델 이름]Service`
- [x] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
    - [x] 클래스 패키지명: `com.sprint.mission.discodeit.service.jcf`
    - [x] 클래스 네이밍 규칙: `JCF[인터페이스 이름]`
    - [x] Java Collections Framework를 활용하여 데이터를 저장할 수 있는 필드(`data`)를 `final`로 선언하고 생성자에서 초기화하세요.
    - [x] `data` 필드를 활용해 생성, 조회, 수정, 삭제하는 메소드를 구현하세요.

### 메인 클래스 구현

- [x] 메인 메소드가 선언된 `JavaApplication` 클래스를 선언하고, 도메인 별 서비스 구현체를 테스트해보세요.
    - [x] 등록
    - [x] 조회(단건, 다건)
    - [x] 수정
    - [x] 수정된 데이터 조회
    - [x] 삭제
    - [x] 조회를 통해 삭제되었는지 확인

## 심화 요구 사항

### 서비스 간 의존성 주입

- [x] 도메인 모델 간 관계를 고려해서 검증하는 로직을 추가하고, 테스트해보세요.
    - 힌트: Message를 생성할 때 연관된 도메인 모델 데이터 확인하기

----

# 2차

## 기본 요구사항

### 1. File IO를 통한 데이터 영속화

- [x] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
    - [x] 클래스 패키지명: `com.sprint.mission.discodeit.service.file`
    - [x] 클래스 네이밍 규칙: `File[인터페이스 이름]`
    - [x] JCF 대신 FileIO와 객체 직렬화를 활용해 메소드를 구현하세요.
- [x] 객체 직렬화/역직렬화 가이드
    - [x] Application에서 서비스 구현체를 `File*Service`로 바꾸어 테스트해보세요.

### 2. 서비스 구현체 분석

- [x] `JCF*Service` 구현체와 `File*Service` 구현체를 비교하여 공통점과 차이점을 발견해보세요.
    - [x] "비즈니스 로직"과 관련된 코드를 식별해보세요.
    - [x] "저장 로직"과 관련된 코드를 식별해보세요.

### 3. 레포지토리 설계 및 구현

- [x] "저장 로직"과 관련된 기능을 도메인 모델 별 인터페이스로 선언하세요.
    - [x] 인터페이스 패키지명: `com.sprint.mission.discodeit.repository`
    - [x] 인터페이스 네이밍 규칙: `[도메인 모델 이름]Repository`
- [x] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요. (JCF 버전)
    - [x] 클래스 패키지명: `com.sprint.mission.discodeit.repository.jcf`
    - [x] 클래스 네이밍 규칙: `JCF[인터페이스 이름]`
    - [x] 기존에 구현한 `JCF*Service` 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.
- [x] 다음의 조건을 만족하는 레포지토리 인터페이스의 구현체를 작성하세요. (File 버전)
    - [x] 클래스 패키지명: `com.sprint.mission.discodeit.repository.file`
    - [x] 클래스 네이밍 규칙: `File[인터페이스 이름]`
    - [x] 기존에 구현한 `File*Service` 구현체의 "저장 로직"과 관련된 코드를 참고하여 구현하세요.

---

## 심화 요구 사항

### 4. 관심사 분리를 통한 레이어 간 의존성 주입

- [x] 다음의 조건을 만족하는 서비스 인터페이스의 구현체를 작성하세요.
    - [x] 클래스 패키지명: `com.sprint.mission.discodeit.service.basic`
    - [x] 클래스 네이밍 규칙: `Basic[인터페이스 이름]`
    - [x] 기존에 구현한 서비스 구현체의 "비즈니스 로직"과 관련된 코드를 참고하여 구현하세요.
    - [x] 필요한 Repository 인터페이스를 필드로 선언하고 생성자를 통해 초기화하세요.
    - [x] "저장 로직"은 Repository 인터페이스 필드를 활용하세요. (직접 구현하지 마세요.)
- [x] `Basic*Service` 구현체를 활용하여 테스트해보세요.