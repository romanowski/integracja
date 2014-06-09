package controllers

import model.Sequence

object RulesCreator {

  private val FIRST_CONDITION = "$e%s : Event( eventType matches \"%s\" ) from entry-point \"Event Generator\"";
  private val NEXT_CONDITION = "\n\t$e%s : Event( eventType matches \"%s\", this after[0s, 10s] $e%s ) from entry-point \"Event Generator\"";
  private val RULE_TEMPLATE =
    """rule \"Rule %s\"
when
	%s
then
	RulesProcessor.thenResolve([%s]);
end
\n""";

  def createRule(sequence: Sequence): String = {
    var j = 0

    val when = new StringBuilder
    val slist = new StringBuilder

    for (elem <- sequence.order) {
      var econd: String = null
      var s: String = null

      if (j == 0) {

        econd = String.format(FIRST_CONDITION, j.toString, elem)
        s = String.format("$e%s", j.toString)

      } else {

        econd = String.format(NEXT_CONDITION, j.toString, elem, (j - 1).toString)
        s = String.format(", $e%s", j.toString)

      }

      when.append(econd)
      slist.append(s)
      j += 1
    }

    val rule = String.format(RULE_TEMPLATE, sequence.id.toString, when.mkString, slist.mkString)
    rule
  }

}