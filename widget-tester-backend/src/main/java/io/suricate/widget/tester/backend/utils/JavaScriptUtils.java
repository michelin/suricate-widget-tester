package io.suricate.widget.tester.backend.utils;

import io.suricate.widget.tester.backend.services.nashorn.script.NashornWidgetScript;
import org.apache.commons.lang3.StringUtils;

public final class JavaScriptUtils {

    /**
     * "Packages." constant used in Javascript to call REST API
     */
    private static final String PACKAGES_LITERAL = "Packages.";

    /**
     * Regex to find loop in script
     */
    private static final String REGEX_LOOP = "\\)\\s*\\{";

    /**
     * Name of the variable used to store the widget data from previous execution
     */
    public static final String PREVIOUS_DATA_VARIABLE = "SURI_PREVIOUS";

    /**
     * Name of the variable used to store the widget instance ID
     */
    public static final String WIDGET_INSTANCE_ID_VARIABLE = "SURI_INSTANCE_ID";

    /**
     * Full path to the checkInterrupted Java method
     */
    private static final String INJECT_INTERRUPT_STRING = JavaScriptUtils.PACKAGES_LITERAL + NashornWidgetScript.class.getName() + ".checkInterrupted();";

    /**
     * Constructor
     */
    private JavaScriptUtils() { }

    /**
     * Method used to prepare Nashorn script and update path
     *
     * @param data javascript script
     * @return the script with all class path updated
     */
    public static String prepare(String data) {
        return injectInterrupt(
                StringUtils.trimToEmpty(data).replace(
                        JavaScriptUtils.PACKAGES_LITERAL, JavaScriptUtils.PACKAGES_LITERAL + NashornWidgetScript.class.getName() + "."));
    }

    /**
     * Method used to inject interruption in loop for javascript code
     *
     * @param data javascript code
     * @return the javascript code with interruption on it
     */
    public static String injectInterrupt(String data) {
        return StringUtils.trimToEmpty(data).replaceAll(REGEX_LOOP, "){" + INJECT_INTERRUPT_STRING);
    }
}
