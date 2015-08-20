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
package org.joy.generator.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.joy.config.model.TemplateElement;
import org.joy.util.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

public class FreeMarkerImpl implements TemplateEngine {

    private Configuration config;

    private String classPath;

    public FreeMarkerImpl(String classPath){
        this.classPath = classPath;
        initConfiguration();
    }

    public void initConfiguration() {
        try {
            config = new Configuration();
            config.setDirectoryForTemplateLoading(new File(classPath + "templates/freemarker"));
            config.setObjectWrapper(new DefaultObjectWrapper());

            config.setSetting("classic_compatible", "true");
            config.setSetting("whitespace_stripping", "true");
            config.setSetting("template_update_delay", "1");
            config.setSetting("locale", "zh_CN");
            config.setSetting("default_encoding", "utf-8");
            config.setSetting("url_escaping_charset", "utf-8");
            config.setSetting("datetime_format", "yyyy-MM-dd hh:mm:ss");
            config.setSetting("date_format", "yyyy-MM-dd");
            config.setSetting("time_format", "HH:mm:ss");
            config.setSetting("number_format", "0.######;");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String processToString(Map<String, Object> model, String stringTemplate) throws TemplateEngineException {
        try {
            Configuration cfg = new Configuration();
            cfg.setTemplateLoader(new StringTemplateLoader(stringTemplate));
            cfg.setDefaultEncoding("UTF-8");

            Template template = cfg.getTemplate("");
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (Exception e) {
            throw new TemplateEngineException(e.getMessage(), e);
        }
    }

    public void processToFile(Map<String, Object> model, TemplateElement templateElement) throws TemplateEngineException {
        try {
            Template template = config.getTemplate(templateElement.getTemplateFile(), templateElement.getEncoding());
            String targetPath = StringUtil.packagePathToFilePath(processToString(model, templateElement.getTargetPath()));
            String targetFileName = processToString(model, templateElement.getTargetFileName());
            File file = new File(targetPath + File.separator + targetFileName);
            File directory = new File(targetPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                                                                   templateElement.getEncoding()));
            template.process(model, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new TemplateEngineException(e.getMessage(), e);
        }
    }

}
