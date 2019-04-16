FROM openjdk:8-jre-alpine

ADD file-scanner.jar /

ENTRYPOINT ["java","-jar", "/file-scanner.jar"]

LABEL taskforce.task.name="file-scanner"
