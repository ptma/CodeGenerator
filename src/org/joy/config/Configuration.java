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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.joy.config.model.DatabaseElement;
import org.joy.config.model.TemplateElement;
import org.joy.exception.ApplicationException;
import org.joy.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configuration {

    private static final Logger   LOGGER             = Logger.getLogger(Configuration.class);

    private static final String   CONFIGURATION_FILE = "configuration.xml";
    private String                configurationFile;
    private List<DatabaseElement> connectionHistory;
    private List<String>          classPathEntries;
    private String                targetProject;
    private String                basePackage;
    private String                moduleName;
    private List<TemplateElement> templates;

    public Configuration(String classPath){
        this.configurationFile = classPath + CONFIGURATION_FILE;
        connectionHistory = new ArrayList<DatabaseElement>();
        classPathEntries = new ArrayList<String>();
        templates = new ArrayList<TemplateElement>();
    }

    public void loadConfiguration() throws ApplicationException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(configurationFile);

            XPathFactory f = XPathFactory.newInstance();
            XPath path = f.newXPath();

            parseClassPathEntry(doc, path);
            parseConnections(doc, path);
            parseTemplates(doc, path);

            targetProject = path.evaluate("/configuration/targetProject/text()", doc);
            basePackage = path.evaluate("/configuration/basePackage/text()", doc);
            moduleName = path.evaluate("/configuration/moduleName/text()", doc);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    private void parseClassPathEntry(Document doc, XPath path) throws XPathExpressionException {
        NodeList classpathEntrys = (NodeList) path.evaluate("configuration/classpath/entry", doc,
                                                            XPathConstants.NODESET);
        if (classpathEntrys != null) {
            for (int i = 0; i < classpathEntrys.getLength(); i++) {
                String entry = parseElementNodeValue(classpathEntrys.item(i));
                if (StringUtil.isNotEmpty(entry)) {
                    classPathEntries.add(entry);
                }
            }
        }
    }

    private void parseConnections(Document doc, XPath path) throws XPathExpressionException {
        NodeList databaseList = (NodeList) path.evaluate("configuration/connections/database", doc,
                                                         XPathConstants.NODESET);
        if (databaseList != null) {
            for (int i = 0; i < databaseList.getLength(); i++) {
                parseDatabase(databaseList.item(i), path);
            }
        }
    }

    private void parseDatabase(Node node, XPath path) throws XPathExpressionException {
        String name = path.evaluate("@name", node);
        String driverClass = path.evaluate("./driverClass/text()", node);
        String url = path.evaluate("./url/text()", node);
        String username = path.evaluate("./username/text()", node);
        String password = path.evaluate("./password/text()", node);
        String schema = path.evaluate("./schema/text()", node);

        if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(driverClass) && StringUtil.isNotEmpty(url)
            && StringUtil.isNotEmpty(username)) {
            connectionHistory.add(new DatabaseElement(name, driverClass, url, username, password, schema));
        }
    }

    private void parseTemplates(Document doc, XPath path) throws XPathExpressionException {
        NodeList templateList = (NodeList) path.evaluate("configuration/templates/template", doc,
                                                         XPathConstants.NODESET);
        if (templateList != null) {
            for (int i = 0; i < templateList.getLength(); i++) {
                parseTemplate(templateList.item(i), path);
            }
        }
    }

    private void parseTemplate(Node node, XPath path) throws XPathExpressionException {
        String name = null, engine = null, templateFile = null, targetPath = null, targetFileName = null, encoding = null;
        templateFile = path.evaluate("./templateFile/text()", node);
        targetPath = path.evaluate("./targetPath/text()", node);
        targetFileName = path.evaluate("./targetFileName/text()", node);
        encoding = path.evaluate("./encoding/text()", node);

        name = path.evaluate("@name", node);
        engine = path.evaluate("@engine", node);

        if (StringUtil.isEmpty(engine)) {
            engine = "freemarker";
        }
        if (StringUtil.isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        if (StringUtil.isEmpty(name)) {
            name = templateFile;
        }
        if (StringUtil.isNotEmpty(templateFile) && StringUtil.isNotEmpty(targetPath)
            && StringUtil.isNotEmpty(targetFileName)) {
            TemplateElement templateElement = new TemplateElement(name, engine, templateFile, targetPath,
                                                                  targetFileName, encoding);
            templates.add(templateElement);
            templateElement.setSelected(Boolean.parseBoolean(parseAttributes(node).getProperty("selected")));
        }
    }

    private String parseElementNodeValue(Node node) {
        if (node.getFirstChild() != null) {
            return node.getFirstChild().getNodeValue();
        } else {
            return null;
        }
    }

    public DatabaseElement getHistoryByName(String name) {
        for (int i = 0; i < connectionHistory.size(); i++) {
            if (connectionHistory.get(i).getName().equalsIgnoreCase(name)) {
                return connectionHistory.get(i);
            }
        }
        return null;
    }

    public void addHistory(DatabaseElement database) {
        for (int i = 0; i < connectionHistory.size(); i++) {
            DatabaseElement hisItem = connectionHistory.get(i);
            if (hisItem.getName().equalsIgnoreCase(database.getName())) {
                hisItem.setDriverClass(database.getDriverClass());
                hisItem.setConnectionUrl(database.getConnectionUrl());
                hisItem.setUsername(database.getUsername());
                hisItem.setPassword(database.getPassword());
                hisItem.setSchema(database.getSchema());
                return;
            }
        }
        connectionHistory.add(database);
    }

    public void removeHistory(DatabaseElement database) {
        removeHistory(database.getName());
    }

    public void removeHistory(String name) {
        Iterator<DatabaseElement> iterator = connectionHistory.iterator();
        while (iterator.hasNext()) {
            DatabaseElement hisItem = iterator.next();
            if (hisItem.getName().equalsIgnoreCase(name)) {
                iterator.remove();
                return;
            }
        }
    }

    private Properties parseAttributes(Node node) {
        Properties attributes = new Properties();
        NamedNodeMap nnm = node.getAttributes();
        for (int i = 0; i < nnm.getLength(); i++) {
            Node attribute = nnm.item(i);
            String value = attribute.getNodeValue();
            attributes.put(attribute.getNodeName(), value);
        }

        return attributes;
    }

    public void save() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("configuration");
            doc.appendChild(root);

            Element classpathEle = doc.createElement("classpath");
            root.appendChild(classpathEle);
            for (String s : classPathEntries) {
                Element e = doc.createElement("entry");
                e.appendChild(doc.createTextNode(s));
                classpathEle.appendChild(e);
            }

            Element connectionsEle = doc.createElement("connections");
            root.appendChild(connectionsEle);
            for (DatabaseElement d : connectionHistory) {
                writeDatabase(connectionsEle, d);
            }

            Element e = doc.createElement("targetProject");
            e.appendChild(doc.createTextNode(targetProject));
            root.appendChild(e);

            e = doc.createElement("basePackage");
            e.appendChild(doc.createTextNode(basePackage));
            root.appendChild(e);

            e = doc.createElement("moduleName");
            e.appendChild(doc.createTextNode(moduleName));
            root.appendChild(e);

            Element templatesEle = doc.createElement("templates");
            root.appendChild(templatesEle);
            for (TemplateElement t : templates) {
                writeTemplate(templatesEle, t);
            }

            // Write the file
            DOMSource ds = new DOMSource(doc);
            StreamResult sr = new StreamResult(new File(configurationFile));
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.STANDALONE, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            t.transform(ds, sr);
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
    }

    private void writeDatabase(Element elem, DatabaseElement db) {
        Element e = elem.getOwnerDocument().createElement("database");
        e.setAttribute("name", db.getName());
        setTextChild(e, "driverClass", db.getDriverClass());
        setTextChild(e, "url", db.getConnectionUrl());
        setTextChild(e, "username", db.getUsername());
        setTextChild(e, "password", db.getPassword());
        setTextChild(e, "schema", db.getSchema());

        elem.appendChild(e);
    }

    private void writeTemplate(Element elem, TemplateElement t) {
        Element e = elem.getOwnerDocument().createElement("template");
        e.setAttribute("name", t.getTemplateName());
        e.setAttribute("engine", t.getEngine());
        e.setAttribute("selected", String.valueOf(t.isSelected()));
        setTextChild(e, "templateFile", t.getTemplateFile());
        setTextChild(e, "targetPath", t.getTargetPath());
        setTextChild(e, "targetFileName", t.getTargetFileName());
        setTextChild(e, "encoding", t.getEncoding());
        elem.appendChild(e);
    }

    /**
     * Convenience function to set the text of a child element.
     */
    private void setTextChild(Element elem, String name, String value) {
        Document doc = elem.getOwnerDocument();
        Element e = doc.createElement(name);
        e.appendChild(doc.createTextNode(value));
        elem.appendChild(e);
    }

    public List<DatabaseElement> getConnectionHistory() {
        return connectionHistory;
    }

    public List<String> getClassPathEntries() {
        return classPathEntries;
    }

    public String getTargetProject() {
        return targetProject;
    }

    public void setTargetProject(String targetProject) {
        this.targetProject = targetProject;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public List<TemplateElement> getTemplates() {
        return templates;
    }
}
