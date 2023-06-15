package com.aurx.core.servlets;

import static com.aurx.core.constant.ApplicationConstants.DROPDOWN_KEY_1;
import static com.aurx.core.constant.ApplicationConstants.DROPDOWN_KEY_2;
import static com.aurx.core.constant.ApplicationConstants.DROPDOWN_KEY_3;
import static com.aurx.core.constant.ApplicationConstants.DROPDOWN_VALUE_1;
import static com.aurx.core.constant.ApplicationConstants.DROPDOWN_VALUE_2;
import static com.aurx.core.constant.ApplicationConstants.DROPDOWN_VALUE_3;
import static com.aurx.core.constant.ApplicationConstants.TEXT;
import static com.aurx.core.constant.ApplicationConstants.VALUE;

import com.adobe.cq.commerce.common.ValueMapDecorator;
import com.adobe.granite.ui.components.ds.DataSource;
import com.adobe.granite.ui.components.ds.SimpleDataSource;
import com.adobe.granite.ui.components.ds.ValueMapResource;
import com.aurx.core.pojo.DynamicDropDownKeyValue;
import com.day.cq.commons.jcr.JcrConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.Servlet;
import org.apache.commons.collections4.iterators.TransformIterator;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceMetadata;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This servlet is used to dynamically set the DropDown value in a dialog.
 */
@Component(service = Servlet.class, immediate = true, property = {
    "sling.servlet.paths=/apps/dropdownList", "sling.servlet.selectors=dropDownValue"})
public class DynamicPopulateDropdownValueServlet extends SlingSafeMethodsServlet {

  /**
   * LOGGER - Logger object
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(
      DynamicPopulateDropdownValueServlet.class);

  /**
   * This method is used to set the value in the dropdown.
   *
   * @param request  - The request
   * @param response - The response
   */
  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
    LOGGER.info("Start of doGet Method ");
    try {
      ResourceResolver resourceResolver = request.getResourceResolver();
      List<DynamicDropDownKeyValue> dropDownList = new ArrayList<>();
      dropDownList.add(new DynamicDropDownKeyValue(DROPDOWN_KEY_1, DROPDOWN_VALUE_1));
      dropDownList.add(new DynamicDropDownKeyValue(DROPDOWN_KEY_2, DROPDOWN_VALUE_2));
      dropDownList.add(new DynamicDropDownKeyValue(DROPDOWN_KEY_3, DROPDOWN_VALUE_3));
      LOGGER.info("Inside the doGet Method with dropDownList : {}", dropDownList);
      DataSource ds = new SimpleDataSource(
          new TransformIterator<>(dropDownList.iterator(), input -> {
            ValueMap vm = new ValueMapDecorator(new HashMap<>());
            vm.put(VALUE, input.getKey());
            vm.put(TEXT, input.getValue());
            return new ValueMapResource(resourceResolver, new ResourceMetadata(),
                JcrConstants.NT_UNSTRUCTURED, vm);
          }));
      request.setAttribute(DataSource.class.getName(), ds);
    } catch (Exception e) {
      LOGGER.error("Error in Get Drop Down Values: {}", e.getMessage());
    }
    LOGGER.info("End of doGet Method");
  }
}
