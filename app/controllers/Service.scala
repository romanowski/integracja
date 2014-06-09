package controllers

import model.{ Sequence, Event }
import org.drools.runtime.StatefulKnowledgeSession
import org.drools.runtime.rule.WorkingMemoryEntryPoint

object Service {
  
  private val events : Seq[Event] = List.empty

  def processEvents(events: Seq[Event]): Unit = {
    this.events.++:(events)
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
    for(e <- this.events) {
      eg.insert(e)
    }
    ksession.fireAllRules()
  }

}
