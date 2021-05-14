package io.suricate.widget.tester.services.nashorn.services;

import io.suricate.widget.tester.model.dto.nashorn.NashornRequest;
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
      if (!nashornRequest.isValid()) {
        LOGGER.debug("The Nashorn request is not valid for the widget instance: {}", nashornRequest.getProjectWidgetId());
        return false;
      }

      if (nashornRequest.getDelay() < 0) {
        LOGGER.debug("The Nashorn request has a delay < 0 for widget instance: {}", nashornRequest.getProjectWidgetId());
        return false;
      }

      return true;
    }
}
