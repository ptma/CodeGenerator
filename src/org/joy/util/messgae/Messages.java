package org.joy.util.messgae;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {

    private static final String         BUNDLE_NAME     = "org.joy.util.messgae.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    public static final String RUNTIME_ERROR_0 = "RuntimeError.0";
    public static final String RUNTIME_ERROR_1 = "RuntimeError.1";
    public static final String RUNTIME_ERROR_2 = "RuntimeError.2";
    public static final String RUNTIME_ERROR_3 = "RuntimeError.3";
    public static final String RUNTIME_ERROR_4 = "RuntimeError.4";
    public static final String RUNTIME_ERROR_5 = "RuntimeError.5";

    private Messages(){
    }

    public static String getString(String key) {
        return RESOURCE_BUNDLE.getString(key);
    }

    public static String getString(String key, String parm1) {
        return MessageFormat.format(RESOURCE_BUNDLE.getString(key), new Object[] { parm1 });
    }

    public static String getString(String key, String parm1, String parm2) {
        return MessageFormat.format(RESOURCE_BUNDLE.getString(key), new Object[] { parm1, parm2 });
    }

    public static String getString(String key, String parm1, String parm2, String parm3) {
        return MessageFormat.format(RESOURCE_BUNDLE.getString(key), new Object[] { parm1, parm2, parm3 });
    }
}
