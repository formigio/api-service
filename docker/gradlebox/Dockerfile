FROM openjdk

# Install Gradle
RUN wget https://services.gradle.org/distributions/gradle-2.10-all.zip
RUN unzip gradle-2.10-all.zip
RUN mv gradle-2.10 /opt/
RUN rm gradle-2.10-all.zip

ENV GRADLE_HOME /opt/gradle-2.10
ENV PATH $PATH:$GRADLE_HOME/bin
