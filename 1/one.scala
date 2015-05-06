import scala.collection.immutable.Queue
import scala.io.StdIn._

/** Challenge 1 - The Buffer
 *
 * http://blog.xkcd.com/2009/09/02/urinal-protocol-vulnerability/
 *
 * @author rubenrua@gmail.com
 */

var inputList = Queue[Long]()
var num = readLong()
while (num > 0) {
  num = num - 1
  inputList = inputList enqueue readLong()
}

println(inputList.map( x => (x + 1) / 2 ).mkString("\n"))



