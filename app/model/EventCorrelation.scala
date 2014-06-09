package model

/**
 * Author: Krzysztof Romanowski
 */
case class EventCorrelation(name: String, eventType: String, frequency: Int, deviation: Double, correlations: Seq[Correlation])
