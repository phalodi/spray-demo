package com.knol.core.impl

import java.sql.Connection
import com.knol.core._
import com.knol.db.connection.DBConnection
import com.knol.db.connection.DBConnection
import java.sql.PreparedStatement
import java.sql.Statement
import java.sql.ResultSet
import org.slf4j.LoggerFactory
class KnolRepoImp extends KnolRepo with DBConnection {
  var genkey: Int = 0;

  /**
   * createKnol method take a knol object as an argument and persist a knols object into the knol table and
   *  return the last auto incremented primary key
   */
  def createKnol(knols: Knols): Option[Int] = {
    val con: Option[Connection] = getConnection()
    con match {
      case Some(con) =>
        try {
          val sql: String = "insert into knol(name,email,mob) values('" + knols.name + "','" + knols.email + "','" + knols.mob + "');"
          val stmt: PreparedStatement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
          val num = stmt.executeUpdate();
          val rs: ResultSet = stmt.getGeneratedKeys();
          while (rs.next()) {
            genkey = rs.getInt(1)
          }
          logger.debug("Created Id:" + genkey)
          Some(genkey)
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
   * updateKnol method take argument of Knols object and update the knols object into the knol table and
   *  return the number of row affected
   */
  def updateKnol(knols: Knols): Option[Int] = {
    val con: Option[Connection] = getConnection()
    con match {
      case Some(con) =>
        try {

          val st: Statement = con.createStatement()

          val sql: String = "update knol set name = '" + knols.name + "', email='" + knols.email + "',mob='" + knols.mob + "' where id=" + knols.id

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
   * deleteKnol method take a argument of integer type id and delete the knols object from the knol table
   * correspoding to id and return number of rows affected as an integer
   */
  def deleteKnol(id: Int): Option[Int] = {
    val con: Option[Connection] = getConnection()

    con match {
      case Some(con) =>
        try {
          val st: Statement = con.createStatement()
          val sql: String = "delete from knol where id=" + id
          val deleterows = st.executeUpdate(sql);
          logger.debug("Delete row successfully")
          if (deleterows > 0) Some(deleterows) else throw new Exception
          Some(deleterows)
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
   * getKnol method is take the argument id of type integer and return the knols object corresponding to id
   */
  def getKnol(id1: Int): Option[Knols] = {
    val connection = getConnection()

    connection match {
      case Some(connection) =>
        try {
          val st: Statement = connection.createStatement()
          val sql: String = "select * from knol where id=" + id1
          st.execute(sql)
          val rs: ResultSet = st.executeQuery(sql)
          rs.next()
          logger.debug("knols retrieve successfully")
          Some(Knols(rs.getString("name"), rs.getString("email"), rs.getInt("mob"), rs.getInt("id")))
        } catch {
          case e: Exception =>
            logger.error("SQL syntax error in get knol", e.printStackTrace())
            none
        } finally {
          connection.close()
        }
      case None =>
        none
    }
  }

  /**
   * getListKnol method is return the list of type knols object
   */
  def getListKnol(): Option[List[Knols]] =
    {
      val connection = getConnection()
      connection match {
        case Some(connection) =>
          try {
            val st: Statement = connection.createStatement()
            val sql: String = "select * from knol";
            st.execute(sql);
            var knolist: List[Knols] = List[Knols]()
            val rs: ResultSet = st.executeQuery(sql);
            while (rs.next()) {
              val knolobject = Knols(rs.getString("name"), rs.getString("email"), rs.getInt("mob"), rs.getInt("id"))
              knolist = knolist :+ knolobject;
            }
            logger.debug("List retrieve")
            Some(knolist)
          } catch {
            case e: Exception =>

              none
          } finally {
            connection.close()
          }
        case None =>

          none
      }
    }
}
