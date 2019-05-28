package com.example.personalwellness;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

public class ResourceDB implements Serializable {
    private static ResourceDB singleton = null;
    private List<Resource> resourceList = new ArrayList<Resource>();

    private  ResourceDB() {

    }

    public static ResourceDB getResourceDB () {
        if (singleton == null) {
            singleton = new ResourceDB();
        }
        return singleton;
    }

    public void clearResources() {
        resourceList = new ArrayList<Resource>();
    }

    public List<Resource> getCategoryResource(String category) {
        List<Resource> categoryResources = null;
        for (Resource curr : resourceList) {
            if (curr.getCategory().equals(category)) {
                categoryResources.add(curr);
            }
        }
        return categoryResources;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceDB(List<Resource> resources) {
        for (Resource r : resources) {
            resourceList.add(r);
        }
    }
}
