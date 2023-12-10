package com.michelin.suricate.widget.tester.services.js.services;

import com.michelin.suricate.widget.tester.model.dto.js.JsExecutionDto;
import com.michelin.suricate.widget.tester.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Js execution service.
 */
@Slf4j
@Service
public class JsExecutionService {
    /**
     * Check if the given Js execution can be executed.
     *
     * @param jsExecutionDto The Js execution to check
     * @return True is it is ok, false otherwise
     */
    public boolean isJsExecutable(final JsExecutionDto jsExecutionDto) {
        if (!StringUtils.isNotEmpty(jsExecutionDto.getScript())) {
            log.debug("The widget instance {} has no script. Stopping JavaScript execution",
                jsExecutionDto.getProjectWidgetId());
            return false;
        }

        if (!JsonUtils.isValid(jsExecutionDto.getPreviousData())) {
            log.debug("The widget instance {} has bad formed previous data. Stopping JavaScript execution",
                jsExecutionDto.getProjectWidgetId());
            return false;
        }

        if (jsExecutionDto.getDelay() == null || jsExecutionDto.getDelay() < 0) {
            log.debug("The widget instance {} has no delay or delay is < 0. Stopping JavaScript execution",
                jsExecutionDto.getProjectWidgetId());
            return false;
        }

        return true;
    }
}
