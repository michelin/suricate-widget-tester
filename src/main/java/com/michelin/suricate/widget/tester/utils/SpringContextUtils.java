package com.michelin.suricate.widget.tester.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy(false)
@Component
public class SpringContextUtils implements ApplicationContextAware {
    @Getter
    private static ApplicationContext applicationContext;

    /**
     * Set application context
     *
     * @param applicationContext The application context to set
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
