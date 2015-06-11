package com.knol.db.connection

import com.typesafe.config.ConfigFactory
import java.sql.Connection
import java.sql.DriverManager

import org.slf4j.LoggerFactory

trait DBConnection {
  val none=None;
  val logger = LoggerFactory.getLogger(this.getClass)
  val config = ConfigFactory.load();
  val url = config.getString("db.url")
  val user = config.getString("db.username")
  val password = config.getString("db.password")
  val loggerknolimpl = LoggerFactory.getLogger(this.getClass)
  def getConnection(): Option[Connection] = {
    try {
      Class.forName("com.mysql.jdbc.Driver")
      logger.debug("Connection created successfull")
      Some(DriverManager.getConnection(url, user, password))
    } catch {
      case e: Exception =>
        logger.error("Problem in making connection", e.printStackTrace())
        none
    }
  }
}
