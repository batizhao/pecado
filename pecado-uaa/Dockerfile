FROM harbor.pecado.com/ideal/alpine-openjdk:11 as builder
ARG JAR_FILE=target/pecado-uaa.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM harbor.pecado.com/ideal/alpine-openjdk:11
COPY --from=builder dependencies/ ./
COPY --from=builder snapshot-dependencies/ ./
RUN true
COPY --from=builder spring-boot-loader/ ./
COPY --from=builder application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]