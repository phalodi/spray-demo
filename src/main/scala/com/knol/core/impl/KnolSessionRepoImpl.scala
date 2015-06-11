package com.knol.core.impl

import java.sql._
import java.sql.Connection
import com.knol.core.KnolSession
import com.knol.core.KnolSessionRepo
import com.knol.core.Knols
import com.knol.db.connection.DBConnection
import com.knol.core.FindKnolx

class KnolSessionRepoImpl extends KnolSessionRepo with DBConnection {
  var sessionkey: Int = 0
  /**
   * creatKnolx function take a argument of KnlSession type object and insert in knolx table and 
   * return last auto generated primary key
   */
  def createKnolx(knolx: KnolSession): Option[Int] = {
    val con: Option[Connection] = getConnection()
    con match {
      case Some(con) =>
        try {
          val sql: String = "insert into knolx(topic,session_date,knol_id) values('" + knolx.topic + "','" + knolx.date + "'," + knolx.knolid + ");"
          val stmt: PreparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
          val num = stmt.executeUpdate();
          val rs: ResultSet = stmt.getGeneratedKeys();
          while (rs.next()) {
            sessionkey = rs.getInt(1)
          }
          logger.debug("Created Id:" + sessionkey)
          Some(sessionkey)
        } catch {
          case ex: Exception =>
            logger.error("SQL syntax error in insert", ex.printStackTrace())
            none
        } finally {
          con.close()
        }
      case None =>
        none
    }
  }

  /**
   * updateKnolx method take a argument of KnolSession type and update rows in knolx corresponding to id
   * and return number of row affected as Int
   */
  def updateKnolx(knolx: KnolSession): Option[Int] = {
    val con: Option[Connection] = getConnection()
    con match {
      case Some(con) =>
        try {

          val st: Statement = con.createStatement()

          val sql = "update knolx set topic = '" + knolx.topic + "', session_date='" + knolx.date + "',knol_id='" + knolx.knolid + "' where id=" + knolx.id

          val updaterows = st.executeUpdate(sql)
          logger.debug("update row successfully")
          Some(updaterows)
        } catch {
          case ex: Exception =>
            logger.error("SQL syntax error in update", ex.printStackTrace())
            none
        } finally {
          con.close()
        }
      case None =>
        none
    }
  }

  /**
   * deleteKnolx method take a argument id as Int type and delete rows in knolx table and return number of
   * rows affect as Int type
   */

  def deleteKnolx(id: Int): Option[Int] = {
    val con: Option[Connection] = getConnection()
    con match {
      case Some(con) =>
        try {
          val st: Statement = con.createStatement()
          val sql: String = "delete from knolx where id=" + id
          val deleterows = st.executeUpdate(sql)
          logger.debug("Delete row successfully")
          if (deleterows > 0) Some(deleterows) else throw new Exception
        } catch {
          case ex: Exception =>
            loggerknolimpl.error("SQL syntax error in delete", ex.printStackTrace())
            none
        } finally {
          con.close()
        }
      case None =>
         none
    }
  }
  /**
   * getKnolx method take a argument id1 as Int type and return list of KnolSession
   *  objects corresponding to id1 from knolx table
   */
  def getKnolx(id1: Int): Option[KnolSession] = {
    val connection = getConnection()

    connection match {
      case Some(connection) =>
        try {
          val st: Statement = connection.createStatement()
          val sql: String = "select * from knolx where id=" + id1
          st.execute(sql)
          val rs: ResultSet = st.executeQuery(sql)
          rs.next()
          logger.debug("knols retrieve successfully")
          Some(KnolSession(rs.getString("topic"), rs.getString("session_date"), rs.getInt("knol_id"), rs.getInt("id")))
        } catch {
          case e: Exception =>
            logger.error("SQL syntax error in get knolx", e.printStackTrace())
            none
        } finally {
          connection.close()
        }
      case None =>
        none
    }
  }
  /**
   * getListKnolx method is return list of KnolSession objects from knolx table
   */

  def getListKnolx(): Option[List[KnolSession]] =
    {
      val connection = getConnection()
      connection match {
        case Some(connection) =>
          try {
            val st: Statement = connection.createStatement()
            val sql: String = "select * from knolx";
            st.execute(sql);
            var knolxlist: List[KnolSession] = List[KnolSession]()
            val rs: ResultSet = st.executeQuery(sql);
            while (rs.next()) {
              val knolxobject = KnolSession(rs.getString("topic"), rs.getString("session_date"), rs.getInt("knol_id"), rs.getInt("id"))
              knolxlist = knolxlist :+ knolxobject;
            }
            logger.debug("List retrieve")
            Some(knolxlist)
          } catch {
            case e: Exception =>
              logger.error("SQL systex error in getting list of knols", e.printStackTrace())
              none
          } finally {
            connection.close()
          }
        case None =>
          none
      }
    }
}
