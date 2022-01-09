## samplewebapp

I use tomcat for the webserver of the Java Web Application. And I have deployed it on Kubernetes.

I separate Tomcat config to could change without building images.
These pods have one ClusterIP service that distributes requests between replicas.

_If our server use HTTP/2. We should use a headless service and client-side load balancing. because in HTTP/2(GRPC) connection is keepalived and our requests will not be balanced._

And to expose our application, I use Ingress configured on NodePort service and Local TrafficPolicy.
We can use cert-manager for our ingress(domains) certificate generate.

I don't use Loadbalancer because Kubernetes doesn't implement LB type(Because it's Cloud Computing level implementation, we have metallb, but it's not good!), and It's different depending on cloud providers.

CDN -> LBs -> K8s worker nodes (IPVS loadbalancing*) -> Ingress NodePort -> Nginx -> service SVC -> Pods(updated own ipes in endpoints)

*In IPVS load-balancing(Kubernetes CNI use Linux IPVS for in-node loadbalancing) *
if our traffic policy is set on Cluster mode, the load-balancer doesn't balance requests on the node's pod and sometimes (similar to round-robin) send packets to other nodes(with SNAT).
On the other hand, If our traffic policy is set on Local in each nodes loadbalancer just knows about its own pods. And we don't have SNAT and, Client real IP reminds on the packet, and Nginx fill X-forwarded-for with it.
When we set Ingress Traffic Policies on Local, we can fill real-IP in our edge Ngnix. But In another solution, we don't have real-IP. Unless other 7-layer services fill on the HTTP headers before cluster. For example, CDNs fill real-IP on HTTP headers.

For service stability, we have different approaches depending on the service type. In simple services like samplewebapp, we can scale resources. You can find auto-scaler in the Kubernetes subdirectory. It scales service when the load is high. but It's not a good solution sometimes because we should have saved resources. (On AWS, it's ok)

Anti-affinity: Defining a pod anti-affinity is the next step in increasing your application's availability. A pod anti-affinity guarantees the distribution of the pods across different nodes in your Kubernetes cluster. And if some nodes are faced with the problem, your application is running on other ones at the moment. Also, In this way, resources are used relatively between nodes. Some cloud providers give worker nodes with Over Commit Resources. So when you are in the high load better to distribute your replicas. And better for resource-type balances.

RollingUpdate: Rolling updates allow Deployments' update to take place with zero downtime by incrementally updating Pods instances with new ones. I set maxSurge 3 and maxUnavailable depends on application this metrics should change.

ReadinessProbe & LivenessProbe: Both liveness & readiness probes are used to control the health of an application. The failing liveness probe will restart the container, whereas the failing readiness probe will stop our application from serving traffic.
I enable /health in sample application to check health. But in a complex application, dependency should be checked in readiness. For example, DBs, Kafka, ...