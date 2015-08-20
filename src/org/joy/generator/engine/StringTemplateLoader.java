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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import freemarker.cache.TemplateLoader;

public class StringTemplateLoader implements TemplateLoader {

    private String template;

    public StringTemplateLoader(String template){
        this.template = template;
        if (template == null) {
            this.template = "";
        }
    }

    public void closeTemplateSource(Object templateSource) throws IOException {
        ((StringReader) templateSource).close();
    }

    public Object findTemplateSource(String name) throws IOException {
        return new StringReader(template);
    }

    public long getLastModified(Object templateSource) {
        return 0;
    }

    public Reader getReader(Object templateSource, String encoding) throws IOException {
        return (Reader) templateSource;
    }
}
