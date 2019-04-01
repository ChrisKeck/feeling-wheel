package de.iso.apps.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of FeelingSearchRepository to test the application without starting Elasticsearch.
 */
@Configuration public class FeelingSearchRepositoryMockConfiguration {
    
    @MockBean private FeelingSearchRepository mockFeelingSearchRepository;
    
}
