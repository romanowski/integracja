package controllers

import play.api.mvc.{Action, Controller}


object Rest extends Controller {

  def computeCorrelations = Action {
    implicit request =>
      Mappings.eventsForm.bindFromRequest().fold(
        errors => BadRequest("Bad data"),
        data => {
          Service.computeCorrelations(data)
          Ok
        }
      )
  }


  def processEvents = Action {
    implicit request =>
      Mappings.eventsForm.bindFromRequest().fold(
        errors => BadRequest("Bad data"),
        data => {
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
