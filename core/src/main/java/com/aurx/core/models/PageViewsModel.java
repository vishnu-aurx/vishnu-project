package com.aurx.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Reference;

import java.util.Set;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PageViewsModel {
    @OSGiService
    private SlingSettingsService slingSettingsService;

    private Set<String> runModes;

    public boolean isAuthor(){
        runModes = this.slingSettingsService.getRunModes();
        if(runModes != null && runModes.contains("author")){
            return true;
        }

        return false;
    }
}
