package controllers

import play.api._
import play.api.mvc._
import model.Event
import model.Sequence

object Application extends Controller {

  def index = Action {
    val e1 = new Event("e1", "t1", System.currentTimeMillis())
    val e2 = new Event("e2", "t2", System.currentTimeMillis())
    val e3 = new Event("e3", "t3", System.currentTimeMillis())
    Service.processEvents(Seq(e1, e2, e3))
    val s1 = new Sequence(1, Seq("t1", "t2", "t3"))
    val s2 = new Sequence(2, Seq("t1", "t3"))
    Service.processSequences(Seq(s1, s2))
    Ok(views.html.index("Your new application is ready."))
  }

}