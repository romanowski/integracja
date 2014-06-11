package controllers

import play.api.data._
import play.api.data.format.Formats._
import play.api.data.Forms._
import model._


object Mappings {

  private val eventMapping = mapping(
    "name" -> of[String],
    "type" -> of[String],
    "timestamp" -> of[Double]
  )(Event.apply)(Event.unapply)


  val sequencesData = Form(mapping(
    "input" -> single("series" -> seq(eventMapping)),
    "output" -> seq(seq(of[String]))
  )(Sequences.apply)(Sequences.unapply))

  val eventsForm = Form(single("series" -> seq(eventMapping)))
}
