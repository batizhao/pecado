FROM harbor.pecado.com/ideal/alpine-openjdk:11 as builder
WORKDIR application
ARG JAR_FILE=target/pecado-ims-*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract && rm app.jar

FROM harbor.pecado.com/ideal/alpine-openjdk:11
WORKDIR application
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/application/ ./
EXPOSE 5000
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]