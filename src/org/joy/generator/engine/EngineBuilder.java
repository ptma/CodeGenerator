package org.joy.generator.engine;

import java.util.HashMap;
import java.util.Map;


public class EngineBuilder {

    private Map<String, TemplateEngine> engineMap;

    public EngineBuilder(String classPath) {
        engineMap = new HashMap<String, TemplateEngine>();
        synchronized (this) {
            engineMap.put("freemarker", new FreeMarkerImpl(classPath));
            engineMap.put("velocity", new VelocityImpl(classPath));
        }
    }

    public TemplateEngine getTemplateEngine(String engine) {
        return engineMap.get(engine);
    }
}
