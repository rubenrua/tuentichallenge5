import scala.collection.immutable.Queue
import scala.collection.mutable.Map
import scala.annotation.tailrec
import scala.io.Source
import scala.io.StdIn._


/** Challenge 3 - Favourite primes
 *  
 * http://stackoverflow.com/questions/15594227/calculating-prime-numbers-in-scala-how-does-this-code-work
 *
 * THX VirtualVM :)
 *
 * To run $ scalac three.scala && scala Three < inputFile.txt
 *
 * @author rubenrua@gmail.com
 */

object Three {
  
    lazy val ps: Stream[Int] = 2 #:: Stream.from(3).filter(i =>
       ps.takeWhile{j => j * j <= i}.forall{ k => i % k > 0});
    
        
    @tailrec
    def getPrimesRec(n: BigInt, inPrimes: List[Int], outPrimes: List[Int]): List[Int] = {
        if (n == 1 || inPrimes.isEmpty) outPrimes
        else {
          if (n % inPrimes.head == 0) getPrimesRec(n / inPrimes.head, inPrimes, inPrimes.head :: outPrimes)
          else getPrimesRec(n, inPrimes.tail, outPrimes)
        }
    }


    def flatCount(list: List[List[(Int, Int)]], p: Int): Int = {
      var cnt = 0
      for (xs <- list)
        for (x <- xs)
          if (p == x._1) cnt += x._2
      cnt
    }

    def getMaxRepeatInSubRanges(min: Int, maxi: Int, primesInRanges: List[(Int,Int,List[(Int, Int)])], primes: List[Int]): (Int, List[Int]) = {
      var listValid: List[List[(Int, Int)]] = Nil
      for (elem <- primesInRanges) {
        if (min <= elem._1 && maxi >= elem._2) {
           listValid = elem._3 :: listValid
        }   
      }

      var max = 0
      var list: List[Int] = Nil
      for (elem <- primes) {
        val count = flatCount(listValid, elem)
        if (count == max) list = elem :: list
        else if(count > max ) {
          max = count
          list = elem :: Nil
        }
      }
      (max, list)
      
    }

    def getRepeatInSubRanges(samosPrimes: List[List[(Int, Int)]], primes: List[Int]): List[(Int, Int)] = {
      var max = 0
      var list: List[(Int, Int)] = Nil
      for (elem <- primes) {
        val count = flatCount(samosPrimes, elem)
        list = (elem, count) :: list
      }
      list
    }
    

    /**
     * Same as list.groupBy(identity).mapValues(_.size).toList
     */
    def groupByIdentityMapValues(list: List[Int]) = {
      val m = Map.empty[Int, Int]
      for (elem <- list) {
        m.getOrElseUpdate(elem, list.count(_ == elem))
      }
      m.toList
    }

    def createPrimesFromFile(file: String, primes: List[Int]): List[List[(Int, Int)]] = {
      Source.fromFile(file).getLines.toList.par.map(x => groupByIdentityMapValues(getPrimesRec(BigInt(x), primes, Nil))).toList
    }


    //TODO Chapuza
    def getSubRanges(list: Queue[(Int,Int)]) = {
    
      var tmp = List[(Int, Int)]()
      var flatSortedDictList = list.flatMap(x => List(x._1, x._2)).sorted.distinct
    
      while(!flatSortedDictList.tail.isEmpty) {
        tmp = (flatSortedDictList.head, flatSortedDictList.tail.head) :: tmp
        flatSortedDictList = flatSortedDictList.tail
      }
      
      tmp    
    }


    def main(args: Array[String]) {
        val primes = ps.take(25).toList
        val samosPrimes = createPrimesFromFile("numbers.txt", primes)

        var inputList = Queue[(Int, Int)]()
        var num = readLong()
        while (num > 0) {
          num = num - 1
          var (a, b) = readf2("{0,number,integer} {1,number,integer}")
          inputList = inputList enqueue (a.asInstanceOf[Long].toInt, b.asInstanceOf[Long].toInt)
        }

        val subranges = getSubRanges(inputList)
        val subRangesWithCount = subranges.par.map(x => (x._1, x._2, {getRepeatInSubRanges(samosPrimes.slice(x._1,  x._2), primes)})).toList

        println(inputList.par.map(x => {
          getMaxRepeatInSubRanges(x._1,  x._2, subRangesWithCount, primes)
        } ).map( (x) => (x._1 + " " + x._2.sorted.mkString(" "))).mkString("\n"))
        
    }
}