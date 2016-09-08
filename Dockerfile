FROM openjdk 

EXPOSE 8080
ADD ./build/libs/commitr-service-0.0.1-SNAPSHOT.jar commitr.jar
RUN sh -c 'touch /commitr.jar'
ENTRYPOINT ["java","-Dspring.profiles.active=prod","-jar","/commitr.jar"]
