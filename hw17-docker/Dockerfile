FROM bellsoft/liberica-openjre-alpine-musl:11
COPY /target/books-v1.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
