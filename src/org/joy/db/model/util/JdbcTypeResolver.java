package org.joy.db.model.util;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class JdbcTypeResolver {

    private static Map<Integer, String> typeToName;
    private static Map<String, Integer> nameToType;

    static {
        typeToName = new HashMap<Integer, String>();
        typeToName.put(Types.ARRAY, "ARRAY");
        typeToName.put(Types.BIGINT, "BIGINT");
        typeToName.put(Types.BINARY, "BINARY");
        typeToName.put(Types.BIT, "BIT");
        typeToName.put(Types.BLOB, "BLOB");
        typeToName.put(Types.BOOLEAN, "BOOLEAN");
        typeToName.put(Types.CHAR, "CHAR");
        typeToName.put(Types.CLOB, "CLOB");
        typeToName.put(Types.DATALINK, "DATALINK");
        typeToName.put(Types.DATE, "DATE");
        typeToName.put(Types.DECIMAL, "DECIMAL");
        typeToName.put(Types.DISTINCT, "DISTINCT");
        typeToName.put(Types.DOUBLE, "DOUBLE");
        typeToName.put(Types.FLOAT, "FLOAT");
        typeToName.put(Types.INTEGER, "INTEGER");
        typeToName.put(Types.JAVA_OBJECT, "JAVA_OBJECT");
        typeToName.put(Types.LONGVARBINARY, "LONGVARBINARY");
        typeToName.put(Types.LONGVARCHAR, "LONGVARCHAR");
        typeToName.put(Jdbc4Types.NCHAR, "NCHAR");
        typeToName.put(Jdbc4Types.NCLOB, "NCLOB");
        typeToName.put(Jdbc4Types.NVARCHAR, "NVARCHAR");
        typeToName.put(Jdbc4Types.LONGNVARCHAR, "LONGNVARCHAR");
        typeToName.put(Types.NULL, "NULL");
        typeToName.put(Types.NUMERIC, "NUMERIC");
        typeToName.put(Types.OTHER, "OTHER");
        typeToName.put(Types.REAL, "REAL");
        typeToName.put(Types.REF, "REF");
        typeToName.put(Types.SMALLINT, "SMALLINT");
        typeToName.put(Types.STRUCT, "STRUCT");
        typeToName.put(Types.TIME, "TIME");
        typeToName.put(Types.TIMESTAMP, "TIMESTAMP");
        typeToName.put(Types.TINYINT, "TINYINT");
        typeToName.put(Types.VARBINARY, "VARBINARY");
        typeToName.put(Types.VARCHAR, "VARCHAR");

        nameToType = new HashMap<String, Integer>();
        nameToType.put("ARRAY", Types.ARRAY);
        nameToType.put("BIGINT", Types.BIGINT);
        nameToType.put("BINARY", Types.BINARY);
        nameToType.put("BIT", Types.BIT);
        nameToType.put("BLOB", Types.BLOB);
        nameToType.put("BOOLEAN", Types.BOOLEAN);
        nameToType.put("CHAR", Types.CHAR);
        nameToType.put("CLOB", Types.CLOB);
        nameToType.put("DATALINK", Types.DATALINK);
        nameToType.put("DATE", Types.DATE);
        nameToType.put("DECIMAL", Types.DECIMAL);
        nameToType.put("DISTINCT", Types.DISTINCT);
        nameToType.put("DOUBLE", Types.DOUBLE);
        nameToType.put("FLOAT", Types.FLOAT);
        nameToType.put("INTEGER", Types.INTEGER);
        nameToType.put("JAVA_OBJECT", Types.JAVA_OBJECT);
        nameToType.put("LONGVARBINARY", Types.LONGVARBINARY);
        nameToType.put("LONGVARCHAR", Types.LONGVARCHAR);
        nameToType.put("NCHAR", Jdbc4Types.NCHAR);
        nameToType.put("NCLOB", Jdbc4Types.NCLOB);
        nameToType.put("NVARCHAR", Jdbc4Types.NVARCHAR);
        nameToType.put("LONGNVARCHAR", Jdbc4Types.LONGNVARCHAR);
        nameToType.put("NULL", Types.NULL);
        nameToType.put("NUMERIC", Types.NUMERIC);
        nameToType.put("OTHER", Types.OTHER);
        nameToType.put("REAL", Types.REAL);
        nameToType.put("REF", Types.REF);
        nameToType.put("SMALLINT", Types.SMALLINT);
        nameToType.put("STRUCT", Types.STRUCT);
        nameToType.put("TIME", Types.TIME);
        nameToType.put("TIMESTAMP", Types.TIMESTAMP);
        nameToType.put("TINYINT", Types.TINYINT);
        nameToType.put("VARBINARY", Types.VARBINARY);
        nameToType.put("VARCHAR", Types.VARCHAR);
    }

    private JdbcTypeResolver(){
        super();
    }

    public static String getJdbcTypeName(int jdbcType) {
        String answer = typeToName.get(jdbcType);
        if (answer == null) {
            answer = "OTHER";
        }

        return answer;
    }

    public static int getJdbcType(String jdbcTypeName) {
        Integer answer = nameToType.get(jdbcTypeName);
        if (answer == null) {
            answer = Types.OTHER;
        }

        return answer;
    }
}
