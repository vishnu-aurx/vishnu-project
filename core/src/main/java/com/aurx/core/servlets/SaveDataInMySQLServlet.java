package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.EMPTY_PARAM;
import static com.aurx.core.constant.ApplicationConstants.ERROR;
import static com.aurx.core.constant.ApplicationConstants.LOCAL_DB_TEST;
import static com.aurx.core.constant.ApplicationConstants.NUMBER_CONSTANT;
import static com.aurx.core.constant.ApplicationConstants.STATUS;
import static com.aurx.core.constant.ApplicationConstants.SUCCESSFUL;
import static com.aurx.core.constant.ApplicationConstants.USER_NAME;

import com.day.commons.datasource.poolservice.DataSourceNotFoundException;
import com.day.commons.datasource.poolservice.DataSourcePool;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = Servlet.class, immediate = true, property = {"sling.servlet.methods=GET",
    "sling.servlet.paths=/bin/mysql", "sling.servlet.selectors=save",
    "sling.servlet.extensions=json"})
public class SaveDataInMySQLServlet extends SlingSafeMethodsServlet {

  /**
   * dataSourcePool - The dataSourcePool
   */
  @Reference
  private transient DataSourcePool dataSourcePool;

  private transient JsonObject jsonObject;

  /**
   * LOGGER - The Logger object.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SaveDataInMySQLServlet.class);

  /**
   * This method is used to get username and number from a URL.
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
      throws ServletException, IOException {
    LOGGER.info("doGetMethod start");
    jsonObject = new JsonObject();
    String name = request.getParameter(USER_NAME);
    String number = request.getParameter(NUMBER_CONSTANT);
    LOGGER.info("Name : {} , Number : {}", name, number);
    if (StringUtils.isNotBlank(number) && StringUtils.isNotBlank(name) && number.matches("\\d+")) {
      int userNumber = Integer.parseInt(number);
      JsonObject saveDataInMySQL = null;
      try {
        saveDataInMySQL = saveDataInMySQL(name, userNumber);
      } catch (SQLException e) {
        LOGGER.error("Exception {}", e.getMessage());
      }
      if (saveDataInMySQL != null) {
        response.getWriter().write(saveDataInMySQL.toString());
      }
    } else {
      LOGGER.info("Empty param or number not correct");
      jsonObject.addProperty(STATUS, EMPTY_PARAM);
      response.getWriter().write(jsonObject.toString());
    }
    LOGGER.info("END of doGet Method");
  }

  /**
   * This method is used to save the data in MySQL.
   *
   * @param name       - The name.
   * @param userNumber - The userNumber.
   */
  private JsonObject saveDataInMySQL(String name, int userNumber) throws SQLException {
    LOGGER.info("saveDataInMySQL method Start with name : {} , userNumber : {}", name, userNumber);

    DataSource dataSource = null;
    Connection connection = null;
    try {
      dataSource = (DataSource) dataSourcePool.getDataSource(LOCAL_DB_TEST);
      if (dataSource != null) {
        LOGGER.info("dataSource is not null");
        connection = dataSource.getConnection();
        if (connection != null) {
          LOGGER.info("connection is not null");
          try (final Statement statement = connection.createStatement();) {
            if (statement != null) {
              statement.executeQuery("CALL setdata(" + userNumber + ",'" + name + "')");
              jsonObject.addProperty(STATUS, SUCCESSFUL);
            }
            LOGGER.info("data save successfully");
          }
        } else {
          jsonObject.addProperty(STATUS, ERROR);
        }
      } else {
        jsonObject.addProperty(STATUS, ERROR);
      }
    } catch (SQLException | DataSourceNotFoundException e) {
      jsonObject.addProperty(STATUS, ERROR);
      LOGGER.error("Exception {}", e.getMessage());
    } finally {
      if (connection != null) {
        connection.close();
        LOGGER.info("Connection is closed");
      }
    }
    LOGGER.info("END of saveDataInMySQL method");
    return jsonObject;
  }
}
