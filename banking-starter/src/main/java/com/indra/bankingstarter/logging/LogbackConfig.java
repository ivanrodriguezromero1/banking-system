package com.indra.bankingstarter.logging;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogbackConfig {

    @Bean
    public void configureLogback() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%X{traceId}] %-5level %logger{36} - %msg%n");
        encoder.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setContext(loggerContext);
        consoleAppender.setEncoder(encoder);
        consoleAppender.start();

        RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<>();
        fileAppender.setContext(loggerContext);
        fileAppender.setFile("logs/app.log");

        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setFileNamePattern("logs/app.%d{yyyy-MM-dd}.log");
        rollingPolicy.setMaxHistory(30);
        rollingPolicy.setParent(fileAppender); // parent and context required
        rollingPolicy.start();

        fileAppender.setRollingPolicy(rollingPolicy);
        fileAppender.setEncoder(encoder);
        fileAppender.start();

        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(loggerContext);
        asyncAppender.addAppender(consoleAppender);
        asyncAppender.addAppender(fileAppender);
        asyncAppender.start();

        ch.qos.logback.classic.Logger rootLogger = loggerContext.getLogger("ROOT");
        rootLogger.addAppender(asyncAppender);
    }
}
