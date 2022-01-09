# The Discovery of simple CI/CD

Simple Kubernetes deployment with Jenkins! And my first experience with Jenkins.

## Installation and preparation

Use the package manager [pip](https://pip.pypa.io/en/stable/) to install foobar.

### Minikube setup

```bash
$ minikube start --cpus 5 --memory 10240MB --driver docker
```
you can use other cni(flannel, calico) and drivers(kvm2, vmware)

For setup proxy for docker:
```bash
--docker-env http_proxy=http://proxy.example.org:3128 --docker-env https_proxy=http://proxy.example.org:3128 --docker-env no_proxy=*.test.example.com,.example2.com,localhost,127.0.0.1,10.96.0.0/12,192.168.59.0/24,192.168.39.0/24,registry,local,registry.local
```
I need proxy for downloading images and tools

minikube addons:

[Ingress](https://kubernetes.io/docs/concepts/services-networking/ingress/) exposes HTTP and HTTPS routes from outside the cluster to services within the cluster. Traffic routing is controlled by rules defined on the Ingress resource. We use [ingress-nginx](https://github.com/kubernetes/ingress-nginx).

```bash
$ minikube addons enable ingress
```
[Registry](https://docs.docker.com/registry/) stores and lets you distribute Docker images. 
- tightly control where your images are being stored
- fully own your images distribution pipeline
- integrate image storage and distribution tightly into your in-house development workflow

```bash
$ minikube addons enable registry
```


[Metrics Server](https://github.com/kubernetes-sigs/metrics-server) collects resource metrics from Kubelets and exposes them in Kubernetes apiserver through Metrics API for use by Horizontal Pod Autoscaler and Vertical Pod Autoscaler. we need it on one of the scale solutions.
```bash
$ minikube addons enable metrics-server
```
