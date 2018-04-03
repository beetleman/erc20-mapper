FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/erc20-mapper.jar /erc20-mapper/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/erc20-mapper/app.jar"]
