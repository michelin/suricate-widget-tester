package io.suricate.widget.tester.services.nashorn.tasks;

import io.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import io.suricate.widget.tester.model.dto.nashorn.NashornResponse;
import io.suricate.widget.tester.model.dto.nashorn.WidgetVariableResponse;
import io.suricate.widget.tester.model.enums.NashornErrorTypeEnum;
import io.suricate.widget.tester.services.nashorn.filters.JavaClassFilter;
import io.suricate.widget.tester.utils.JavaScriptUtils;
import io.suricate.widget.tester.utils.JsonUtils;
import io.suricate.widget.tester.utils.PropertiesUtils;
import io.suricate.widget.tester.utils.exceptions.nashorn.RemoteException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.script.*;
import java.io.InterruptedIOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@Slf4j
public class NashornRequestWidgetExecutionAsyncTask implements Callable<NashornResponse> {
    private final NashornRequest nashornRequest;
    private final List<WidgetVariableResponse> widgetParameters;

    /**
     * Constructor
     *
     * @param nashornRequest          The Nashorn request
     * @param widgetParameters The widget parameters
     */
    public NashornRequestWidgetExecutionAsyncTask(NashornRequest nashornRequest, List<WidgetVariableResponse> widgetParameters) {
        this.nashornRequest = nashornRequest;
        this.widgetParameters = widgetParameters;
    }

    /**
     * Method automatically called by the scheduler after the given delay.
     * Convert the widget properties set by the user to a map. Then, decrypt
     * the secret properties and set default value to unset properties.
     *
     * Then, set the mandatory variables to the engine script.
     * - The widget properties
     * - The data of the previous widget execution
     * - The widget instance ID
     *
     * Compile the Javascript script of the widget, evaluate it and get
     * the JSON result
     *
     * @return The response from Nashorn execution
     */
    @Override
    public NashornResponse call() {
        log.debug("Executing the Nashorn request of the widget instance {}", nashornRequest.getProjectWidgetId());

        NashornResponse nashornResponse = new NashornResponse();
        nashornResponse.setLaunchDate(new Date());

        try {
            NashornScriptEngineFactory nashornScriptEngineFactory = new NashornScriptEngineFactory();

            // Restrict some java class
            ScriptEngine scriptEngine = nashornScriptEngineFactory.getScriptEngine(new JavaClassFilter());

            // Get widget parameters values set by the user
            Map<String, String> widgetProperties = PropertiesUtils.convertStringWidgetPropertiesToMap(nashornRequest.getProperties());

            // Set default value to widget properties
            setDefaultValueToWidgetProperties(widgetProperties);

            // Populate properties in the engine
            for (Map.Entry<String, String> entry : widgetProperties.entrySet()) {
                scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).put(entry.getKey().toUpperCase(), entry.getValue());
            }

            // Add the data of the previous execution
            scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).put(JavaScriptUtils.PREVIOUS_DATA_VARIABLE, nashornRequest.getPreviousData());

            // Add the project widget id (id of the widget instance)
            scriptEngine.getBindings(ScriptContext.ENGINE_SCOPE).put(JavaScriptUtils.WIDGET_INSTANCE_ID_VARIABLE, nashornRequest.getProjectWidgetId());

            try (StringWriter sw = new StringWriter()) {
                scriptEngine.getContext().setWriter(sw);

                // Compile the widget Javascript script
                CompiledScript widgetScript = ((Compilable) scriptEngine).compile(JavaScriptUtils.prepare(nashornRequest.getScript()));

                widgetScript.eval();

                // Result of the widget Javascript script
                String json = (String) ((Invocable) scriptEngine).invokeFunction("run");

                if (JsonUtils.isValid(json)) {
                    nashornResponse.setData(json);
                    nashornResponse.setLog(sw.toString());
                } else {
                    log.debug("The JSON response obtained after the execution of the Nashorn request of the widget instance {} is invalid", nashornRequest.getProjectWidgetId());
                    log.debug("The JSON response is: {}", json);

                    nashornResponse.setLog(sw + "\nThe JSON response is not valid - " + json);
                    nashornResponse.setError(nashornRequest.isAlreadySuccess() ? NashornErrorTypeEnum.ERROR : NashornErrorTypeEnum.FATAL);
                }
            }
        } catch (Exception exception) {
            Throwable rootCause = ExceptionUtils.getRootCause(exception);

            // Do not set logs during an interruption, as it is caused by a canceling
            // of the Nashorn request, the return Nashorn response will not be processed by the NashornRequestResultAsyncTask
            if (rootCause instanceof InterruptedIOException) {
                log.info("The execution of the widget instance {} has been interrupted", nashornRequest.getProjectWidgetId());
            } else {
                log.error("An error has occurred during the Nashorn request execution of the widget instance {}", nashornRequest.getProjectWidgetId(), exception);

              if (isFatalError(exception, rootCause)) {
                nashornResponse.setError(NashornErrorTypeEnum.FATAL);
              } else {
                nashornResponse.setError(NashornErrorTypeEnum.ERROR);
              }

              nashornResponse.setLog(ExceptionUtils.getRootCauseMessage(exception));
            }
        } finally {
            nashornResponse.setProjectId(nashornRequest.getProjectId());
            nashornResponse.setProjectWidgetId(nashornRequest.getProjectWidgetId());
        }

        return nashornResponse;
    }

    /**
     * Set the unset variables in the map properties
     *
     * @param widgetProperties        The widget properties
     */
    private void setDefaultValueToWidgetProperties(Map<String, String> widgetProperties) {
        if (widgetParameters != null) {
            for (WidgetVariableResponse widgetVariableResponse : widgetParameters) {
                if (!widgetProperties.containsKey(widgetVariableResponse.getName())) {
                    if (!widgetVariableResponse.isRequired()) {
                        widgetProperties.put(widgetVariableResponse.getName(), null);
                    } else {
                        widgetProperties.put(widgetVariableResponse.getName(), widgetVariableResponse.getDefaultValue());
                    }
                }
            }
        }
    }

    /**
     * Check if the returned error is fatal
     *
     * @param e         The exception thrown
     * @param rootCause The root cause of the exception
     * @return true if the error is fatal, false otherwise
     */
    protected boolean isFatalError(Exception e, Throwable rootCause) {
        return !(rootCause instanceof RemoteException
                || StringUtils.containsIgnoreCase(ExceptionUtils.getMessage(e), "timeout")
                || rootCause instanceof UnknownHostException
                || nashornRequest.isAlreadySuccess()
        );
    }
}
