import io.undertow.server.{HttpHandler, HttpServerExchange}

class AuthHandler(next: HttpHandler) extends HttpHandler {

  @throws [Exception]
  override def handleRequest(req: HttpServerExchange): Unit =
    next handleRequest req

}
