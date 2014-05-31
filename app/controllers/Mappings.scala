package controllers

import play.api.data._
import play.api.data.format.Formats._
import play.api.data.Forms._
import model._


object Mappings {

  private val sequenceMapping = mapping(
    "id" -> of[Long],
    "order" -> seq(of[String])
  )(Sequence.apply)(Sequence.unapply)

  private val clusterMapping = mapping(
    "sequence" -> sequenceMapping,
    "groups" -> seq(seq(of[Long])))(Cluster.apply)(Cluster.unapply)

  private val eventMapping = mapping(
    "time" -> of[Long],
    "what" -> of[String],
    "data" -> of[String],
    "who" -> of[Long],
    "id" -> of[Long]
  )(Event.apply)(Event.unapply)


  val clustersMapping = single(
    "clusters" -> seq(clusterMapping)
  )
  val sequencesForm = Form(single("sequences" -> seq(sequenceMapping)))

  val eventsForm = Form(single("events" -> seq(seq(eventMapping))))


}
