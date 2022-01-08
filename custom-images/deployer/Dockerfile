FROM ubuntu:20.04


RUN apt-get update && apt-get install -y --no-install-recommends \
    git \
    wget \
    curl \
    openssh-client \
    ca-certificates

RUN curl -L --output /usr/local/bin/kubectl "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
RUN curl -L --output /tmp/helm.tar.gz https://get.helm.sh/helm-v3.7.2-linux-amd64.tar.gz
RUN tar xf /tmp/helm.tar.gz -C /tmp/ && mv /tmp/linux-amd64/helm /usr/local/bin/ && rm -rf /tmp/helm.tar.gz /tmp/linux-amd64

RUN chmod +x /usr/local/bin/*

