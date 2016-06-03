/*
 * Copyright 2014 ptma@163.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joy.config.model;

/**
 * JDBC Driver Template.
 */
public class DriverInfo {

    /** The name. */
    private String name;

    /** The driver class. */
    private String driverClass;

    /** The url template. */
    private String url;

    /**
     * Instantiates a new driver info.
     *
     * @param name the name
     * @param driverClass the driver class
     * @param url the url template
     */
    public DriverInfo(String name, String driverClass, String url){
        this.name = name;
        this.driverClass = driverClass;
        this.url = url;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the driver class.
     *
     * @return the driver class
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * Sets the driver class.
     *
     * @param driverClass the new driver class
     */
    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    /**
     * Gets the url template.
     *
     * @return the url template
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url template.
     *
     * @param url the new url template
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return driverClass;
    }


}
