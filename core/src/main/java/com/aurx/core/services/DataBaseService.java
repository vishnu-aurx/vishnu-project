package com.aurx.core.services;
import java.sql.Connection;

/**
 * Interface DataBaseService.
 */
public interface DataBaseService {

  /**
   * This method returns the connection object.
   *
   * @return The connection.
   */
  Connection getConnection();
}
