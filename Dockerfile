FROM maven:3.9.5-eclipse-temurin-17

COPY . /usr/local/source_code
COPY ./docker/mvn-package-run.sh /usr/local/bin/mvn-package-run.sh
ENTRYPOINT ["/usr/local/bin/mvn-package-run.sh"]

