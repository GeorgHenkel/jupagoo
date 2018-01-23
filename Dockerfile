FROM airhacks/glassfish
COPY ./target/dadoop.war ${DEPLOYMENT_DIR}
