package de.iso.apps.web.rest.vm;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.client.ServiceInstance;

import java.util.List;

/**
 * View Model that stores a route managed by the Gateway.
 */
@Getter @Setter public class RouteVM {
    
    private String path;
    
    private String serviceId;
    
    private List<ServiceInstance> serviceInstances;
    
}
