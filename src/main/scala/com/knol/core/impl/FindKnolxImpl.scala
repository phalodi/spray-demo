package com.knol.core.impl

import com.knol.core.FindKnolx
import com.knol.db.connection.DBConnection
import com.knol.core.KnolxSession
import java.sql._
class FindKnolxImpl extends FindKnolx with DBConnection {

  /**
   * getSession method is take a argument id as Int type and return number of row fetch which
   * have same id in knol and knol_id in knolx
   */
  def getSession(id: Int): Option[List[KnolxSession]] =
    {
      val connection = getConnection()
      connection match {
        case Some(connection) =>
          try {
            val st: Statement = connection.createStatement()
            val sql: String = "select k.id,k.name,x.topic,x.session_date,x.id from knol k "+
            "inner join knolx x where x.knol_id = " + id
            st.execute(sql)
            var knolsessionlist: List[KnolxSession] = List[KnolxSession]()
            val rs: ResultSet = st.executeQuery(sql)
            while (rs.next()) {
              val knolx = KnolxSession(rs.getInt("k.id"), rs.getString("k.name"), rs.getString("x.topic"),rs.getString("x.session_date"), rs.getInt("x.id"))
              knolsessionlist = knolsessionlist :+ knolx;
            }
            if(knolsessionlist.size<1) throw new Exception
            logger.debug("List retrieve")
            Some(knolsessionlist)
          } catch {
            case e: Exception =>
              logger.error("SQL systex error in getting list of knols" , e.printStackTrace())
              none
          } finally {
            connection.close()
          }
        case None =>
          none
      }
    }
}
