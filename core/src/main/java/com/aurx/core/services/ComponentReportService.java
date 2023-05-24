package com.aurx.core.services;

import com.aurx.core.pojo.ComponentReport;
import java.util.List;
import org.apache.sling.api.resource.Resource;

public interface ComponentReportService {
   List<Resource> fetchComponents();
   void fetchTitle();
   public List<ComponentReport> comopnentList();

}
