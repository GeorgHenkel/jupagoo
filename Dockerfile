FROM georghenkel/openliberty-javaee8-derbydb

COPY ./target/jupagoo.war ${DEPLOYMENT_DIR}