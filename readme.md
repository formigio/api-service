#Commitr Service

Spring Boot JPA Based REST Service

Currently using HSQLDB as the database engine.

##Building
`docker run -v [path/to/repo]:/var/service -w="/var/service" [openjdk image] /var/service/gradlew clean build`

##Running
`java -jar -Dspring.profiles.active=prod commitr-service-0.0.1-SNAPSHOT.jar`

##Development
Local development is pretty straightforwrd, 
**nix*
`./gradlew clean build`
*Windows*
`gradlew clean build`

Local server is also simple, 
**nix*
`./gradlew bootrun`
*Windows*
`gradlew bootrun`

###Idea
**nix*
`./gradlew cleanIdea idea`
*Windows*
`gradlew cleanIdea idea`
###Eclipse/STS
**nix*
`./gradlew cleanEclipse eclipse`
*Windows*
`gradlew cleanEclipse eclipse`

**Note:** I have noticed that if you change the `build.gradle` file you need to refresh your IDE using one of the above commands. You can also manage your classpath settings manually, but using the plugin works pretty well.