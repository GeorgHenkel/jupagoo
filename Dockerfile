FROM airhacks/glassfish

COPY ./target/jupagoo.war ${DEPLOYMENT_DIR}
COPY ./src/main/lib/hsqldb-2.4.jar ${GLASSFISH_HOME}/domains/domain1/lib/ext

ADD hsqldb-2.3.2.jar /opt/app/extlib/hsqldb-2.3.2.jar

ADD bin/configure-glassfish.sh /opt/app/bin/configure-glassfish.sh
RUN /opt/app/bin/initialize-glassfish.sh