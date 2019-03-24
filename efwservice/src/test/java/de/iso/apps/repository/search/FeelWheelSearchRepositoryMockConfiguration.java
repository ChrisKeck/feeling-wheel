package de.iso.apps.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of FeelWheelSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FeelWheelSearchRepositoryMockConfiguration {

    @MockBean
    private FeelWheelSearchRepository mockFeelWheelSearchRepository;

}
