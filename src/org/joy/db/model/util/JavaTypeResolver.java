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
package org.joy.db.model.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joy.db.model.Column;

public class JavaTypeResolver {

    private static boolean              forceBigDecimals;

    private static Map<Integer, String> typeMap;
    private static Map<Integer, String> fullTypeMap;

    private static final String         TYPE_BYTE            = "Byte";
    private static final String         TYPE_SHORT           = "Short";
    private static final String         TYPE_INTEGER         = "Integer";
    private static final String         TYPE_LONG            = "Long";
    private static final String         TYPE_FLOAT           = "Float";
    private static final String         TYPE_DOUBLE          = "Double";
    private static final String         TYPE_STRING          = "String";
    private static final String         TYPE_BOOLEAN         = "Boolean";
    private static final String         TYPE_OBJECT          = "Object";
    private static final String         TYPE_BYTE_ARRAY      = "byte[]";
    private static final String         TYPE_BIGDECIMAL      = "BigDecimal";

    private static final String         FULL_TYPE_BYTE       = "java.lang.Byte";
    private static final String         FULL_TYPE_SHORT      = "java.lang.Short";
    private static final String         FULL_TYPE_INTEGER    = "java.lang.Integer";
    private static final String         FULL_TYPE_LONG       = "java.lang.Long";
    private static final String         FULL_TYPE_FLOAT      = "java.lang.Float";
    private static final String         FULL_TYPE_DOUBLE     = "java.lang.Double";
    private static final String         FULL_TYPE_STRING     = "java.lang.String";
    private static final String         FULL_TYPE_BOOLEAN    = "java.lang.Boolean";
    private static final String         FULL_TYPE_OBJECT     = "java.lang.Object";
    private static final String         FULL_TYPE_DATE       = "java.util.Date";
    private static final String         FULL_TYPE_BIGDECIMAL = "java.math.BigDecimal";

    static {
        typeMap = new HashMap<Integer, String>();

        typeMap.put(Types.ARRAY, TYPE_OBJECT);
        typeMap.put(Types.BIGINT, TYPE_LONG);
        typeMap.put(Types.BINARY, TYPE_BYTE_ARRAY);
        typeMap.put(Types.BIT, TYPE_BOOLEAN);
        typeMap.put(Types.BLOB, TYPE_BYTE_ARRAY);
        typeMap.put(Types.BOOLEAN, TYPE_BOOLEAN);
        typeMap.put(Types.CHAR, TYPE_STRING);
        typeMap.put(Types.CLOB, TYPE_STRING);
        typeMap.put(Types.DATALINK, TYPE_STRING);
        typeMap.put(Types.DATE, "Date");
        typeMap.put(Types.DISTINCT, TYPE_OBJECT);
        typeMap.put(Types.DOUBLE, TYPE_DOUBLE);
        typeMap.put(Types.FLOAT, TYPE_DOUBLE);
        typeMap.put(Types.INTEGER, TYPE_INTEGER);
        typeMap.put(Types.JAVA_OBJECT, TYPE_OBJECT);
        typeMap.put(Jdbc4Types.LONGNVARCHAR, TYPE_STRING);
        typeMap.put(Types.LONGVARBINARY, TYPE_BYTE_ARRAY);
        typeMap.put(Types.LONGVARCHAR, TYPE_STRING);
        typeMap.put(Jdbc4Types.NCHAR, TYPE_STRING);
        typeMap.put(Jdbc4Types.NCLOB, TYPE_STRING);
        typeMap.put(Jdbc4Types.NVARCHAR, TYPE_STRING);
        typeMap.put(Types.NULL, TYPE_OBJECT);
        typeMap.put(Types.OTHER, TYPE_OBJECT);
        typeMap.put(Types.REAL, TYPE_FLOAT);
        typeMap.put(Types.REF, TYPE_OBJECT);
        typeMap.put(Types.SMALLINT, TYPE_SHORT);
        typeMap.put(Types.STRUCT, TYPE_OBJECT);
        typeMap.put(Types.TIME, "Date");
        typeMap.put(Types.TIMESTAMP, "Date");
        typeMap.put(Types.TINYINT, TYPE_BYTE);
        typeMap.put(Types.VARBINARY, TYPE_BYTE_ARRAY);
        typeMap.put(Types.VARCHAR, TYPE_STRING);

        fullTypeMap = new HashMap<Integer, String>();

        fullTypeMap.put(Types.ARRAY, FULL_TYPE_OBJECT);
        fullTypeMap.put(Types.BIGINT, FULL_TYPE_LONG);
        fullTypeMap.put(Types.BINARY, TYPE_BYTE_ARRAY);
        fullTypeMap.put(Types.BIT, FULL_TYPE_BOOLEAN);
        fullTypeMap.put(Types.BLOB, TYPE_BYTE_ARRAY);
        fullTypeMap.put(Types.BOOLEAN, FULL_TYPE_BOOLEAN);
        fullTypeMap.put(Types.CHAR, FULL_TYPE_STRING);
        fullTypeMap.put(Types.CLOB, FULL_TYPE_STRING);
        fullTypeMap.put(Types.DATALINK, FULL_TYPE_STRING);
        fullTypeMap.put(Types.DATE, FULL_TYPE_DATE);
        fullTypeMap.put(Types.DISTINCT, FULL_TYPE_OBJECT);
        fullTypeMap.put(Types.DOUBLE, FULL_TYPE_DOUBLE);
        fullTypeMap.put(Types.FLOAT, FULL_TYPE_DOUBLE);
        fullTypeMap.put(Types.INTEGER, FULL_TYPE_INTEGER);
        fullTypeMap.put(Types.JAVA_OBJECT, FULL_TYPE_OBJECT);
        fullTypeMap.put(Jdbc4Types.LONGNVARCHAR, FULL_TYPE_STRING);
        fullTypeMap.put(Types.LONGVARBINARY, TYPE_BYTE_ARRAY);
        fullTypeMap.put(Types.LONGVARCHAR, FULL_TYPE_STRING);
        fullTypeMap.put(Jdbc4Types.NCHAR, FULL_TYPE_STRING);
        fullTypeMap.put(Jdbc4Types.NCLOB, FULL_TYPE_STRING);
        fullTypeMap.put(Jdbc4Types.NVARCHAR, FULL_TYPE_STRING);
        fullTypeMap.put(Types.NULL, FULL_TYPE_OBJECT);
        fullTypeMap.put(Types.OTHER, FULL_TYPE_OBJECT);
        fullTypeMap.put(Types.REAL, FULL_TYPE_FLOAT);
        fullTypeMap.put(Types.REF, FULL_TYPE_OBJECT);
        fullTypeMap.put(Types.SMALLINT, FULL_TYPE_SHORT);
        fullTypeMap.put(Types.STRUCT, FULL_TYPE_OBJECT);
        fullTypeMap.put(Types.TIME, FULL_TYPE_DATE);
        fullTypeMap.put(Types.TIMESTAMP, FULL_TYPE_DATE);
        fullTypeMap.put(Types.TINYINT, FULL_TYPE_BYTE);
        fullTypeMap.put(Types.VARBINARY, TYPE_BYTE_ARRAY);
        fullTypeMap.put(Types.VARCHAR, FULL_TYPE_STRING);
    }

    private JavaTypeResolver(){
    }

    public static String calculateJavaType(Column column) {
        String answer;
        String javaType = typeMap.get(column.getJdbcType());

        if (javaType == null) {
            switch (column.getJdbcType()) {
                case Types.DECIMAL:
                case Types.NUMERIC:
                    answer = calculateNumericType(column);
                    break;
                default:
                    answer = null;
                    break;
            }
        } else {
            answer = javaType;
        }

        return answer;
    }

    private static String calculateNumericType(Column column) {
        if (column.getDecimalDigits() > 0 || column.getSize() > 18 || forceBigDecimals) {
            return TYPE_BIGDECIMAL;
        } else if (column.getSize() > 9) {
            return TYPE_LONG;
        } else if (column.getSize() > 4) {
            return TYPE_INTEGER;
        } else {
            return TYPE_SHORT;
        }
    }

    public static String calculateFullJavaType(Column column) {
        String answer;
        String javaType = fullTypeMap.get(column.getJdbcType());

        if (javaType == null) {
            switch (column.getJdbcType()) {
                case Types.DECIMAL:
                case Types.NUMERIC:
                    answer = calculateFullNumericType(column);
                    break;

                default:
                    answer = null;
                    break;
            }
        } else {
            answer = javaType;
        }

        return answer;
    }

    private static String calculateFullNumericType(Column column) {
        if (column.getDecimalDigits() > 0 || column.getSize() > 18 || forceBigDecimals) {
            return FULL_TYPE_BIGDECIMAL;
        } else if (column.getSize() > 9) {
            return FULL_TYPE_LONG;
        } else if (column.getSize() > 4) {
            return FULL_TYPE_INTEGER;
        } else {
            return FULL_TYPE_SHORT;
        }
    }

    public static String[] getAllJavaTypes() {
        Set<String> javaTypeSet = new HashSet<String>();
        javaTypeSet.addAll(typeMap.values());

        String[] values = new String[javaTypeSet.size() + 1];
        int index = 0;
        for (String itemValue : javaTypeSet) {
            values[index++] = itemValue;
        }
        values[index++] = TYPE_BIGDECIMAL;
        return values;
    }

    public static boolean isDouble(String javaType) {
        return "double".equals(javaType) || TYPE_DOUBLE.equals(javaType) || FULL_TYPE_DOUBLE.equals(javaType);
    }

    public static boolean isFloat(String javaType) {
        return "float".equals(javaType) || TYPE_FLOAT.equals(javaType) || FULL_TYPE_FLOAT.equals(javaType);
    }

    public static boolean isLong(String javaType) {
        return "long".equals(javaType) || TYPE_LONG.equals(javaType) || FULL_TYPE_LONG.equals(javaType);
    }

    public static boolean isShort(String javaType) {
        return "short".equals(javaType) || TYPE_SHORT.equals(javaType) || FULL_TYPE_SHORT.equals(javaType);
    }

    public static boolean isByte(String javaType) {
        return "byte".equals(javaType) || TYPE_BYTE.equals(javaType) || FULL_TYPE_BYTE.equals(javaType);
    }

    public static boolean isChar(String javaType) {
        return "char".equals(javaType) || TYPE_BYTE.equals(javaType) || FULL_TYPE_BYTE.equals(javaType);
    }

    public static boolean isBigDecimal(String javaType) {
        return FULL_TYPE_BIGDECIMAL.equals(javaType);
    }

    public static boolean isBigInteger(String javaType) {
        return "java.math.BigInteger".equals(javaType);
    }

    public static boolean isInteger(String javaType) {
        return "int".equals(javaType) || TYPE_INTEGER.equals(javaType) || FULL_TYPE_INTEGER.equals(javaType);
    }

    public static boolean isDate(String javaType) {
        return javaType.endsWith("Date") || javaType.endsWith("Timestamp") || javaType.endsWith("Time");
    }

    public static boolean isString(String javaType) {
        return FULL_TYPE_STRING.equals(javaType) || "String".equals(javaType);
    }

    public static boolean isBoolean(String javaType) {
        return "boolean".equals(javaType) || TYPE_BOOLEAN.equals(javaType) || FULL_TYPE_BOOLEAN.equals(javaType);
    }
}
