import java.net.URI

import io.undertow.server.handlers.proxy.LoadBalancingProxyClient
import io.undertow.Undertow
import io.undertow.server.handlers.proxy.ProxyHandler

object HelloWorldServer {
  def main(args: Array[String]): Unit =
    new MyServer(Application(path="/", port = 8085,
      pool = WKA(Seq("http://localhost:8081", "http://localhost:8082")))).start()
}

class MyServer(application: Application) {

  def start(): Unit = {
    val loadBalancer = new LoadBalancingProxyClient()
    application.pool match {
      case ZNode(_) =>
      case WKA(pool) =>
        pool.foreach(s => loadBalancer.addHost(URI.create(s)))
    }
    val reverseProxy: Undertow = Undertow.builder
      .addHttpListener(application.port, application.host)
      .setIoThreads(4)
      .setHandler(ProxyHandler.builder.setProxyClient(loadBalancer)
        .setMaxRequestTime(application.timeout)
        .build)
      .build

    reverseProxy.start()
  }
}