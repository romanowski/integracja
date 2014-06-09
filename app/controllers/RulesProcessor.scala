package controllers

import org.drools._
import org.drools.builder._
import org.drools.conf.EventProcessingOption
import org.drools.io.ResourceFactory
import org.drools.runtime._
import org.drools.runtime.conf.ClockTypeOption
import org.drools.runtime.rule.WorkingMemoryEntryPoint
import model.Event
import java.io.File
import java.io.FileWriter
import collection.JavaConversions._

object RulesProcessor {

  private val kbase: KnowledgeBase = createKnowledgeBase("app/controllers/rules.drl")
  private val ksession: StatefulKnowledgeSession = createKnowledgeSession(kbase)
  private val eg: WorkingMemoryEntryPoint = ksession.getWorkingMemoryEntryPoint("Event Generator")
  private val fileWriter: FileWriter = new FileWriter(new File("sequences.txt"))

  def getSession: StatefulKnowledgeSession = {
    ksession
  }

  def getEntryPoint: WorkingMemoryEntryPoint = {
    println(ksession.getWorkingMemoryEntryPoints.map(_.getEntryPointId).mkString(" "))
    eg
  }

  //  def run() {
  //    val e1: Event = new Event(System.currentTimeMillis(), "a", System.currentTimeMillis().toString, 1L, 1L)
  //    val e2: Event = new Event(System.currentTimeMillis(), "b", System.currentTimeMillis().toString, 2L, 2L)
  //    val e3: Event = new Event(System.currentTimeMillis(), "c", System.currentTimeMillis().toString, 3L, 3L)
  //
  //    eg.insert(e1)
  //    eg.insert(e2)
  //    eg.insert(e3)
  //
  //    ksession.fireAllRules
  //  }

  private def createKnowledgeBase(rulesfile: String): KnowledgeBase = {
    val kbuilder: KnowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder
    kbuilder.add(ResourceFactory.newFileResource(new File(rulesfile)), ResourceType.DRL)

    val conf: KnowledgeBaseConfiguration = KnowledgeBaseFactory.newKnowledgeBaseConfiguration
    conf.setOption(EventProcessingOption.STREAM)

    val kbase: KnowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(conf)
    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages)
    kbase
  }

  private def createKnowledgeSession(kbase: KnowledgeBase): StatefulKnowledgeSession = {
    val ksconf: KnowledgeSessionConfiguration = KnowledgeBaseFactory.newKnowledgeSessionConfiguration
    ksconf.setOption(ClockTypeOption.get(ClockType.REALTIME_CLOCK.getId))

    val ksession: StatefulKnowledgeSession = kbase.newStatefulKnowledgeSession(ksconf, null)
    ksession
  }

  def thenResolve(sequence: Array[Event]) {
    val sb: StringBuilder = new StringBuilder("seq:\n")
    for (e <- sequence) {
      import e._
      sb.append(s"$time $eventType $name")
    }
    fileWriter.write(sb.mkString)
  }

}