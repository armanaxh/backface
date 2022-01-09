FROM centos

RUN mkdir /opt/tomcat/

WORKDIR /opt/tomcat
RUN curl -O https://downloads.apache.org/tomcat/tomcat-10/v10.0.14/bin/apache-tomcat-10.0.14.tar.gz
RUN tar xvfz apache*.tar.gz
RUN rm apache-tomcat-10.0.14.tar.gz # review
RUN mv apache-tomcat-10.0.14/* /opt/tomcat/.
RUN yum -y install java
RUN java -version  # TODO cleaning after update and install

WORKDIR /opt/tomcat/webapps
RUN curl -O -L https://tomcat.apache.org/tomcat-8.0-doc/appdev/sample/sample.war
RUN rm -R ROOT && mv sample.war ROOT.war

EXPOSE 8080

CMD ["/opt/tomcat/bin/catalina.sh", "run"]
