package com.knol.core

import java.sql.ResultSet
trait FindKnolx {
  def getSession(id: Int):Option[List[KnolxSession]]
}
