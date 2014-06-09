package controllers

import model.{Correlation, EventCorrelation, Sequence, Event}
import org.drools.runtime.StatefulKnowledgeSession
import org.drools.runtime.rule.WorkingMemoryEntryPoint
import play.api.libs.json.Json
import play.api.libs.ws.WS

object Service {

  private var events: Seq[Event] = List.empty

  def computeCorrelations(events: Seq[Event]): Unit = {
    val data = CorrelationService.compute(events)
    val json = postReadyData(data)
    println(s"processed: ${System.nanoTime()}")

    import play.api.Play.current

    Some("http://immense-refuge-2812.herokuapp.com/results").map(WS.url).map {
      case req =>
        import scala.concurrent.ExecutionContext.Implicits.global
        val future = req.post(json)
        future.onFailure {
          case e => e.printStackTrace()
        }
        future.onSuccess {
          case response =>
            import response._
            println(s"Got status: $status")
        }
    }

  }

  def processEvents(events: Seq[Event]): Unit = {
    this.events = this.events.++(events)
    println(events)
  }

  def processSequences(sequences: Seq[Sequence]): Unit = {
    for (s <- sequences) {
      val rule = RulesCreator.createRule(s)
      RulesProcessor.writeRule(rule)
    }
    fireUpRules
    println(sequences)
  }

  private def fireUpRules {
    val ksession: StatefulKnowledgeSession = RulesProcessor.getSession
    val eg: WorkingMemoryEntryPoint = RulesProcessor.getEntryPoint
    for (e <- this.events) {
      eg.insert(e)
    }
    ksession.fireAllRules()
  }

  def postReadyData(eventCorrelations: Seq[EventCorrelation]) = {
    def singleEvent(ec: EventCorrelation) = Json.obj(
      "name" -> ec.name,
      "type" -> ec.eventType,
      "frequency" -> ec.frequency,
      "deviaiton" -> ec.deviation,
      "correlation" -> ec.correlations.map(singleCorrelation))

    def singleCorrelation(correlation: Correlation) = Json.obj(
      "id" -> correlation.id,
      "after" -> correlation.after,
      "deviation" -> correlation.deviation)

    Json.obj(
      "component" -> "romanowski",
      "events" -> eventCorrelations.map(singleEvent))
  }

}
