CodeGenerator
====
这是一个使用 Freemarker 和 Velocity 模板来生代码的工具。

本生成器只是将数据库中的表结构数据提取出来，然后将这份结构模型提交给模板引擎, 根据你自定义的模板生成你需要的代码。

##配置文件
```xml
    <?xml version="1.0" encoding="utf-8" standalone="no"?>
    <configuration>
        <classpath>
            <!-- 引入JDBC驱动，手动添加 -->
            <entry>lib/mysql-connector-java-3.1.7-bin.jar</entry>
        </classpath>
        <!-- 代码生成目标基准目录 -->
        <tagertProject>D:\temp</tagertProject>
        <!-- 基本包 -->
        <basePackage>org.joy</basePackage>
        <!-- 模块名 -->
        <moduleName>test</moduleName>
        <templates>
            <!-- 模板 ，手动添加 -->
            <template name="实体类" engine="freemarker">
                <!-- 模板文件 -->
                <templateFile>./entity.ftl</templateFile>
                <!-- 文件路径 -->
                <targetPath>${tagertProject}\src\${basePackage}\${moduleName}\entity\</targetPath>
                <!-- 文件名 -->
                <targetFileName>${table.className}.java</targetFileName>
                <!-- 文件编码 -->
                <fileEncoding>UTF-8</fileEncoding>
            </template>
        </templates>
    </configuration>
```

##模板中可用的变量和方法
### table 对象
####属性
    String       tableName           表名

	String       tableType           表类型

	String       tableAlias          表别名

	String       remarks             表注释

	String       remarksUnicode      表注释转Unicode后的字符串

	String       className           实体类名

	String       javaProperty        实体类作为属性时的名字 == ${table.className?uncap_first}

	List<Column> primaryKeys         主键集

	List<Column> baseColumns         基本字段集

	List<Column> columns             所有字段  == primaryKeys + baseColumns

	boolean      hasDateColumn       是否有日期类型字段

	boolean      hasBigDecimalColumn 是否有 BigDecimal 字段

### column
####属性
	String	columnName       字段名

	boolean isPrimaryKey     是否为主键

	boolean isForeignKey     是否为外键

	int     size             字段长度

	int     decimalDigits    小数位长度

	boolean nullable         是否可空

	boolean unique           是否唯一值

	String  defaultValue     默认值

	String  remarks          注释

	String  remarksUnicode   数字转Unicode后的字符串

	int     jdbcType         对应 java.sql.Types

	String  jdbcTypeName     对应jdbcType的名称

	String  javaProperty     属性名

	String  javaType         java类型，比如String

	String  fullJavaType     完整的Java类型，比如 java.lang.String

	String  getterMethodName Get方法名

	String  setterMethodName Set方法名

####方法
	boolean isString()      是否是字符串

	boolean isFloat()       是否是浮点类型

	boolean isInteger()     是否是整形

	boolean isBigDecimal()  是否是BigDecimal类型

	boolean isBoolean()     是否是布尔类型

	boolean isDate()        是否有日期类型，含Date、Timestamp、Time

	public boolean isBLOB() 是否有 BLOB、CLOB、LONGVARCHAR、LONGVARBINARY或VARBINARY

##作者
ptma@163.com
