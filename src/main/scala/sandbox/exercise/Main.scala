package sandbox.exercise

object Main extends App {
  import PrintableInstances._
  import PrinterSyntax._
  val cat = Cat("みけ", 222, "red")
  cat.print
}

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val stringPrinter: Printable[String] = new Printable[String] {
    override def format(value: String): String = {
      value
    }
  }

  implicit val intPrinter: Printable[Int] = new Printable[Int] {
    override def format(value: Int): String = {
      value.toString
    }
  }

  implicit val catPrinter: Printable[Cat] = new Printable[Cat] {
    override def format(value: Cat): String = {
      s"${value.name} is a ${value.age} years old ${value.color} cat"
    }
  }
}

final case class Cat(name: String, age: Int, color: String)


object PrinterSyntax {
  implicit class RichPrint[A](value: A) {
    def format(implicit p: Printable[A]): String = {
      p.format(value)
    }

    def print(implicit p: Printable[A]): Unit = {
      println(p.format(value))
    }
  }
}
