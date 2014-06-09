package controllers

import play.api.data._
import play.api.data.format.Formats._
import play.api.data.Forms._
import model._


object Mappings {

  private val sequenceMapping = mapping(
    "input" -> of[Long],
    "output" -> seq(of[String])
  )(Sequence.apply)(Sequence.unapply)

  private val clusterMapping = mapping(
    "sequence" -> sequenceMapping,
    "groups" -> seq(seq(of[Long])))(Cluster.apply)(Cluster.unapply)

  private val eventMapping = mapping(
    "name" -> of[String],
    "eventType" -> of[String],
    "timestamp" -> of[Double]
  )(Event.apply)(Event.unapply)


  val clustersMapping = single(
    "clusters" -> seq(clusterMapping)
  )
  val sequencesForm = Form(single("sequences" -> seq(sequenceMapping)))

  val eventsForm = Form(single("series" -> seq(eventMapping)))
}
