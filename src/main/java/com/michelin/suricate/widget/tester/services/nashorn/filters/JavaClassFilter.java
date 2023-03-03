package com.michelin.suricate.widget.tester.services.nashorn.filters;

import com.michelin.suricate.widget.tester.services.nashorn.script.NashornWidgetScript;
import jdk.nashorn.api.scripting.ClassFilter;

public class JavaClassFilter implements ClassFilter {

    /**
     * Method used to authorize access to some Java class
     *
     * @param s class name to check
     * @return true is the class name is authorized, false otherwise
     */
    public boolean exposeToScripts(String s) {
        return s.compareTo(NashornWidgetScript.class.getName()) == 0;
    }
}
