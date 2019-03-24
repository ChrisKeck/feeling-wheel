package de.iso.apps.web.rest;

import de.iso.apps.EfwserviceApp;
import de.iso.apps.domain.FeelWheel;
import de.iso.apps.repository.FeelWheelRepository;
import de.iso.apps.repository.search.FeelWheelSearchRepository;
import de.iso.apps.repository.search.FeelWheelSearchRepositoryMockConfiguration;
import de.iso.apps.service.FeelWheelService;
import de.iso.apps.service.dto.FeelWheelDTO;
import de.iso.apps.service.mapper.FeelWheelMapper;
import de.iso.apps.web.rest.errors.ExceptionTranslator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static de.iso.apps.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the FeelWheelResource REST controller.
 *
 * @see FeelWheelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EfwserviceApp.class)
public class FeelWheelResourceIntTest {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Instant DEFAULT_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private FeelWheelRepository feelWheelRepository;

    @Autowired
    private FeelWheelMapper feelWheelMapper;

    @Autowired
    private FeelWheelService feelWheelService;

    /**
     * This repository is mocked in the de.iso.apps.repository.search test package.
     *
     * @see FeelWheelSearchRepositoryMockConfiguration
     */
    @Autowired
    private FeelWheelSearchRepository mockFeelWheelSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;
    
    @Qualifier("mvcValidator")
    @Autowired
    private Validator validator;

    private MockMvc restFeelWheelMockMvc;

    private FeelWheel feelWheel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeelWheelResource feelWheelResource = new FeelWheelResource(feelWheelService);
        this.restFeelWheelMockMvc = MockMvcBuilders.standaloneSetup(feelWheelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FeelWheel createEntity(EntityManager em) {
        return FeelWheel.builder()
                        .subject(DEFAULT_SUBJECT)
                        .from(DEFAULT_FROM)
                        .to(DEFAULT_TO)
                        .build();
    }

    @Before
    public void initTest() {
        feelWheel = createEntity(em);
    }

    @Test
    @Transactional
    public void createFeelWheel() throws Exception {
        int databaseSizeBeforeCreate = feelWheelRepository.findAll().size();

        // Create the FeelWheel
        FeelWheelDTO feelWheelDTO = feelWheelMapper.toDto(feelWheel);
        restFeelWheelMockMvc.perform(post("/api/feel-wheels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feelWheelDTO)))
            .andExpect(status().isCreated());

        // Validate the FeelWheel in the database
        List<FeelWheel> feelWheelList = feelWheelRepository.findAll();
        assertThat(feelWheelList).hasSize(databaseSizeBeforeCreate + 1);
        FeelWheel testFeelWheel = feelWheelList.get(feelWheelList.size() - 1);
        assertThat(testFeelWheel.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testFeelWheel.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testFeelWheel.getTo()).isEqualTo(DEFAULT_TO);

        // Validate the FeelWheel in Elasticsearch
        verify(mockFeelWheelSearchRepository, times(1)).save(testFeelWheel);
    }

    @Test
    @Transactional
    public void createFeelWheelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = feelWheelRepository.findAll().size();

        // Create the FeelWheel with an existing ID
        feelWheel.setId(1L);
        FeelWheelDTO feelWheelDTO = feelWheelMapper.toDto(feelWheel);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeelWheelMockMvc.perform(post("/api/feel-wheels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feelWheelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FeelWheel in the database
        List<FeelWheel> feelWheelList = feelWheelRepository.findAll();
        assertThat(feelWheelList).hasSize(databaseSizeBeforeCreate);

        // Validate the FeelWheel in Elasticsearch
        verify(mockFeelWheelSearchRepository, times(0)).save(feelWheel);
    }

    @Test
    @Transactional
    public void checkSubjectIsRequired() throws Exception {
        int databaseSizeBeforeTest = feelWheelRepository.findAll().size();
        // set the field null
        feelWheel.setSubject(null);

        // Create the FeelWheel, which fails.
        FeelWheelDTO feelWheelDTO = feelWheelMapper.toDto(feelWheel);

        restFeelWheelMockMvc.perform(post("/api/feel-wheels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feelWheelDTO)))
            .andExpect(status().isBadRequest());

        List<FeelWheel> feelWheelList = feelWheelRepository.findAll();
        assertThat(feelWheelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFeelWheels() throws Exception {
        // Initialize the database
        feelWheelRepository.saveAndFlush(feelWheel);

        // Get all the feelWheelList
        restFeelWheelMockMvc.perform(get("/api/feel-wheels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feelWheel.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT.toString())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())));
    }
    
    @Test
    @Transactional
    public void getFeelWheel() throws Exception {
        // Initialize the database
        feelWheelRepository.saveAndFlush(feelWheel);

        // Get the feelWheel
        restFeelWheelMockMvc.perform(get("/api/feel-wheels/{id}", feelWheel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(feelWheel.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT.toString()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM.toString()))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFeelWheel() throws Exception {
        // Get the feelWheel
        restFeelWheelMockMvc.perform(get("/api/feel-wheels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeelWheel() throws Exception {
        // Initialize the database
        feelWheelRepository.saveAndFlush(feelWheel);

        int databaseSizeBeforeUpdate = feelWheelRepository.findAll().size();

        // Update the feelWheel
        FeelWheel updatedFeelWheel = feelWheelRepository.findById(feelWheel.getId()).get();
        // Disconnect from session so that the updates on updatedFeelWheel are not directly saved in db
        em.detach(updatedFeelWheel);
        updatedFeelWheel = FeelWheel.builder()
                                    .subject(UPDATED_SUBJECT)
                                    .from(UPDATED_FROM)
                                    .to(UPDATED_TO)
                                    .employee(updatedFeelWheel.getEmployee())
                                    .feelings(updatedFeelWheel.getFeelings())
                                    .id(updatedFeelWheel.getId())
                                    .build();
        FeelWheelDTO feelWheelDTO = feelWheelMapper.toDto(updatedFeelWheel);

        restFeelWheelMockMvc.perform(put("/api/feel-wheels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feelWheelDTO)))
            .andExpect(status().isOk());

        // Validate the FeelWheel in the database
        List<FeelWheel> feelWheelList = feelWheelRepository.findAll();
        assertThat(feelWheelList).hasSize(databaseSizeBeforeUpdate);
        FeelWheel testFeelWheel = feelWheelList.get(feelWheelList.size() - 1);
        assertThat(testFeelWheel.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testFeelWheel.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testFeelWheel.getTo()).isEqualTo(UPDATED_TO);

        // Validate the FeelWheel in Elasticsearch
        verify(mockFeelWheelSearchRepository, times(1)).save(testFeelWheel);
    }

    @Test
    @Transactional
    public void updateNonExistingFeelWheel() throws Exception {
        int databaseSizeBeforeUpdate = feelWheelRepository.findAll().size();

        // Create the FeelWheel
        FeelWheelDTO feelWheelDTO = feelWheelMapper.toDto(feelWheel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeelWheelMockMvc.perform(put("/api/feel-wheels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(feelWheelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FeelWheel in the database
        List<FeelWheel> feelWheelList = feelWheelRepository.findAll();
        assertThat(feelWheelList).hasSize(databaseSizeBeforeUpdate);

        // Validate the FeelWheel in Elasticsearch
        verify(mockFeelWheelSearchRepository, times(0)).save(feelWheel);
    }

    @Test
    @Transactional
    public void deleteFeelWheel() throws Exception {
        // Initialize the database
        feelWheelRepository.saveAndFlush(feelWheel);

        int databaseSizeBeforeDelete = feelWheelRepository.findAll().size();

        // Delete the feelWheel
        restFeelWheelMockMvc.perform(delete("/api/feel-wheels/{id}", feelWheel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FeelWheel> feelWheelList = feelWheelRepository.findAll();
        assertThat(feelWheelList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the FeelWheel in Elasticsearch
        verify(mockFeelWheelSearchRepository, times(1)).deleteById(feelWheel.getId());
    }

    @Test
    @Transactional
    public void searchFeelWheel() throws Exception {
        // Initialize the database
        feelWheelRepository.saveAndFlush(feelWheel);
        when(mockFeelWheelSearchRepository.search(queryStringQuery("id:" + feelWheel.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(feelWheel), PageRequest.of(0, 1), 1));
        // Search the feelWheel
        restFeelWheelMockMvc.perform(get("/api/_search/feel-wheels?query=id:" + feelWheel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feelWheel.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeelWheel.class);
        FeelWheel feelWheel1 = new FeelWheel();
        feelWheel1.setId(1L);
        FeelWheel feelWheel2 = new FeelWheel();
        feelWheel2.setId(feelWheel1.getId());
        assertThat(feelWheel1).isEqualTo(feelWheel2);
        feelWheel2.setId(2L);
        assertThat(feelWheel1).isNotEqualTo(feelWheel2);
        feelWheel1.setId(null);
        assertThat(feelWheel1).isNotEqualTo(feelWheel2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FeelWheelDTO.class);
        FeelWheelDTO feelWheelDTO1 = new FeelWheelDTO();
        feelWheelDTO1.setId(1L);
        FeelWheelDTO feelWheelDTO2 = new FeelWheelDTO();
        assertThat(feelWheelDTO1).isNotEqualTo(feelWheelDTO2);
        feelWheelDTO2.setId(feelWheelDTO1.getId());
        assertThat(feelWheelDTO1).isEqualTo(feelWheelDTO2);
        feelWheelDTO2.setId(2L);
        assertThat(feelWheelDTO1).isNotEqualTo(feelWheelDTO2);
        feelWheelDTO1.setId(null);
        assertThat(feelWheelDTO1).isNotEqualTo(feelWheelDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(feelWheelMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(feelWheelMapper.fromId(null)).isNull();
    }
}
