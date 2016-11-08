FROM openjdk:8 

# Install maven
RUN apt-get clean && apt-get update
RUN apt-get install -y maven

WORKDIR /code

# Adding Mavendependencies
ADD pom.xml /code/pom.xml

# Adding source folder
ADD src /code/src

# Load all dependencies and create a fat jar
RUN ["mvn", "install"]

# This Command will be executed in the Containter
CMD ["/usr/lib/jvm/java-8-openjdk-amd64/bin/java", "-jar", "target/StadtradCrawler-jar-with-dependencies.jar"]