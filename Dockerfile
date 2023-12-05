FROM maven:3.9.5-eclipse-temurin-17

COPY . /usr/local/source_code
COPY ./docker/mvn-package-run.sh /usr/local/source_code/mvn-package-run.sh

ENTRYPOINT ["/bin/bash", "-ce", "cd /usr/local/source_code && sh /usr/local/source_code/mvn-package-run.sh"]
