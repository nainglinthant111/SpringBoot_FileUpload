ARG MVN_VERSION=3.8.3
ARG JDK_VERSION=17

FROM maven:${MVN_VERSION}-openjdk-${JDK_VERSION}-slim as MAVEN_TOOL_CHAIN_CACHE
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:tree
COPY ./pom.xml /tmp/
COPY ./src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install

FROM gcr.io/distroless/java17:nonroot

USER nonroot:nonroot

COPY --from=MAVEN_TOOL_CHAIN_CACHE --chown=nonroot:nonroot /tmp/target/MonGo-0.0.1-SNAPSHOT.jar /MonGo-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/MonGo-0.0.1-SNAPSHOT.jar"]

