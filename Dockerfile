FROM openjdk:11-jdk

ADD backend/target/backend-0.0.1-SNAPSHOT.jar /

ENV JAVA_OPTS=""

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /backend-0.0.1-SNAPSHOT.jar ${0} ${@}" ]

