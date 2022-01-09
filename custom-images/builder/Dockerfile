FROM docker:stable-dind


RUN apk add --no-cache tzdata openjdk8 maven && rm -vrf /var/cache/apk/*
RUN cp /usr/share/zoneinfo/Asia/Tehran /etc/localtime && \
    echo "Asia/Tehran" >  /etc/timezone && \
