package com.knol.core.impl

import java.sql._

import org.scalatest.BeforeAndAfter
import org.scalatest.FunSuite

import com.knol.core.KnolSession
import com.knol.core.Knols
import com.knol.core.KnolxSession
import com.knol.core.impl._
import com.knol.db.connection.DBConnection
import com.typesafe.config.Config

class FindKnolxImplTest extends FunSuite with BeforeAndAfter with DBConnection {
  before {
    val connection = getConnection()
    val knolsessionrepoimpl = new KnolSessionRepoImpl
    val knolrepoimpl = new KnolRepoImp
    connection match {
      case Some(connection) =>
        try {
          val knolsql = "create table knol(id int(4) primary key auto_increment,name char(20),email varchar(30) unique,mob int(11));"
          val stmt: Statement = connection.createStatement();
          stmt.execute(knolsql);
          val genknol = knolrepoimpl.createKnol(Knols("oo", "p@knoldus.com", 765544))
          val knolxsql = "create table knolx(id int(4) primary key auto_increment,topic varchar(30),session_date date,knol_id int(4) references knol(id));"
          stmt.execute(knolxsql);
          logger.debug("Knolx table created")
          val knolx = KnolSession("play with routing", "2015-02-11", 1)
          val knolxid = knolsessionrepoimpl.createKnolx(knolx)
        } catch {
          case ex: Exception =>
            None
        } finally {
          connection.close()
        }
      case None =>
        None
    }
  }
 after {
    val connection = getConnection()
    connection match {
      case Some(connection) =>
        try {
          val sqlknol = "drop table knol"
          val sqlknolx = "drop table knolx"
          val stmt: Statement = connection.createStatement();
          stmt.execute(sqlknol);
          stmt.execute(sqlknolx)
          connection.close()
        } catch {
          case ex: Exception =>
            None
        } finally {
          connection.close()
        }
      case None =>
        None
    }
  }
  test("Get the knolx session") {
    val findknolximpl = new FindKnolxImpl()
    val resultsession = findknolximpl.getSession(1)
    assert(resultsession === Some(List(KnolxSession(1,"oo","play with routing","2015-02-11",1))))
  }
  test("Exeption in Get the knolx session") {
    val findknolximpl = new FindKnolxImpl()
   

    val resultsession = findknolximpl.getSession(8)
    assert(resultsession === None)
  }
}