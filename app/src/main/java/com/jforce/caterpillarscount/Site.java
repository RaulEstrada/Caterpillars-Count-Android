package com.jforce.caterpillarscount;

import java.util.Comparator;

/**
 * Created by justinforsyth on 12/5/14.
 */
public class Site implements Comparable<Site>{

    private int siteID;
    private String password;
    private String name;
    private String stateCode;

    public Site(int siteID, String password, String name, String stateCode){
        this.siteID = siteID;
        this.password = password;
        this.name = name;
        this.stateCode = stateCode;
    }

    public int getSiteID() {
        return siteID;
    }

    public void setSiteID(int siteID) {
        this.siteID = siteID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public int compareTo(Site site){
        return this.siteID - site.siteID;
    }

}
