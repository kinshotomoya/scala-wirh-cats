package sandbox.exercise

object Main extends App {

}


trait Printable[A] {
  def format[A](value: A): String
}

object PrintableInstances {
  implicit val 
}