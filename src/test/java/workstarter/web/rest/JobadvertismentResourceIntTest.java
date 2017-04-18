package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Jobadvertisment;
import workstarter.repository.JobadvertismentRepository;
import workstarter.repository.search.JobadvertismentSearchRepository;
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
 * Test class for the JobadvertismentResource REST controller.
 *
 * @see JobadvertismentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class JobadvertismentResourceIntTest {

    private static final String DEFAULT_JOBNAME = "AAAAAAAAAA";
    private static final String UPDATED_JOBNAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_EXERCISES = "AAAAAAAAAA";
    private static final String UPDATED_EXERCISES = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_TASKS = "AAAAAAAAAA";
    private static final String UPDATED_TASKS = "BBBBBBBBBB";

    @Autowired
    private JobadvertismentRepository jobadvertismentRepository;

    @Autowired
    private JobadvertismentSearchRepository jobadvertismentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restJobadvertismentMockMvc;

    private Jobadvertisment jobadvertisment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        JobadvertismentResource jobadvertismentResource = new JobadvertismentResource(jobadvertismentRepository, jobadvertismentSearchRepository);
        this.restJobadvertismentMockMvc = MockMvcBuilders.standaloneSetup(jobadvertismentResource)
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
    public static Jobadvertisment createEntity(EntityManager em) {
        Jobadvertisment jobadvertisment = new Jobadvertisment()
            .jobname(DEFAULT_JOBNAME)
            .description(DEFAULT_DESCRIPTION)
            .title(DEFAULT_TITLE)
            .exercises(DEFAULT_EXERCISES)
            .contact(DEFAULT_CONTACT)
            .location(DEFAULT_LOCATION)
            .tasks(DEFAULT_TASKS);
        return jobadvertisment;
    }

    @Before
    public void initTest() {
        jobadvertismentSearchRepository.deleteAll();
        jobadvertisment = createEntity(em);
    }

    @Test
    @Transactional
    public void createJobadvertisment() throws Exception {
        int databaseSizeBeforeCreate = jobadvertismentRepository.findAll().size();

        // Create the Jobadvertisment
        restJobadvertismentMockMvc.perform(post("/api/jobadvertisments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobadvertisment)))
            .andExpect(status().isCreated());

        // Validate the Jobadvertisment in the database
        List<Jobadvertisment> jobadvertismentList = jobadvertismentRepository.findAll();
        assertThat(jobadvertismentList).hasSize(databaseSizeBeforeCreate + 1);
        Jobadvertisment testJobadvertisment = jobadvertismentList.get(jobadvertismentList.size() - 1);
        assertThat(testJobadvertisment.getJobname()).isEqualTo(DEFAULT_JOBNAME);
        assertThat(testJobadvertisment.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testJobadvertisment.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testJobadvertisment.getExercises()).isEqualTo(DEFAULT_EXERCISES);
        assertThat(testJobadvertisment.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testJobadvertisment.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testJobadvertisment.getTasks()).isEqualTo(DEFAULT_TASKS);

        // Validate the Jobadvertisment in Elasticsearch
        Jobadvertisment jobadvertismentEs = jobadvertismentSearchRepository.findOne(testJobadvertisment.getId());
        assertThat(jobadvertismentEs).isEqualToComparingFieldByField(testJobadvertisment);
    }

    @Test
    @Transactional
    public void createJobadvertismentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = jobadvertismentRepository.findAll().size();

        // Create the Jobadvertisment with an existing ID
        jobadvertisment.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restJobadvertismentMockMvc.perform(post("/api/jobadvertisments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobadvertisment)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Jobadvertisment> jobadvertismentList = jobadvertismentRepository.findAll();
        assertThat(jobadvertismentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkJobnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = jobadvertismentRepository.findAll().size();
        // set the field null
        jobadvertisment.setJobname(null);

        // Create the Jobadvertisment, which fails.

        restJobadvertismentMockMvc.perform(post("/api/jobadvertisments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobadvertisment)))
            .andExpect(status().isBadRequest());

        List<Jobadvertisment> jobadvertismentList = jobadvertismentRepository.findAll();
        assertThat(jobadvertismentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllJobadvertisments() throws Exception {
        // Initialize the database
        jobadvertismentRepository.saveAndFlush(jobadvertisment);

        // Get all the jobadvertismentList
        restJobadvertismentMockMvc.perform(get("/api/jobadvertisments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobadvertisment.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobname").value(hasItem(DEFAULT_JOBNAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].exercises").value(hasItem(DEFAULT_EXERCISES.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].tasks").value(hasItem(DEFAULT_TASKS.toString())));
    }

    @Test
    @Transactional
    public void getJobadvertisment() throws Exception {
        // Initialize the database
        jobadvertismentRepository.saveAndFlush(jobadvertisment);

        // Get the jobadvertisment
        restJobadvertismentMockMvc.perform(get("/api/jobadvertisments/{id}", jobadvertisment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(jobadvertisment.getId().intValue()))
            .andExpect(jsonPath("$.jobname").value(DEFAULT_JOBNAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.exercises").value(DEFAULT_EXERCISES.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.tasks").value(DEFAULT_TASKS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingJobadvertisment() throws Exception {
        // Get the jobadvertisment
        restJobadvertismentMockMvc.perform(get("/api/jobadvertisments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateJobadvertisment() throws Exception {
        // Initialize the database
        jobadvertismentRepository.saveAndFlush(jobadvertisment);
        jobadvertismentSearchRepository.save(jobadvertisment);
        int databaseSizeBeforeUpdate = jobadvertismentRepository.findAll().size();

        // Update the jobadvertisment
        Jobadvertisment updatedJobadvertisment = jobadvertismentRepository.findOne(jobadvertisment.getId());
        updatedJobadvertisment
            .jobname(UPDATED_JOBNAME)
            .description(UPDATED_DESCRIPTION)
            .title(UPDATED_TITLE)
            .exercises(UPDATED_EXERCISES)
            .contact(UPDATED_CONTACT)
            .location(UPDATED_LOCATION)
            .tasks(UPDATED_TASKS);

        restJobadvertismentMockMvc.perform(put("/api/jobadvertisments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedJobadvertisment)))
            .andExpect(status().isOk());

        // Validate the Jobadvertisment in the database
        List<Jobadvertisment> jobadvertismentList = jobadvertismentRepository.findAll();
        assertThat(jobadvertismentList).hasSize(databaseSizeBeforeUpdate);
        Jobadvertisment testJobadvertisment = jobadvertismentList.get(jobadvertismentList.size() - 1);
        assertThat(testJobadvertisment.getJobname()).isEqualTo(UPDATED_JOBNAME);
        assertThat(testJobadvertisment.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testJobadvertisment.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testJobadvertisment.getExercises()).isEqualTo(UPDATED_EXERCISES);
        assertThat(testJobadvertisment.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testJobadvertisment.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testJobadvertisment.getTasks()).isEqualTo(UPDATED_TASKS);

        // Validate the Jobadvertisment in Elasticsearch
        Jobadvertisment jobadvertismentEs = jobadvertismentSearchRepository.findOne(testJobadvertisment.getId());
        assertThat(jobadvertismentEs).isEqualToComparingFieldByField(testJobadvertisment);
    }

    @Test
    @Transactional
    public void updateNonExistingJobadvertisment() throws Exception {
        int databaseSizeBeforeUpdate = jobadvertismentRepository.findAll().size();

        // Create the Jobadvertisment

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restJobadvertismentMockMvc.perform(put("/api/jobadvertisments")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(jobadvertisment)))
            .andExpect(status().isCreated());

        // Validate the Jobadvertisment in the database
        List<Jobadvertisment> jobadvertismentList = jobadvertismentRepository.findAll();
        assertThat(jobadvertismentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteJobadvertisment() throws Exception {
        // Initialize the database
        jobadvertismentRepository.saveAndFlush(jobadvertisment);
        jobadvertismentSearchRepository.save(jobadvertisment);
        int databaseSizeBeforeDelete = jobadvertismentRepository.findAll().size();

        // Get the jobadvertisment
        restJobadvertismentMockMvc.perform(delete("/api/jobadvertisments/{id}", jobadvertisment.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean jobadvertismentExistsInEs = jobadvertismentSearchRepository.exists(jobadvertisment.getId());
        assertThat(jobadvertismentExistsInEs).isFalse();

        // Validate the database is empty
        List<Jobadvertisment> jobadvertismentList = jobadvertismentRepository.findAll();
        assertThat(jobadvertismentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchJobadvertisment() throws Exception {
        // Initialize the database
        jobadvertismentRepository.saveAndFlush(jobadvertisment);
        jobadvertismentSearchRepository.save(jobadvertisment);

        // Search the jobadvertisment
        restJobadvertismentMockMvc.perform(get("/api/_search/jobadvertisments?query=id:" + jobadvertisment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jobadvertisment.getId().intValue())))
            .andExpect(jsonPath("$.[*].jobname").value(hasItem(DEFAULT_JOBNAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].exercises").value(hasItem(DEFAULT_EXERCISES.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].tasks").value(hasItem(DEFAULT_TASKS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Jobadvertisment.class);
    }
}
