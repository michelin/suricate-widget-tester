package io.suricate.widget.tester.services.nashorn.services;

import io.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import io.suricate.widget.tester.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NashornService {
    /**
     * Check if the given Nashorn request can be executed
     * @param nashornRequest The nashorn request to check
     * @return True is it is ok, false otherwise
     */
    public boolean isNashornRequestExecutable(final NashornRequest nashornRequest) {
        if (!StringUtils.isNotEmpty(nashornRequest.getScript())) {
            log.debug("The widget instance {} has no script. Stopping Nashorn request execution",
                    nashornRequest.getProjectWidgetId());
            return false;
        }

        if (!JsonUtils.isValid(nashornRequest.getPreviousData())) {
            log.debug("The widget instance {} has bad formed previous data. Stopping Nashorn request execution",
                    nashornRequest.getProjectWidgetId());
            return false;
        }

        if (nashornRequest.getDelay() == null || nashornRequest.getDelay() < 0) {
            log.debug("The widget instance {} has no delay or delay is < 0. Stopping Nashorn request execution",
                    nashornRequest.getProjectWidgetId());
            return false;
        }

        return true;
    }
}
