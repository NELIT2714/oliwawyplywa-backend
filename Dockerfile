#FROM eclipse-temurin:21-jdk AS builder
#WORKDIR /app
#COPY . .
#RUN chmod +x ./mvnw
#RUN ./mvnw clean package -DskipTests

#FROM eclipse-temurin:21-jdk
#WORKDIR /app
#COPY --from=builder /app/target/oliwawyplywa-backend.jar oliwawyplywa-backend.jar
#EXPOSE 4000
#ENTRYPOINT ["java", "-jar", "oliwawyplywa-backend.jar"]

FROM eclipse-temurin:21-jdk
RUN apt-get update && apt-get install -y webp && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY oliwawyplywa-backend.jar oliwawyplywa-backend.jar
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "oliwawyplywa-backend.jar"]