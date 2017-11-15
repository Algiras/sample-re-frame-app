FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/exercize.jar /exercize/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/exercize/app.jar"]
