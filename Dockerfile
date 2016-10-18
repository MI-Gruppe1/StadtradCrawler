FROM java:8 

# Install maven
RUN apt-get clean && apt-get update
RUN apt-get install -y maven

WORKDIR /code

# Prepare by downloading dependencies
ADD pom.xml /code/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Adding source, compile and package into a fat jar
ADD src /code/src
RUN ["mvn", "package"]

# Making the service accessible from other containers
#EXPOSE 4567

# This Command will be executed in the Containter
CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/StadtradCrawler-jar-with-dependencies.jar"]