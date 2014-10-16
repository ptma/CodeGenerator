package org.joy.generator.engine;

import java.util.Map;

import org.joy.config.model.TemplateElement;


public interface TemplateEngine {

    public String processToString(Map<String, Object> model, String stringTemplate) throws TemplateEngineException;

    public void processToFile(Map<String, Object> model, TemplateElement templateElement) throws TemplateEngineException;
}
