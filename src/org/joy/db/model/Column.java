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

import java.io.Serializable;

import org.joy.db.model.util.JavaTypeResolver;
import org.joy.util.StringUtil;

public class Column implements Serializable {

    private static final long serialVersionUID = 6987289314682844881L;

    private String            columnName;

    private boolean           primaryKey;

    private boolean           foreignKey;

    private int               size;

    private int               decimalDigits;

    private boolean           nullable;

    private boolean           unique;

    private boolean           indexed;

    private boolean           autoincrement;

    private String            defaultValue;

    private String            remarks;

    protected int             jdbcType;
    protected String          jdbcTypeName;

    private String            javaProperty;
    private String            javaType;
    private String            fullJavaType;
    private String            editor;
    private boolean           display          = true;
    private boolean           searchable;
    private String            dict = "";

    public Column(String columnName){
        this.columnName = columnName;
        this.javaProperty = StringUtil.getCamelCaseString(columnName, false);
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public String getDefaultValue() {
        return defaultValue == null ? "" : defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemarks() {
        return remarks == null ? "" : remarks.trim();
    }

    public boolean isHasRemarks() {
        return StringUtil.isNotEmpty(remarks);
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarksUnicode() {
        return StringUtil.toUnicodeString(getRemarks());
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaProperty() {
        return javaProperty;
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJdbcTypeName() {
        return jdbcTypeName;
    }

    public void setJdbcTypeName(String jdbcTypeName) {
        this.jdbcTypeName = jdbcTypeName;
    }

    public boolean isString() {
        return JavaTypeResolver.isString(javaType);
    }

    public boolean isFloatNumber() {
        return JavaTypeResolver.isFloat(javaType) || JavaTypeResolver.isDouble(javaType)
               || JavaTypeResolver.isBigDecimal(javaType) || JavaTypeResolver.isBigInteger(javaType);
    }

    public boolean isIntegerNumber() {
        return JavaTypeResolver.isByte(javaType) || JavaTypeResolver.isShort(javaType)
               || JavaTypeResolver.isInteger(javaType) || JavaTypeResolver.isLong(javaType);
    }

    public boolean isBigDecimal() {
        return JavaTypeResolver.isBigDecimal(javaType);
    }

    public boolean isBoolean() {
        return JavaTypeResolver.isBoolean(javaType);
    }

    public boolean isDate() {
        return JavaTypeResolver.isDate(javaType);
    }

    public boolean isBLOB() {
        String typeName = getJdbcTypeName();
        boolean isBlob = "BINARY".equals(typeName);
        isBlob = isBlob || "BLOB".equals(typeName);
        isBlob = isBlob || "CLOB".equals(typeName);
        isBlob = isBlob || "LONGVARBINARY".equals(typeName);
        isBlob = isBlob || "LONGVARCHAR".equals(typeName);
        isBlob = isBlob || "VARBINARY".equals(typeName);
        return isBlob;
    }

    public boolean isPrimitiveType(){
        //int, double, float, long, short, boolean, byte, char
        return JavaTypeResolver.isInteger(javaType)
            || JavaTypeResolver.isDouble(javaType)
            || JavaTypeResolver.isFloat(javaType)
            || JavaTypeResolver.isLong(javaType)
            || JavaTypeResolver.isShort(javaType)
            || JavaTypeResolver.isBoolean(javaType)
            || JavaTypeResolver.isByte(javaType);
    }

    public String getJavaPrimitiveType() {
        if (JavaTypeResolver.isInteger(javaType)){
            return "int";
        } else if (JavaTypeResolver.isDouble(javaType)){
            return "double";
        } else if (JavaTypeResolver.isFloat(javaType)){
            return "float";
        } else if (JavaTypeResolver.isLong(javaType)){
            return "long";
        } else if (JavaTypeResolver.isShort(javaType)){
            return "short";
        } else if (JavaTypeResolver.isBoolean(javaType)){
            return "boolean";
        } else if (JavaTypeResolver.isByte(javaType)){
            return "byte";
        } else {
            return javaType;
        }
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public String getGetterMethodName() {
        StringBuilder sb = new StringBuilder();

        sb.append(javaProperty);
        if (Character.isLowerCase(sb.charAt(0)) && ((sb.length() == 1) || Character.isLowerCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        if (JavaTypeResolver.isBoolean(javaType)) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }

        return sb.toString();
    }

    public String getSetterMethodName() {
        StringBuilder sb = new StringBuilder();

        sb.append(javaProperty);
        if (Character.isLowerCase(sb.charAt(0)) && ((sb.length() == 1) || Character.isLowerCase(sb.charAt(1)))) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }

        sb.insert(0, "set");

        return sb.toString();
    }

    public String getFullJavaType() {
        return fullJavaType;
    }

    public void setFullJavaType(String fullJavaType) {
        this.fullJavaType = fullJavaType;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public boolean isAutoincrement() {
        return autoincrement;
    }

    public void setAutoincrement(boolean autoincrement) {
        this.autoincrement = autoincrement;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public String getDict() {
        return dict;
    }

    public boolean hasDict() {
        return StringUtil.isNotEmpty(dict);
    }
    public void setDict(String dict) {
        this.dict = dict;
    }

}
