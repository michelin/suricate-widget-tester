package com.michelin.suricate.widget.tester.utils;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Spring context utils.
 */
@Lazy(false)
@Component
public class SpringContextUtils implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    /**
     * Set application context.
     *
     * @param applicationContext The application context to set
     */
    @Override
    public void setApplicationContext(@NotNull final ApplicationContext applicationContext) {
        SpringContextUtils.applicationContext = applicationContext;
    }
}
