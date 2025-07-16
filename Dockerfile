FROM openjdk:21

COPY build/libs/*.jar /app.jar

RUN echo "Checking /app.jar..." && ls -lh /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
