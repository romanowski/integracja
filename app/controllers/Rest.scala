package controllers

import play.api.mvc.{Action, Controller}


object Rest extends Controller {

  def getEvents = Action {
    implicit request =>
      Mappings.eventsForm.bindFromRequest().fold(
        errors => BadRequest("Bad data"),
        data => {
          println(request.body.asText)
          Service.processEvents(data)
          Ok
        }
      )
  }

  def getSequences = Action {
    implicit request =>
      Mappings.sequencesForm.bindFromRequest().fold(
        errors => BadRequest("Bad data"),
        data => {
          Service.processSequences(data)
          Ok
        }
      )
  }
}
