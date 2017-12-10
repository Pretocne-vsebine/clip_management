package com.rso.streaming.clip.api.v1.health;

import com.rso.streaming.ententies.logic.RestConfig;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class ClipHealthCheck implements HealthCheck {

    @Inject
    private RestConfig restConfig;

    @Override
    public HealthCheckResponse call() {
        if(restConfig.getIsHealth()){
            return HealthCheckResponse.named(ClipHealthCheck.class.getSimpleName()).up().build();
        }
        return HealthCheckResponse.named(ClipHealthCheck.class.getSimpleName()).down().build();
    }
}
