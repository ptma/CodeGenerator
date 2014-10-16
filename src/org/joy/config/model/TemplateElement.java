package org.joy.config.model;

public class TemplateElement {

    private String templateName;
    private String engine;
    private String templateFile;
    private String targetPath;
    private String targetFileName;
    private String encoding;
    private boolean selected;

    public String getTemplateFile() {
        return templateFile;
    }

    public TemplateElement(String templateName, String engine, String templateFile, String targetPath, String targetFileName, String encoding){
        super();
        this.templateName = templateName;
        this.engine = engine;
        this.templateFile = templateFile;
        this.targetPath = targetPath;
        this.targetFileName = targetFileName;
        this.encoding = encoding;
    }

    public void setTemplateFile(String templateFile) {
        this.templateFile = templateFile;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    public String getTemplateName() {
        return templateName;
    }


    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public String toString() {
        return templateName + " <"+engine+">";
    }


    public boolean isSelected() {
        return selected;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public String getEngine() {
        return engine;
    }


    public void setEngine(String engine) {
        this.engine = engine;
    }


    public String getEncoding() {
        return encoding;
    }


    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
