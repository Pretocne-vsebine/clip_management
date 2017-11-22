package com.rso.streaming.ententies.logic;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("rest-properties")
public class RestConfig {

        @ConfigValue(value = "external-dependencies.clip-service.write-enabled", watch = true)
        private boolean writeEnabled;

        public boolean isWriteEnabled() {
            return writeEnabled;
        }
}
