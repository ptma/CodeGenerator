package org.joy.util.messgae;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {

    private static final String         BUNDLE_NAME     = "org.joy.util.messgae.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

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
