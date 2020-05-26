

object Main extends App {
  get("https://yandex.ru")

  def get(url: String): String = {
    println(s"Make request $url")
    val source = scala.io.Source.fromURL(url)
    val data = source.mkString
    source.close()
    data
  }
}