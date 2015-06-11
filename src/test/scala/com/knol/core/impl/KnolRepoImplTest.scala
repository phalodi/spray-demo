package com.knol.core.impl

import org.scalatest.FunSuite
import com.knol.core.Knols
import org.scalatest.BeforeAndAfter
import com.knol.db.connection.DBConnection
import java.sql.Statement

class KnolRepoImplTest extends FunSuite with BeforeAndAfter with DBConnection {

  val knolrepoimpl = new KnolRepoImp
  before {
    val connection = getConnection()
    connection match {
      case Some(connection) =>
        try {
          val sql = "create table knol(id int(4) primary key auto_increment,name char(20),email varchar(30) unique,mob int(11));"
          val stmt: Statement = connection.createStatement();
          stmt.execute(sql);
          val resultcreate = knolrepoimpl.createKnol(Knols("oo", "p@knoldus.com", 765544))

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
          val sql = "drop table knol"
          val stmt: Statement = connection.createStatement();
          stmt.execute(sql);
          stmt.close()
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
  test("Create a knol") {
    val resultcreate = knolrepoimpl.createKnol(Knols("yy", "ppt@knoldus.com", 765544))
    assert(resultcreate === Some(2))

  }

  test("Update a knol") {

    val resultupdate = knolrepoimpl.updateKnol(Knols("rrp", "rrp@knoldus.com", 45435435, 1))
    assert(resultupdate === Some(1))
  }

  test("Delete a knol") {

    val resultdelete = knolrepoimpl.deleteKnol(1)
    assert(resultdelete === Some(1))
  }
  test("Get a knol") {

    val resultgetknol = knolrepoimpl.getKnol(1)

    assert(resultgetknol === Some(Knols("oo", "p@knoldus.com", 765544, 1)))
  }

  test("Get the knol list") {

    val resultgetknol = knolrepoimpl.getListKnol()
    assert(resultgetknol === Some(List(Knols("oo", "p@knoldus.com", 765544, 1))))

  }
  test(" Exception in Create a knol") {
    val resultcreate = knolrepoimpl.createKnol(Knols("yy", "p@knoldus.com", 765544))
    assert(resultcreate === None)

  }
  test("Exception in Update a knol") {
    val resultcreate = knolrepoimpl.createKnol(Knols("yy", "ps@knoldus.com", 765544))
    val resultupdate = knolrepoimpl.updateKnol(Knols("rrp", "ps@knoldus.com", 454376545, 1))
    assert(resultupdate === None)
  }
    test(" Exception in Delete a knol") {

    val resultdelete = knolrepoimpl.deleteKnol(45)
    assert(resultdelete === None)

    }
    test("Exception in Get a knol") {

    val resultgetknol = knolrepoimpl.getKnol(4)

    assert(resultgetknol === None)
  }

}