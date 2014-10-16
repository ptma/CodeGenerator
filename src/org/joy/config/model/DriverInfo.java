package org.joy.config.model;

public class DriverInfo {

    private String name;
    private String driverClass;
    private String url;

    public DriverInfo(String name, String driverClass, String url){
        this.name = name;
        this.driverClass = driverClass;
        this.url = url;
    }

    public String toString() {
        return driverClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
