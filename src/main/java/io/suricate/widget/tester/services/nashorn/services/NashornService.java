package io.suricate.widget.tester.services.nashorn.services;

import io.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import io.suricate.widget.tester.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The nashorn service
 */
@Service
public class NashornService {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NashornService.class);

    /**
     * Check if the given Nashorn request can be executed
     *
     * @param nashornRequest The nashorn request to check
     * @return True is it is ok, false otherwise
     */
    public boolean isNashornRequestExecutable(final NashornRequest nashornRequest) {
        if (!StringUtils.isNotEmpty(nashornRequest.getScript())) {
            LOGGER.debug("The widget instance {} has no script. Stopping Nashorn request execution",
                    nashornRequest.getProjectWidgetId());
            return false;
        }

        if (!JsonUtils.isValid(nashornRequest.getPreviousData())) {
            LOGGER.debug("The widget instance {} has bad formed previous data. Stopping Nashorn request execution",
                    nashornRequest.getProjectWidgetId());
            return false;
        }

        if (nashornRequest.getDelay() == null || nashornRequest.getDelay() < 0) {
            LOGGER.debug("The widget instance {} has no delay or delay is < 0. Stopping Nashorn request execution",
                    nashornRequest.getProjectWidgetId());
            return false;
        }

        return true;
    }
}
