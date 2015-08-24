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
package org.joy.config;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.joy.config.model.DriverInfo;
import org.joy.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JdbcDrivers implements Serializable {

    private static final long serialVersionUID = -7067509337564022948L;

    private static final Logger            LOGGER = Logger.getLogger(JdbcDrivers.class);

    private static Map<String, DriverInfo> jdbcDriversMap;

    static {
        jdbcDriversMap = new HashMap<String, DriverInfo>();
        load();
    }

    private JdbcDrivers(){
        super();
    }

    public static Map<String, DriverInfo> getJdbcDriversMap() {
        return jdbcDriversMap;
    }

    public static DriverInfo getDriverInfo(String driverClass){
        if(jdbcDriversMap.containsKey(driverClass)){

            return jdbcDriversMap.get(driverClass);
        } else {
            return null;
        }
    }

    private static void load() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(JdbcDrivers.class.getResourceAsStream("jdbcDrivers.xml"));

            XPathFactory f = XPathFactory.newInstance();
            XPath path = f.newXPath();

            NodeList driverList = (NodeList) path.evaluate("jdbcDrivers/driver", doc, XPathConstants.NODESET);
            if (driverList != null) {
                for (int i = 0; i < driverList.getLength(); i++) {
                    parseDriverNode(driverList.item(i), path);
                }
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    private static void parseDriverNode(Node node, XPath path) throws XPathExpressionException {
        String jdbcDriver = path.evaluate("./driverClass/text()", node);
        String jdbcUrl = path.evaluate("./url/text()", node);
        String name = path.evaluate("./name/text()", node);
        if (StringUtil.isNotEmpty(jdbcDriver) && StringUtil.isNotEmpty(jdbcUrl)) {
            JdbcDrivers.jdbcDriversMap.put(jdbcDriver, new DriverInfo(name, jdbcDriver, jdbcUrl));
        }
    }

}
