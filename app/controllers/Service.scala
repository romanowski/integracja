package controllers

import model.{ Sequence, Event }
import org.drools.runtime.StatefulKnowledgeSession
import org.drools.runtime.rule.WorkingMemoryEntryPoint

object Service {

  private val ksession: StatefulKnowledgeSession = RulesProcessor.getSession
  private val eg: WorkingMemoryEntryPoint = RulesProcessor.getEntryPoint

  def processEvents(events: Seq[Seq[Event]]): Unit = {
    for (es <- events) {
      for (e <- es) {
        eg.insert(e)
      }
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
