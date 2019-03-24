package de.iso.apps.web.rest.vm;

import ch.qos.logback.classic.Logger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * View Model object for storing a Logback logger.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoggerVM {

    private String name;

    private String level;

    public LoggerVM(Logger logger) {
        this.name = logger.getName();
        this.level = logger.getEffectiveLevel().toString();
    }


}
