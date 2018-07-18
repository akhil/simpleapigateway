case class Application(path: String,
                       host: String = "localhost",
                       port: Int = 8080,
                       timeout: Int = 60,
                       pool: ServerPool
                   )

sealed trait ServerPool {
  def pool: Any
}

case class ZNode(pool: String) extends ServerPool

case class WKA(pool: Seq[String])  extends ServerPool