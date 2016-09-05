#Commitr Service

Spring Boot JPA Based REST Service

Currently using HSQLDB as the database engine.

##Development
Local development is pretty straightforwrd, `./gradlew clean build`

Local server is also simple, `./gradlew bootrun`

###Idea
`./gradlew cleanIdea idea`
###Eclipse/STS
`./gradlew cleanEclipse eclipse`

**Note:** I have noticed that if you change the `build.gradle` file you need to refresh your IDE using one of the above commands. You can also manage your classpath settings manually, but using the plugin works pretty well too.