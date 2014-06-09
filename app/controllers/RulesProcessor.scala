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
import java.util.List
import java.util.Arrays

object RulesProcessor {


  val baseRule =
    """
      |package controllers
      |
      |import model.Event
      |import controllers.RulesProcessor
      |import java.util.Arrays
      |
      |declare Event
      |@role( event )
      |@timestamp( time )
      |@expires( 60s )
      |end
      |
    """.stripMargin

  private var rulesFile = File.createTempFile("rules", "drl")

  private lazy val kbase: KnowledgeBase = createKnowledgeBase(rulesFile.getAbsolutePath)
  private lazy val ksession: StatefulKnowledgeSession = createKnowledgeSession(kbase)
  private lazy val eg: WorkingMemoryEntryPoint = ksession.getWorkingMemoryEntryPoint("Event Generator")
  private lazy val ruleWriter = new FileWriter(rulesFile)

  def getSession: StatefulKnowledgeSession = {
    ksession
  }

  def getEntryPoint: WorkingMemoryEntryPoint = {
    println(ksession.getWorkingMemoryEntryPoints.map(_.getEntryPointId).mkString(" "))
    eg
  }

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

  def writeRule(rule: String) {
    ruleWriter.write(baseRule + rule)
    ruleWriter.flush()
  }

  def thenResolve(sequence: List[Event]) {
    val sb: StringBuilder = new StringBuilder("seq:\n")
    for (e <- sequence) {
      import e._
      sb.append(s"$time $eventType $name\n")
    }
  }

}