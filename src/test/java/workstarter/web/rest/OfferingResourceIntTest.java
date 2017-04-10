package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Offering;
import workstarter.repository.OfferingRepository;
import workstarter.repository.search.OfferingSearchRepository;
import workstarter.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OfferingResource REST controller.
 *
 * @see OfferingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class OfferingResourceIntTest {

    private static final String DEFAULT_OFFERINGVALUES = "AAAAAAAAAA";
    private static final String UPDATED_OFFERINGVALUES = "BBBBBBBBBB";

    @Autowired
    private OfferingRepository offeringRepository;

    @Autowired
    private OfferingSearchRepository offeringSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restOfferingMockMvc;

    private Offering offering;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OfferingResource offeringResource = new OfferingResource(offeringRepository, offeringSearchRepository);
        this.restOfferingMockMvc = MockMvcBuilders.standaloneSetup(offeringResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Offering createEntity(EntityManager em) {
        Offering offering = new Offering()
            .offeringvalues(DEFAULT_OFFERINGVALUES);
        return offering;
    }

    @Before
    public void initTest() {
        offeringSearchRepository.deleteAll();
        offering = createEntity(em);
    }

    @Test
    @Transactional
    public void createOffering() throws Exception {
        int databaseSizeBeforeCreate = offeringRepository.findAll().size();

        // Create the Offering
        restOfferingMockMvc.perform(post("/api/offerings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offering)))
            .andExpect(status().isCreated());

        // Validate the Offering in the database
        List<Offering> offeringList = offeringRepository.findAll();
        assertThat(offeringList).hasSize(databaseSizeBeforeCreate + 1);
        Offering testOffering = offeringList.get(offeringList.size() - 1);
        assertThat(testOffering.getOfferingvalues()).isEqualTo(DEFAULT_OFFERINGVALUES);

        // Validate the Offering in Elasticsearch
        Offering offeringEs = offeringSearchRepository.findOne(testOffering.getId());
        assertThat(offeringEs).isEqualToComparingFieldByField(testOffering);
    }

    @Test
    @Transactional
    public void createOfferingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = offeringRepository.findAll().size();

        // Create the Offering with an existing ID
        offering.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOfferingMockMvc.perform(post("/api/offerings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offering)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Offering> offeringList = offeringRepository.findAll();
        assertThat(offeringList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllOfferings() throws Exception {
        // Initialize the database
        offeringRepository.saveAndFlush(offering);

        // Get all the offeringList
        restOfferingMockMvc.perform(get("/api/offerings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offering.getId().intValue())))
            .andExpect(jsonPath("$.[*].offeringvalues").value(hasItem(DEFAULT_OFFERINGVALUES.toString())));
    }

    @Test
    @Transactional
    public void getOffering() throws Exception {
        // Initialize the database
        offeringRepository.saveAndFlush(offering);

        // Get the offering
        restOfferingMockMvc.perform(get("/api/offerings/{id}", offering.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(offering.getId().intValue()))
            .andExpect(jsonPath("$.offeringvalues").value(DEFAULT_OFFERINGVALUES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOffering() throws Exception {
        // Get the offering
        restOfferingMockMvc.perform(get("/api/offerings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOffering() throws Exception {
        // Initialize the database
        offeringRepository.saveAndFlush(offering);
        offeringSearchRepository.save(offering);
        int databaseSizeBeforeUpdate = offeringRepository.findAll().size();

        // Update the offering
        Offering updatedOffering = offeringRepository.findOne(offering.getId());
        updatedOffering
            .offeringvalues(UPDATED_OFFERINGVALUES);

        restOfferingMockMvc.perform(put("/api/offerings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOffering)))
            .andExpect(status().isOk());

        // Validate the Offering in the database
        List<Offering> offeringList = offeringRepository.findAll();
        assertThat(offeringList).hasSize(databaseSizeBeforeUpdate);
        Offering testOffering = offeringList.get(offeringList.size() - 1);
        assertThat(testOffering.getOfferingvalues()).isEqualTo(UPDATED_OFFERINGVALUES);

        // Validate the Offering in Elasticsearch
        Offering offeringEs = offeringSearchRepository.findOne(testOffering.getId());
        assertThat(offeringEs).isEqualToComparingFieldByField(testOffering);
    }

    @Test
    @Transactional
    public void updateNonExistingOffering() throws Exception {
        int databaseSizeBeforeUpdate = offeringRepository.findAll().size();

        // Create the Offering

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restOfferingMockMvc.perform(put("/api/offerings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(offering)))
            .andExpect(status().isCreated());

        // Validate the Offering in the database
        List<Offering> offeringList = offeringRepository.findAll();
        assertThat(offeringList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteOffering() throws Exception {
        // Initialize the database
        offeringRepository.saveAndFlush(offering);
        offeringSearchRepository.save(offering);
        int databaseSizeBeforeDelete = offeringRepository.findAll().size();

        // Get the offering
        restOfferingMockMvc.perform(delete("/api/offerings/{id}", offering.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean offeringExistsInEs = offeringSearchRepository.exists(offering.getId());
        assertThat(offeringExistsInEs).isFalse();

        // Validate the database is empty
        List<Offering> offeringList = offeringRepository.findAll();
        assertThat(offeringList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchOffering() throws Exception {
        // Initialize the database
        offeringRepository.saveAndFlush(offering);
        offeringSearchRepository.save(offering);

        // Search the offering
        restOfferingMockMvc.perform(get("/api/_search/offerings?query=id:" + offering.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(offering.getId().intValue())))
            .andExpect(jsonPath("$.[*].offeringvalues").value(hasItem(DEFAULT_OFFERINGVALUES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offering.class);
    }
}
