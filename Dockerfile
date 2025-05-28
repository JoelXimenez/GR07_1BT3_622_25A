FROM tomcat:11.0.6-jdk17

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY target/appTrueque-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copiar server.xml modificado para que tome el puerto 8081
COPY .smarttomcat/TruequeApp/conf/server.xml /usr/local/tomcat/conf/server.xml

EXPOSE 8081

CMD ["catalina.sh", "run"]
