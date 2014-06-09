package model

case class Event(name: String, eventType: String, timeStamp: Double) {
  def time = timeStamp.toLong
}


