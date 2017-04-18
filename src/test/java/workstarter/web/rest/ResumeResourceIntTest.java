package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Resume;
import workstarter.repository.ResumeRepository;
import workstarter.repository.search.ResumeSearchRepository;
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
 * Test class for the ResumeResource REST controller.
 *
 * @see ResumeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class ResumeResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeSearchRepository resumeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restResumeMockMvc;

    private Resume resume;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ResumeResource resumeResource = new ResumeResource(resumeRepository, resumeSearchRepository);
        this.restResumeMockMvc = MockMvcBuilders.standaloneSetup(resumeResource)
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
    public static Resume createEntity(EntityManager em) {
        Resume resume = new Resume()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        return resume;
    }

    @Before
    public void initTest() {
        resumeSearchRepository.deleteAll();
        resume = createEntity(em);
    }

    @Test
    @Transactional
    public void createResume() throws Exception {
        int databaseSizeBeforeCreate = resumeRepository.findAll().size();

        // Create the Resume
        restResumeMockMvc.perform(post("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isCreated());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate + 1);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResume.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Resume in Elasticsearch
        Resume resumeEs = resumeSearchRepository.findOne(testResume.getId());
        assertThat(resumeEs).isEqualToComparingFieldByField(testResume);
    }

    @Test
    @Transactional
    public void createResumeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resumeRepository.findAll().size();

        // Create the Resume with an existing ID
        resume.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResumeMockMvc.perform(post("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = resumeRepository.findAll().size();
        // set the field null
        resume.setTitle(null);

        // Create the Resume, which fails.

        restResumeMockMvc.perform(post("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isBadRequest());

        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllResumes() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get all the resumeList
        restResumeMockMvc.perform(get("/api/resumes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);

        // Get the resume
        restResumeMockMvc.perform(get("/api/resumes/{id}", resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(resume.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingResume() throws Exception {
        // Get the resume
        restResumeMockMvc.perform(get("/api/resumes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Update the resume
        Resume updatedResume = resumeRepository.findOne(resume.getId());
        updatedResume
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);

        restResumeMockMvc.perform(put("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedResume)))
            .andExpect(status().isOk());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate);
        Resume testResume = resumeList.get(resumeList.size() - 1);
        assertThat(testResume.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResume.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Resume in Elasticsearch
        Resume resumeEs = resumeSearchRepository.findOne(testResume.getId());
        assertThat(resumeEs).isEqualToComparingFieldByField(testResume);
    }

    @Test
    @Transactional
    public void updateNonExistingResume() throws Exception {
        int databaseSizeBeforeUpdate = resumeRepository.findAll().size();

        // Create the Resume

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restResumeMockMvc.perform(put("/api/resumes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(resume)))
            .andExpect(status().isCreated());

        // Validate the Resume in the database
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);
        int databaseSizeBeforeDelete = resumeRepository.findAll().size();

        // Get the resume
        restResumeMockMvc.perform(delete("/api/resumes/{id}", resume.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean resumeExistsInEs = resumeSearchRepository.exists(resume.getId());
        assertThat(resumeExistsInEs).isFalse();

        // Validate the database is empty
        List<Resume> resumeList = resumeRepository.findAll();
        assertThat(resumeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchResume() throws Exception {
        // Initialize the database
        resumeRepository.saveAndFlush(resume);
        resumeSearchRepository.save(resume);

        // Search the resume
        restResumeMockMvc.perform(get("/api/_search/resumes?query=id:" + resume.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resume.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Resume.class);
    }
}
