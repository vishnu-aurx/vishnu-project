package com.aurx.core.services;

import com.aurx.core.pojo.ComponentReport;
import java.util.List;
import org.apache.sling.api.resource.Resource;

/**
 * Interface ComponentReportService.
 */
public interface ComponentReportService {

   /**
    * This method return the list of the resource.
    * @return -List of the Resource.
    */
   List<Resource> fetchComponents();

   /**
    * This method fetches the title of the component.
    */
   void fetchTitle();

   /**
    * This method return the list of the componentReport object
    * @return - List of the ComponentReport.
    */
   List<ComponentReport> comopnentList();

}
