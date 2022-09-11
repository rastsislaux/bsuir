FROM ubuntu:20.04

MAINTAINER Rastsislau Lipski <rostislav.lipsky@gmail.com>

WORKDIR /app

ENV TZ=Europe/Minsk
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt update
RUN apt install -y git python3 python3-pip sudo curl
RUN git clone https://github.com/SHtress/ostis-example-app.git && \
    cd ostis-example-app && \
    git checkout 0.6.1 && \
    scripts/install_ostis.sh

COPY ./run.sh run.sh

ENTRYPOINT ["sh", "run.sh"]
