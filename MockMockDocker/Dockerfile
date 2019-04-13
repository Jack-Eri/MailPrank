FROM java:8

COPY MockMock.jar /opt/MockMock.jar

EXPOSE 8282
EXPOSE 2525

CMD ["java", "-jar", "/opt/MockMock.jar", "-h", "8282", "-p", "2525"]