package de.iso.apps.cucumber;

import cucumber.api.java.Before;
import de.iso.apps.EfwgatewayApp;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest @WebAppConfiguration @ContextConfiguration(classes = EfwgatewayApp.class)
public class CucumberContextConfiguration {
    
    @Before
    public void setup_cucumber_spring_context() {
        // Dummy method so cucumber will recognize this class as glue
        // and use its context configuration.
    }
    
}
