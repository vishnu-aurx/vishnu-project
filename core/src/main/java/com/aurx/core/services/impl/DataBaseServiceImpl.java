package com.aurx.core.services.impl;

import com.aurx.core.services.DataBaseService;
import com.aurx.core.services.config.DataBaseNameConfiguration;
import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class establishes a connection to a MySQL database.
 */
@Component(service = DataBaseService.class, immediate = true)
@Designate(ocd = DataBaseNameConfiguration.class)
public class DataBaseServiceImpl implements DataBaseService {

  /**
   * LOGGER  - The Logger object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(
      DataBaseServiceImpl.class);

  /**
   * dataSourcePool - The dataSourcePool
   */
  @Reference
  private DataSourcePool dataSourcePool;

  /**
   * databaseName - The databaseName.
   */
  private String databaseName;

  /**
   * This method is invoked when configuration is active and modified.
   *
   * @param dataBaseNameConfiguration - The dataBaseNameConfiguration.
   */
  @Activate
  @Modified
  protected void activate(DataBaseNameConfiguration dataBaseNameConfiguration) {
    databaseName = dataBaseNameConfiguration.dataSourceName();
  }

  /**
   * This method returns the connection object.
   *
   * @return - The connection.
   */
  @Override
  public Connection getConnection() {
    LOGGER.info("Start of getConnection method");
    DataSource dataSource = null;
    Connection connection = null;
    try {
      dataSource = (DataSource) dataSourcePool.getDataSource(databaseName);
      if (dataSource != null) {
        LOGGER.info("dataSource is not null");
        connection = dataSource.getConnection();
      }
    } catch (SQLException | DataSourceNotFoundException e) {
      LOGGER.error("Exception {}", e.getMessage());
    }
    LOGGER.info("END of getConnection method");
    return connection;
  }
}
