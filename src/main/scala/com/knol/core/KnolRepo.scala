package com.knol.core
trait KnolRepo {
  def createKnol(knols: Knols): Option[Int]
  def updateKnol(knols: Knols): Option[Int]
  def deleteKnol(id: Int): Option[Int]
  def getKnol(id: Int): Option[Knols]
  def getListKnol(): Option[List[Knols]]
}
