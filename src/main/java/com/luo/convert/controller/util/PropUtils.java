package com.luo.convert.controller.util;

import java.io.File;
import java.net.URL;

/**
 * Created by doublebean on 2015/7/23.
 */
public class PropUtils {
    public PropUtils() {
    }

    public static File getPropFile(Class cls, String propFile) {
        File f = null;

        try {
            ClassLoader e = cls.getClassLoader();
            URL url = e.getResource(propFile);
            if(url != null) {
                f = new File(url.getPath());
                if(f.exists() && f.isFile()) {
                    return f;
                }
            }

            Package pack = cls.getPackage();
            String curDir;
            String classpath;
            int index;
            if(pack != null) {
                curDir = pack.getName();
                classpath = "";
                if(curDir.indexOf(".") < 0) {
                    classpath = curDir + "/";
                } else {
                    int cps = 0;
                    boolean i = false;

                    for(index = curDir.indexOf("."); index != -1; index = curDir.indexOf(".", cps)) {
                        classpath = classpath + curDir.substring(cps, index) + "/";
                        cps = index + 1;
                    }

                    classpath = classpath + curDir.substring(cps) + "/";
                }

                url = e.getResource(classpath + propFile);
                if(url != null) {
                    f = new File(url.getPath());
                    if(f.exists() && f.isFile()) {
                        return f;
                    }
                }
            }

            curDir = System.getProperty("user.dir");
            f = new File(curDir, propFile);
            if(f.exists() && f.isFile()) {
                return f;
            }

            classpath = System.getProperty("java.class.path");
            String[] classpaths = classpath.split(System.getProperty("path.separator"));

            for(index = 0; index < classpaths.length; ++index) {
                f = new File(classpaths[index], propFile);
                if(f.exists() && f.isFile()) {
                    break;
                }
                f = null;
            }
        } catch (Exception var10) {
            var10.printStackTrace();
            f = null;
        }
        return f;
    }

}