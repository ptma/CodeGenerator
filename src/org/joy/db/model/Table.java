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
package org.joy.db.model;

import java.util.ArrayList;
import java.util.List;

import org.joy.util.StringUtil;

public class Table implements java.io.Serializable {

    private static final long serialVersionUID = -7246043091254837124L;
    private String            tableName;
    private String            tableType;
    private String            tableAlias;
    private String            remarks;
    private String            className;
    private String            javaProperty;
    private String            catalog          = null;
    private String            schema           = null;
    private List<Column>      baseColumns      = new ArrayList<Column>();
    private List<Column>      primaryKeys      = new ArrayList<Column>();
    private List<Key>         importedKeys     = new ArrayList<Key>();
    private List<Key>         exportedKeys     = new ArrayList<Key>();

    public Table(){
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        this.tableAlias = tableName;
        this.className = StringUtil.getCamelCaseString(tableAlias, true);
        this.javaProperty = StringUtil.getCamelCaseString(tableAlias, false);
    }

    public String getRemarks() {
        return remarks == null ? "" : remarks.trim();
    }

    public boolean isHasRemarks() {
        return StringUtil.isNotEmpty(remarks);
    }

    public String getRemarksUnicode() {
        return StringUtil.toUnicodeString(getRemarks());
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Column getColumn(String columnName) {
        for (Column column : primaryKeys) {
            if (column.getColumnName().equals(columnName)) {
                return column;
            }
        }
        for (Column column : baseColumns) {
            if (column.getColumnName().equals(columnName)) {
                return column;
            }
        }
        return null;
    }

    public List<Column> getColumns() {
        List<Column> allColumns = new ArrayList<Column>();
        allColumns.addAll(primaryKeys);
        allColumns.addAll(baseColumns);
        return allColumns;
    }

    public List<Column> getBaseColumns() {
        return baseColumns;
    }

    public void addBaseColumn(Column column) {
        this.baseColumns.add(column);
    }

    public List<Column> getPrimaryKeys() {
        return primaryKeys;
    }

    public void addPrimaryKey(Column primaryKeyColumn) {
        this.primaryKeys.add(primaryKeyColumn);
    }

    public String getJavaProperty() {
        return javaProperty;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
        this.className = StringUtil.getCamelCaseString(tableAlias, true);
        this.javaProperty = StringUtil.getCamelCaseString(tableAlias, false);
    }

    public boolean isHasDateColumn() {
        for (Column column : getColumns()) {
            if (column.isDate()) {
                return true;
            }
        }
        return false;
    }

    public boolean isHasBigDecimalColumn() {
        for (Column column : getColumns()) {
            if (column.isBigDecimal()) {
                return true;
            }
        }
        return false;
    }

    public boolean isHasNotNullColumn() {
        for (Column column : getColumns()) {
            if (!column.isNullable() && !column.isString()) {
                return true;
            }
        }
        return false;
    }

    public boolean isHasNotBlankColumn() {
        for (Column column : getColumns()) {
            if (!column.isNullable() && column.isString()) {
                return true;
            }
        }
        return false;
    }


    public List<Key> getImportedKeys() {
        return importedKeys;
    }


    public List<Key> getExportedKeys() {
        return exportedKeys;
    }

    public void addImportedKey(Key importedKey) {
        this.importedKeys.add(importedKey);
    }

    public void addExportedKey(Key exportedKey) {
        this.exportedKeys.add(exportedKey);
    }
}
