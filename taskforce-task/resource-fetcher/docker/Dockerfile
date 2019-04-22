FROM openjdk:8-jre-alpine

ADD resource-fetcher.jar /

ENV TASK_EXEC_MODE docker

ENTRYPOINT ["java","-jar", "/resource-fetcher.jar"]

LABEL taskforce.task.name="resource-fetcher"
