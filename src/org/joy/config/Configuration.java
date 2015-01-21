package org.joy.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.joy.config.model.DatabaseElement;
import org.joy.config.model.TemplateElement;
import org.joy.util.StringUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Configuration {

    private static final String   CONFIGURATION_FILE = "configuration.xml";
    private String                configurationFile;
    private List<DatabaseElement> connectionHistory;
    private List<String>          classPathEntries;
    private String                tagertProject;
    private String                basePackage;
    private String                moduleName;
    private List<TemplateElement> templates;

    public Configuration(String classPath){
        this.configurationFile = classPath + CONFIGURATION_FILE;
        connectionHistory = new ArrayList<DatabaseElement>();
        classPathEntries = new ArrayList<String>();
        templates = new ArrayList<TemplateElement>();
    }

    public void loadConfiguration() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(configurationFile);
        // Get the root element <configuration>
        Element rootNode = doc.getDocumentElement();

        NodeList nodeList = rootNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            if ("classpath".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseClassPathEntry(childNode);
            } else if ("connections".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseConnections(childNode);
            } else if ("tagertProject".equals(childNode.getNodeName())) { //$NON-NLS-1$
                tagertProject = parseElementNodeValue(childNode);
            } else if ("basePackage".equals(childNode.getNodeName())) { //$NON-NLS-1$
                basePackage = parseElementNodeValue(childNode);
            } else if ("moduleName".equals(childNode.getNodeName())) { //$NON-NLS-1$
                moduleName = parseElementNodeValue(childNode);
            } else if ("templates".equals(childNode.getNodeName())) { //$NON-NLS-1$
                parseTemplates(childNode);
            }
        }

    }

    private void parseClassPathEntry(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("entry".equals(childNode.getNodeName())) {
                String entry = parseElementNodeValue(childNode);
                if (StringUtil.isNotEmpty(entry)) {
                    classPathEntries.add(entry);
                }
            }
        }
    }

    private void parseConnections(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("database".equals(childNode.getNodeName())) {
                parseDatabase(childNode);
            }
        }
    }

    private void parseDatabase(Node node) {
        NodeList nodeList = node.getChildNodes();
        String name = null, driverClass = null, url = null, username = null, password = null, schema = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("driverClass".equals(childNode.getNodeName())) {
                driverClass = parseElementNodeValue(childNode);
            } else if ("url".equals(childNode.getNodeName())) {
                url = parseElementNodeValue(childNode);
            } else if ("username".equals(childNode.getNodeName())) {
                username = parseElementNodeValue(childNode);
            } else if ("password".equals(childNode.getNodeName())) {
                password = parseElementNodeValue(childNode);
            } else if ("schema".equals(childNode.getNodeName())) {
                schema = parseElementNodeValue(childNode);
            }
        }

        name = parseAttributes(node).getProperty("name");
        if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(driverClass) && StringUtil.isNotEmpty(url)
            && StringUtil.isNotEmpty(username)) {
            connectionHistory.add(new DatabaseElement(name, driverClass, url, username, password, schema));
        }
    }

    private void parseTemplates(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("template".equals(childNode.getNodeName())) {
                parseTemplate(childNode);
            }
        }
    }

    private void parseTemplate(Node node) {
        NodeList nodeList = node.getChildNodes();
        String name = null, engine = null, templateFile = null, targetPath = null, targetFileName = null, encoding = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);

            if (childNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("templateFile".equals(childNode.getNodeName())) {
                templateFile = parseElementNodeValue(childNode);
            } else if ("targetPath".equals(childNode.getNodeName())) {
                targetPath = parseElementNodeValue(childNode);
            } else if ("targetFileName".equals(childNode.getNodeName())) {
                targetFileName = parseElementNodeValue(childNode);
            } else if ("encoding".equals(childNode.getNodeName())) {
                encoding = parseElementNodeValue(childNode);
            }
        }

        Properties attrs = parseAttributes(node);
        name = attrs.getProperty("name");
        engine = attrs.getProperty("engine");
        if (StringUtil.isNotEmpty(name) && StringUtil.isNotEmpty(engine) && StringUtil.isNotEmpty(templateFile)
            && StringUtil.isNotEmpty(targetPath) && StringUtil.isNotEmpty(targetFileName)
            && StringUtil.isNotEmpty(encoding)) {
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

            Element e = doc.createElement("tagertProject");
            e.appendChild(doc.createTextNode(tagertProject));
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
            e.printStackTrace();
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

    public String getTagertProject() {
        return tagertProject;
    }

    public void setTagertProject(String tagertProject) {
        this.tagertProject = tagertProject;
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
