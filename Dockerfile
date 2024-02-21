FROM openjdk:17-jdk-slim
COPY ./target/FileSystem-0.0.1.jar /opt/service.jar
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://database:5430/file
ENV POSTGRES_USER=file
ENV POSTGRES_PASSWORD=file
EXPOSE 8080
CMD java -jar /opt/service.jar