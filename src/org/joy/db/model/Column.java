package org.joy.db.model;

import org.joy.db.model.util.JavaTypeResolver;
import org.joy.util.StringUtil;

public class Column implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 241462432312500261L;

    private String            columnName;

    private boolean           isPrimaryKey;

    private boolean           isForeignKey;
    private String            targetTableName;
    private String            targetColumnName;

    private int               size;

    private int               decimalDigits;

    private boolean           nullable;

    private boolean           unique;

    private String            defaultValue;

    private String            remarks;

    protected int             jdbcType;
    protected String          jdbcTypeName;

    private String            javaProperty;
    private String            javaType;
    private String            fullJavaType;
    private String            editor;

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
        return remarks == null ? "" : remarks;
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

    public boolean isFloat() {
        return JavaTypeResolver.isFloat(javaType);
    }

    public boolean isInteger() {
        return JavaTypeResolver.isInteger(javaType);
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

        return "BINARY".equals(typeName) || "BLOB".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
               || "CLOB".equals(typeName) || "LONGVARBINARY".equals(typeName) //$NON-NLS-1$ //$NON-NLS-2$
               || "LONGVARCHAR".equals(typeName) || "VARBINARY".equals(typeName); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean isPrimaryKey) {
        this.isPrimaryKey = isPrimaryKey;
    }

    public boolean isForeignKey() {
        return isForeignKey;
    }

    public void setForeignKey(boolean isForeignKey) {
        this.isForeignKey = isForeignKey;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    public String getGetterMethodName() {
        if (JavaTypeResolver.isBoolean(javaType)) {
            return "is" + StringUtil.getCamelCaseString(columnName, true);
        } else {
            return "get" + StringUtil.getCamelCaseString(columnName, true);
        }
    }

    public String getSetterMethodName() {
        return "set" + StringUtil.getCamelCaseString(columnName, true);
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

    public String getTargetTableName() {
        return targetTableName;
    }


    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName;
    }


    public String getTargetColumnName() {
        return targetColumnName;
    }


    public void setTargetColumnName(String targetColumnName) {
        this.targetColumnName = targetColumnName;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
