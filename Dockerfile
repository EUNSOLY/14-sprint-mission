# 기본 베이스 이미지
FROM amazoncorretto:17
# 도커 내 작업 디렉토리 설정
WORKDIR /app
# 현재 소스코드 전부 복사 (불필요한 파일은 .dockerignore로 제외)
COPY . .

# 이미지 만들 때 build 실행(실행 된 후 이미지 내 build 파일 포함)
RUN ./gradlew build

# 빌드 시 받을 매개변수 (프로젝트 정보 - build.gradle이랑 동일해야함)
ARG PROJECT_NAME=discodeit
ARG PROJECT_VERSION=1.2-M8
ARG JVM_OPTS=""

# 실행 시 사용 될 매개 변수
ENV PROJECT_NAME=$PROJECT_NAME
ENV PROJECT_VERSION=$PROJECT_VERSION
ENV JVM_OPTS=$JVM_OPTS

# 포트를 쓴다는 메타 데이터 (외부에 노출안됨 실행 시 연결해야함)
EXPOSE 80

# 컨테이너 실행 시
ENTRYPOINT ["sh", "-c", "exec java $JVM_OPTS -jar build/libs/$PROJECT_NAME-$PROJECT_VERSION.jar"]
