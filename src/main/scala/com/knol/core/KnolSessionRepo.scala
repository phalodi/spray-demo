package com.knol.core

trait KnolSessionRepo {
  def createKnolx(knol: KnolSession):Option[Int]
  def updateKnolx(knol: KnolSession): Option[Int]
  def deleteKnolx(id: Int): Option[Int]
  def getKnolx(id: Int): Option[KnolSession]
  def getListKnolx(): Option[List[KnolSession]]
}
