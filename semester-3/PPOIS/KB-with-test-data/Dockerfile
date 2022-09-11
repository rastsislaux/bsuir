FROM rastsislau/ostis-example-app:latest

COPY test1.gwf /app/ostis-example-app/kb/1.gwf

COPY test2.gwf /app/ostis-example-app/kb/2.gwf

COPY test3.gwf /app/ostis-example-app/kb/3.gwf

COPY test4.gwf /app/ostis-example-app/kb/4.gwf

COPY test5.gwf /app/ostis-example-app/kb/5.gwf

RUN cd /app/ostis-example-app/ostis-web-platform/scripts && \
    ./build_kb.sh