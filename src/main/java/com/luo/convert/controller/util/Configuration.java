package com.luo.convert.controller.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by chenwc on 2015/12/31.
 */
public class Configuration {
	private static Logger logger = Logger.getLogger(Configuration.class);
    private static Map items = new HashMap();

    public Configuration() {
    }

    private static void loadConfig() {
        String CONFIG_FILE_NAME = "configuration.xml";
        File configFile = PropUtils.getPropFile(Configuration.class, CONFIG_FILE_NAME);
        if(configFile != null && configFile.exists() && configFile.isFile()) {
            FileInputStream is = null;

            try {
                is = new FileInputStream(configFile);
                SAXReader var21 = new SAXReader();
                Document document = var21.read(is);
                Element systemElement = document.getRootElement();
                List catList = systemElement.elements("category");
                Iterator catIter = catList.iterator();

                while(true) {
                    Element catElement;
                    String catName;
                    do {
                        if(!catIter.hasNext()) {
                            return;
                        }

                        catElement = (Element)catIter.next();
                        catName = catElement.attributeValue("name");
                    } while(StringUtils.isEmpty(catName));

                    List itemList = catElement.elements("item");
                    Iterator itemIter = itemList.iterator();

                    while(itemIter.hasNext()) {
                        Element itemElement = (Element)itemIter.next();
                        String itemName = itemElement.attributeValue("name");
                        String value = itemElement.attributeValue("value");
                        if(!StringUtils.isEmpty(itemName)) {
                            items.put(catName + "." + itemName, value);
                        }
                    }
                }
            } catch (Exception var22) {
                logger.error("", var22);
            } finally {
                if(is != null) {
                    try {
                        is.close();
                        is = null;
                    } catch (IOException var211) {
                        logger.error("", var211);
                    }
                }
            }
        }
    }

    public static String getString(String name) {
        String value = (String)items.get(name);
        return value == null?"":value;
    }

    public static String getString(String name, String defaultValue) {
        String value = (String)items.get(name);
        return value != null && value.length() > 0?value:defaultValue;
    }

    public static Long getLong(String name) {
        String value = getString(name);

        try {
            return Long.valueOf(Long.parseLong(value));
        } catch (NumberFormatException var3) {
            return Long.valueOf(0L);
        }
    }

    public static Long getLong(String name, Long defaultValue) {
        String value = getString(name);

        try {
            return Long.valueOf(Long.parseLong(value));
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static int getInt(String name) {
        String value = getString(name);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException var3) {
            return 0;
        }
    }

    public static int getInt(String name, int defaultValue) {
        String value = getString(name);

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException var4) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String name) {
        String value = getString(name);
        return Boolean.valueOf(value).booleanValue();
    }

    public static double getDouble(String name, double defaultValue) {
        String value = getString(name);

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException var5) {
            return defaultValue;
        }
    }

    public Map getItems() {
        return items;
    }

    public static void main(String[] args) {
    }

    static {
        loadConfig();
    }
}
