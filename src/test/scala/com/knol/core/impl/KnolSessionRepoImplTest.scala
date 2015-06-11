package com.knol.core.impl
import com.knol.core.impl._
import org.scalatest.BeforeAndAfter
import com.knol.db.connection.DBConnection
import org.scalatest.FunSuite
import com.knol.core.Knols
import java.sql.Statement
import com.knol.core.KnolSession
import java.util.Date
import com.knol.core.FindKnolx

class KnolSessionRepoImplTest extends FunSuite with BeforeAndAfter with DBConnection{
  val knolsessionrepoimpl = new KnolSessionRepoImpl
  val knolrepoimpl = new KnolRepoImp
  before {
    val connection = getConnection()
    connection match {
      case Some(connection) =>
        try {
          val knolsql = "create table knol(id int(4) primary key auto_increment,name char(20),email varchar(30),mob int(11));"
          val stmt: Statement = connection.createStatement();
          stmt.execute(knolsql);
          val genknol=knolrepoimpl.createKnol(Knols("oo", "p@knoldus.com", 765544))
          val knolxsql = "create table knolx(id int(4) primary key auto_increment,topic varchar(30),session_date date,knol_id int(4) references knol(id));"
          stmt.execute(knolxsql);
          logger.debug("Knolx table created")
          val knolx=KnolSession("play with routing","2015-02-11",1)
          val knolxid=knolsessionrepoimpl.createKnolx(knolx)
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
          val sqlknolx="drop table knolx"
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
  test("Create a knolx") {
    val resultcreate = knolsessionrepoimpl.createKnolx(KnolSession("play", "2015-01-01", 1))
    assert(resultcreate === Some(2))
}
 test("Update a knolx") {

    val resultupdate = knolsessionrepoimpl.updateKnolx(KnolSession("akka", "2015-01-05",1,1))
    assert(resultupdate === Some(1))
  }
  test("Delete a knolx") {

    val resultdelete = knolsessionrepoimpl.deleteKnolx(1)
    assert(resultdelete === Some(1))
  }
 test("Get a knolx") {

    val resultgetknol = knolsessionrepoimpl.getKnolx(1)

    assert(resultgetknol === Some(KnolSession("play with routing","2015-02-11",1,1)))
  }
  test("Get the knolx list") {

    val resultgetknol = knolsessionrepoimpl.getListKnolx()
    assert(resultgetknol === Some(List(KnolSession("play with routing","2015-02-11",1,1))))
   
  }
  test("Exception in Create a knolx") {
    val resultcreate = knolsessionrepoimpl.createKnolx(KnolSession("play", "201501-01",1))
    assert(resultcreate === None)
}
  test("Exception in Update a knolx") {

    val resultupdate = knolsessionrepoimpl.updateKnolx(KnolSession("akka", "2015-01",1,1))
    assert(resultupdate === None)
  }
   test("Exception in Get a knolx") {

    val resultgetknol = knolsessionrepoimpl.getKnolx(3)

    assert(resultgetknol === None)
  }
  test("Exception in Delete a knolx") {

    val resultdelete = knolsessionrepoimpl.deleteKnolx(98)
    assert(resultdelete === None)
  }
}