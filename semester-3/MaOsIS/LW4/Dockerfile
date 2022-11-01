FROM rastsislau/ostis-example-app:0.6.1

COPY lw1.scs /app/ostis-example-app/kb
COPY lw2.scs /app/ostis-example-app/kb
COPY lw3_1.scs /app/ostis-example-app/kb
COPY lw3_2.scs /app/ostis-example-app/kb

RUN cd /app/ostis-example-app/ostis-web-platform/scripts && \
    ./build_kb.sh
