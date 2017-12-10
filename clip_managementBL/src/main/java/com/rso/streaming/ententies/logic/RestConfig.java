package com.rso.streaming.ententies.logic;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestConfig {

    @ConfigValue(value = "external-dependencies.clip-service.write-enabled", watch = true)
    private boolean writeEnabled;

    @ConfigValue(value = "external-dependencies.clip-service.is-healthy", watch = true)
    private boolean health;

    public boolean getWriteEnabled() {
        return writeEnabled;
    }

    public void setWriteEnabled(boolean writeEnabled) {
        this.writeEnabled = writeEnabled;
    }

    public boolean getIsHealth() {
        return health;
    }

    public void setHealth(boolean health) {
        this.health = health;
    }
}
