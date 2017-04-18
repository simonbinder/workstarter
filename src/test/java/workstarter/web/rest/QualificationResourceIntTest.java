package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Qualification;
import workstarter.repository.QualificationRepository;
import workstarter.repository.search.QualificationSearchRepository;
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
 * Test class for the QualificationResource REST controller.
 *
 * @see QualificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class QualificationResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private QualificationSearchRepository qualificationSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restQualificationMockMvc;

    private Qualification qualification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QualificationResource qualificationResource = new QualificationResource(qualificationRepository, qualificationSearchRepository);
        this.restQualificationMockMvc = MockMvcBuilders.standaloneSetup(qualificationResource)
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
    public static Qualification createEntity(EntityManager em) {
        Qualification qualification = new Qualification()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        return qualification;
    }

    @Before
    public void initTest() {
        qualificationSearchRepository.deleteAll();
        qualification = createEntity(em);
    }

    @Test
    @Transactional
    public void createQualification() throws Exception {
        int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

        // Create the Qualification
        restQualificationMockMvc.perform(post("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualification)))
            .andExpect(status().isCreated());

        // Validate the Qualification in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeCreate + 1);
        Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
        assertThat(testQualification.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testQualification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Qualification in Elasticsearch
        Qualification qualificationEs = qualificationSearchRepository.findOne(testQualification.getId());
        assertThat(qualificationEs).isEqualToComparingFieldByField(testQualification);
    }

    @Test
    @Transactional
    public void createQualificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = qualificationRepository.findAll().size();

        // Create the Qualification with an existing ID
        qualification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQualificationMockMvc.perform(post("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualification)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = qualificationRepository.findAll().size();
        // set the field null
        qualification.setName(null);

        // Create the Qualification, which fails.

        restQualificationMockMvc.perform(post("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualification)))
            .andExpect(status().isBadRequest());

        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQualifications() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);

        // Get all the qualificationList
        restQualificationMockMvc.perform(get("/api/qualifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);

        // Get the qualification
        restQualificationMockMvc.perform(get("/api/qualifications/{id}", qualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(qualification.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQualification() throws Exception {
        // Get the qualification
        restQualificationMockMvc.perform(get("/api/qualifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);
        qualificationSearchRepository.save(qualification);
        int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

        // Update the qualification
        Qualification updatedQualification = qualificationRepository.findOne(qualification.getId());
        updatedQualification
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);

        restQualificationMockMvc.perform(put("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedQualification)))
            .andExpect(status().isOk());

        // Validate the Qualification in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate);
        Qualification testQualification = qualificationList.get(qualificationList.size() - 1);
        assertThat(testQualification.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testQualification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Qualification in Elasticsearch
        Qualification qualificationEs = qualificationSearchRepository.findOne(testQualification.getId());
        assertThat(qualificationEs).isEqualToComparingFieldByField(testQualification);
    }

    @Test
    @Transactional
    public void updateNonExistingQualification() throws Exception {
        int databaseSizeBeforeUpdate = qualificationRepository.findAll().size();

        // Create the Qualification

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restQualificationMockMvc.perform(put("/api/qualifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(qualification)))
            .andExpect(status().isCreated());

        // Validate the Qualification in the database
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);
        qualificationSearchRepository.save(qualification);
        int databaseSizeBeforeDelete = qualificationRepository.findAll().size();

        // Get the qualification
        restQualificationMockMvc.perform(delete("/api/qualifications/{id}", qualification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean qualificationExistsInEs = qualificationSearchRepository.exists(qualification.getId());
        assertThat(qualificationExistsInEs).isFalse();

        // Validate the database is empty
        List<Qualification> qualificationList = qualificationRepository.findAll();
        assertThat(qualificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchQualification() throws Exception {
        // Initialize the database
        qualificationRepository.saveAndFlush(qualification);
        qualificationSearchRepository.save(qualification);

        // Search the qualification
        restQualificationMockMvc.perform(get("/api/_search/qualifications?query=id:" + qualification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qualification.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Qualification.class);
    }
}
