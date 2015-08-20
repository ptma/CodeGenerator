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

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.joy.config.model.DriverInfo;
import org.joy.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JdbcDrivers {

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

            Element rootNode = doc.getDocumentElement();

            NodeList nodeList = rootNode.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node childNode = nodeList.item(i);

                if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                if ("driver".equals(childNode.getNodeName())) { //$NON-NLS-1$
                    parseDriverNode(childNode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseDriverNode(Node node) {
        String jdbcDriver = null, jdbcUrl = null, name = null;
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("driverClass".equals(childNode.getNodeName())) {
                jdbcDriver = parseElementNodeValue(childNode);
            } else if ("url".equals(childNode.getNodeName())) {
                jdbcUrl = parseElementNodeValue(childNode);
            } else if ("name".equals(childNode.getNodeName())) {
                name = parseElementNodeValue(childNode);
            }
        }
        if (StringUtil.isNotEmpty(jdbcDriver) && StringUtil.isNotEmpty(jdbcUrl)) {
            JdbcDrivers.jdbcDriversMap.put(jdbcDriver, new DriverInfo(name, jdbcDriver, jdbcUrl));
        }
    }

    private static String parseElementNodeValue(Node node) {
        if (node.getFirstChild() != null) {
            return node.getFirstChild().getNodeValue();
        } else {
            return null;
        }
    }

}
