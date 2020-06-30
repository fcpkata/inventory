FROM openjdk:11-slim-buster
WORKDIR /inventory-app
COPY build/libs/inventory-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
ENTRYPOINT ["java"]
CMD ["-jar", "inventory-0.0.1-SNAPSHOT.jar"]
