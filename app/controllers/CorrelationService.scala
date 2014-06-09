package controllers

import model.{Correlation, EventCorrelation, Event}
import scala.util.Random

/**
 * Author: Krzysztof Romanowski
 */
object CorrelationService {

  def compute(data: Seq[Event]): Seq[EventCorrelation] = {
    data.sortBy(_.time)

    data.zip(data.tail).groupBy { case (e, _) => (e.name, e.eventType)}
      .map(Function.tupled(computeSingleEvent _))(collection.breakOut)
  }

  def idFor(data: (String, String)) = Random.nextInt(100)

  def computeSingleEvent(forEvent: (String, String), events: Seq[(Event, Event)]): EventCorrelation = {

    val corellations = events.groupBy { case (_, event) => event.name -> event.eventType}.map {
      case (data, events) => singleCorrelation(idFor(data))(events)
    }(collection.breakOut)


    EventCorrelation(forEvent._1, forEvent._2, events.size, 1, corellations)
  }

  def singleCorrelation(id: Int)(events: Seq[(Event, Event)]): Correlation = {
    val diffs = events.map { case (e1, e2) => e2.timeStamp - e1.timeStamp}
    val after = diffs.sum / events.size
    val deviation = math.sqrt(diffs.map(after -).map(x => x * x).sum)

    Correlation(id, after, deviation)
  }

}
