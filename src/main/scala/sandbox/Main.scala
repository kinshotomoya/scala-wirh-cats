package sandbox


object Main extends App {
  println("Hello " + "Cats!")

  // implicitで定義したインスタンスをimportしている
  // global levelではなく、object内でimportする
  import JsonWriterInstances._
  import JsonSyntax._
  val json: Json = Json.toJson(Person("tomoya", "test@gmail.com"))
  val person = Person("tomoya", "test@gmail.com")
  person toJson


  val personOpt = Some(null)
  println(personOpt.toJson)

}


sealed trait Json
final case class JsObject(get: Map[String, String]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Int) extends Json
case object JsNull extends Json

// type classを定義することで、AにJsonのどんなサブクラスが来ても実装できるようになる
trait JsonWriter[A] {
  def write(value: A): Json
}


final case class Person(name: String, email: String)

// 上で定義したJsonWriterのインスタンスを定義する
//　objectでラップしてあげることで、このオブジェクトをimportすると、implicitが使える
object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] = new JsonWriter[String] {
    override def write(value: String): Json = JsString(value)
  }

  implicit val personWriter: JsonWriter[Person] = new JsonWriter[Person] {
    override def write(value: Person): Json = JsObject(
      Map(
        "name" -> value.name,
        "email" -> value.email
      )
    )
  }

  implicit val nullWriter: JsonWriter[Null] = new JsonWriter[Null] {
    override def write(value: Null): Json = {
      JsNull
    }
  }
}

object Json {
  // implicitで引数を受け取ることで、上で定義したJsonWriterインスタンスを受け取ることができる
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json = {
    w.write(value)
  }
}

object JsonSyntax {
  implicit class RichJson[A](value: A) {
    def toJson(implicit w: JsonWriter[A]) = {
      w.write(value)
    }
  }

  implicit class RichJson2[A](option: Option[A]) {
    def toJson(implicit w: JsonWriter[A]) = {
      option match {
        case Some(aValue) => w.write(aValue)
        case None => JsNull
      }
    }
  }
}
