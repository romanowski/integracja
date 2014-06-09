package controllers

import model.{ Sequence, Event }
import org.drools.runtime.StatefulKnowledgeSession
import org.drools.runtime.rule.WorkingMemoryEntryPoint

object Service {

  private lazy val ksession: StatefulKnowledgeSession = RulesProcessor.getSession
  private lazy val eg: WorkingMemoryEntryPoint = RulesProcessor.getEntryPoint

  def processEvents(events: Seq[Event]): Unit = {
    for (e <- events) {
        eg.insert(e)
    }
    ksession.fireAllRules()
    println(events)
  }

  def processSequences(sequences: Seq[Sequence]): Unit = {
    for (s <- sequences) {
      println(s)
    }
    println(sequences)
  }

}
