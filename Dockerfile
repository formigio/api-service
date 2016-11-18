FROM openjdk 

EXPOSE 8080
ADD ./build/libs/formigio-service-0.0.4-SNAPSHOT.jar commitr.jar
RUN sh -c 'touch /commitr.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=local","-jar","/commitr.jar"]
