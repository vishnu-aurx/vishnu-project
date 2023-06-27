package com.aurx.core.services;
import java.sql.Connection;

/**
 * Interface DataBaseService provides methods to establish a connection to a MySQL database and retrieve the connection object.
 */
public interface DataBaseService {

  /**
   * This method returns the connection object.
   *
   * @return The connection.
   */
  Connection getConnection();
}
