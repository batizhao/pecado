FROM harbor.pecado.com/ideal/alpine-openjdk:11 as builder
WORKDIR application
ARG JAR_FILE=target/pecado-dp.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar list
RUN java -Djarmode=layertools -jar app.jar extract

FROM harbor.pecado.com/ideal/alpine-openjdk:11
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]