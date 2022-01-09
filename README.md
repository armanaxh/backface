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

## Setup jenkins
Jenkins is an open source automation server. It helps automate the parts of software development related to building, testing, and deploying, facilitating continuous integration and continuous delivery.
We can find jenkins Helm Value in jenkins subdirectory. and deploy it on kubernetes. for more information see [here](https://www.jenkins.io/doc/book/installing/kubernetes/).
```bash
kubectl create namespace jenkins
```
Configure Helm
Once Helm is installed and set up properly, add the Jenkins repo as follows:
```bash
$ helm repo add jenkinsci https://charts.jenkins.io
$ helm repo update
```
Apply some raw manifests:
```bash
kubectl apply -f jenkins/jenkins-volume.yaml
kubectl apply -f jenkins/jenkins-sa.yaml
```
Now you can install Jenkins by running the helm install command and passing it the following arguments:
```bash
helm upgrade --install jenkins -n jenkins -f jenkins/values.yaml jenkinsci/jenkins
```
You can find Jenkins configuration in `jenkins/values.yaml`. I use Jcasc for persist configuration and for Infrastructure as code aproche. below I explain why we have a pipeline for each project in the Jcasc. see [here](https://github.com/armanaxh/duckface/blob/main/jenkins/values.yaml#L321).

To access your Jenkins server, you must retrieve the password. You can retrieve your password using either of the two options below.
```bash
kubectl get secret -n jenkins jenkins -o jsonpath={.data.jenkins-admin-username} | base64 --decode
kubectl get secret -n jenkins jenkins -o jsonpath={.data.jenkins-admin-password} | base64 --decode
```
To make Jenkins accessible outside the Kubernetes cluster for test you can use port-forwarding. for production you can enable ingress and set domain and etc.
```bash
kubectl port-forward svc/jenkins 8080:8080 --address 0.0.0.0 -n jenkins
```

## build and deploy custom-images CI/CD

## build and deploy samplewebapp CI/CD

## build and deploy myapp CI/CD





## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License
[MIT](https://choosealicense.com/licenses/mit/)