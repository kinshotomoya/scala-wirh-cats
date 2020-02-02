package sandbox.cats.show

import java.util.Date

import cats.Show
import cats.instances.int._
import cats.instances.string._
import cats.syntax.show._
import sandbox.cats.show

object Main extends App {
  val showInt: Show[Int] = Show.apply[Int]
  val showString: Show[String] = Show.apply[String]

  showInt.show(123)
  123.show

  showString.show("hello tomoya")
  "hello tomoya".show

  implicit val showDate: Show[Date] = new Show[Date] {
    override def show(t: Date): String = {
      s"${t.getTime}ms since the epoch."
    }
  }

  implicit val dateShow2: Show[Date] =
  // Showには、
    Show.show(t => s"${t.getTime}ms since the epoch.")


  // --------------------------------------------------------------------------
  // cat exercise

  implicit val catShow: Show[Cat] = Show.show(cat => s"${cat.name} is ${cat.age} years old ${cat.color} cat.")
  val showCat: Show[Cat] = Show.apply[Cat]
  val cat = show.Cat("ジュラン", 344, "white and black")
  showCat.show(cat)

}

final case class Cat(name: String, age: Int, color: String)
