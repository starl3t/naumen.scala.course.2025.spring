import scala.annotation.tailrec
import scala.util.{Random,Try,Success,Failure}

object Exercises {

@@ -24,6 +26,10 @@ object Exercises {

    def findSumFunctional(items: List[Int], sumValue: Int) = {
        (-1, -1)
    def findSumFunctional(items: List[Int], sumValue: Int): (Int, Int) = {
        val i = items.zipWithIndex.combinations(2).filter(l => l(0)._1 + l(1)._1 == sumValue).map(l => (l(0)._2, l(1)._2))
        if (i.hasNext) i.next().swap
        else (-1, -1)
    }


@@ -48,8 +54,22 @@ object Exercises {
        }
    }


    def tailRecRecursion(items: List[Int]): Int = {
        1
        @tailrec
        def innerRec(items: List[Int], index: Int, result: Int): Int = {
            items match {
                case head :: tail =>
                    if (head % 2 == 0) {
                        innerRec(tail, index - 1, head * result +index)
                    } else {
                        innerRec(tail, index - 1, index - head * result)
                    }
                case _ => result
            }
        }
        innerRec(items.reverse, items.length, 1)
    }

    /**
@@ -61,6 +81,16 @@ object Exercises {

    def functionalBinarySearch(items: List[Int], value: Int): Option[Int] = {
        None
        @tailrec
        def binarySearch(left: Int, right: Int): Option[Int] =
            Some((left + right) / 2).filter(_ => left <= right) match {
                case None => None
                case Some(mid) =>
                    if (items(mid) == value) Some(mid)
                    else if (items(mid) > value) binarySearch(left, mid - 1)
                    else binarySearch(mid + 1, right)
            }
        binarySearch(0, items.length - 1)
    }

    /**
@@ -74,10 +104,10 @@ object Exercises {
    def generateNames(namesСount: Int): List[String] = {
        if (namesСount < 0) throw new Throwable("Invalid namesCount")
        Nil
        else List.tabulate(namesСount)(_ => Random.alphanumeric.filter(_.isLetter).take(Random.nextInt(10) + 2).mkString.toLowerCase.capitalize)
    }

}

/**
 * Задание №5
 *
@@ -94,10 +124,8 @@ object Exercises {
 *
 * Изменять методы внутри SimplePhoneService не разрешается.
 */

object SideEffectExercise {
    import Utils._

    class SimpleChangePhoneService(phoneService: SimplePhoneService) extends ChangePhoneService {
        override def changePhone(oldPhone: String, newPhone: String): String = {
            val oldPhoneRecord = phoneService.findPhoneNumber(oldPhone)
@@ -112,13 +140,28 @@ object SideEffectExercise {

    class PhoneServiceSafety(unsafePhoneService: SimplePhoneService) {
        def findPhoneNumberSafe(num: String) = ???
        def findPhoneNumberSafe(num: String): Option[String] = Option(unsafePhoneService.findPhoneNumber(num))

        def addPhoneToBaseSafe(phone: String) = ???
        def addPhoneToBaseSafe(phone: String): Either[String, Unit] = {
            Try(unsafePhoneService.addPhoneToBase(phone)) match {
                case Success(ok) => Right(ok)
                case Failure(exception) => Left(exception.getMessage)
            }
        }

        def deletePhone(phone: String) = ???
        def deletePhone(phone: String) :Option[Unit] = Option(findPhoneNumberSafe(phone).map(unsafePhoneService.deletePhone))
    }

    class ChangePhoneServiceSafe(phoneServiceSafety: PhoneServiceSafety) extends ChangePhoneService {
        override def changePhone(oldPhone: String, newPhone: String): String = ???
        override def changePhone(oldPhone: String, newPhone: String): String = {
            phoneServiceSafety.findPhoneNumberSafe(oldPhone).foreach(phoneServiceSafety.deletePhone)
            phoneServiceSafety.addPhoneToBaseSafe(newPhone) match {
                case Right(_) => "ok"
                case Left(message) => message
            }
        }
    }
}
}
