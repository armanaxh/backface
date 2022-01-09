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
