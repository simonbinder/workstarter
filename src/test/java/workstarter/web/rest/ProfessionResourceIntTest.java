package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Profession;
import workstarter.repository.ProfessionRepository;
import workstarter.service.ProfessionService;
import workstarter.repository.search.ProfessionSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProfessionResource REST controller.
 *
 * @see ProfessionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class ProfessionResourceIntTest {

    private static final String DEFAULT_POSITION = "AAAAAAAAAA";
    private static final String UPDATED_POSITION = "BBBBBBBBBB";

    private static final String DEFAULT_FORM_OF_EMPLOYMENT = "AAAAAAAAAA";
    private static final String UPDATED_FORM_OF_EMPLOYMENT = "BBBBBBBBBB";

    private static final String DEFAULT_TASKS = "AAAAAAAAAA";
    private static final String UPDATED_TASKS = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DOMAIN = "AAAAAAAAAA";
    private static final String UPDATED_DOMAIN = "BBBBBBBBBB";

    private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ProfessionRepository professionRepository;

    @Autowired
    private ProfessionService professionService;

    @Autowired
    private ProfessionSearchRepository professionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProfessionMockMvc;

    private Profession profession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProfessionResource professionResource = new ProfessionResource(professionService);
        this.restProfessionMockMvc = MockMvcBuilders.standaloneSetup(professionResource)
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
    public static Profession createEntity(EntityManager em) {
        Profession profession = new Profession()
            .position(DEFAULT_POSITION)
            .formOfEmployment(DEFAULT_FORM_OF_EMPLOYMENT)
            .tasks(DEFAULT_TASKS)
            .companyName(DEFAULT_COMPANY_NAME)
            .domain(DEFAULT_DOMAIN)
            .sector(DEFAULT_SECTOR)
            .location(DEFAULT_LOCATION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return profession;
    }

    @Before
    public void initTest() {
        professionSearchRepository.deleteAll();
        profession = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfession() throws Exception {
        int databaseSizeBeforeCreate = professionRepository.findAll().size();

        // Create the Profession
        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isCreated());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeCreate + 1);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getPosition()).isEqualTo(DEFAULT_POSITION);
        assertThat(testProfession.getFormOfEmployment()).isEqualTo(DEFAULT_FORM_OF_EMPLOYMENT);
        assertThat(testProfession.getTasks()).isEqualTo(DEFAULT_TASKS);
        assertThat(testProfession.getCompanyName()).isEqualTo(DEFAULT_COMPANY_NAME);
        assertThat(testProfession.getDomain()).isEqualTo(DEFAULT_DOMAIN);
        assertThat(testProfession.getSector()).isEqualTo(DEFAULT_SECTOR);
        assertThat(testProfession.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testProfession.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testProfession.getEndDate()).isEqualTo(DEFAULT_END_DATE);

        // Validate the Profession in Elasticsearch
        Profession professionEs = professionSearchRepository.findOne(testProfession.getId());
        assertThat(professionEs).isEqualToComparingFieldByField(testProfession);
    }

    @Test
    @Transactional
    public void createProfessionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = professionRepository.findAll().size();

        // Create the Profession with an existing ID
        profession.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFormOfEmploymentIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setFormOfEmployment(null);

        // Create the Profession, which fails.

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCompanyNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setCompanyName(null);

        // Create the Profession, which fails.

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDomainIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setDomain(null);

        // Create the Profession, which fails.

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSectorIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setSector(null);

        // Create the Profession, which fails.

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setLocation(null);

        // Create the Profession, which fails.

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setStartDate(null);

        // Create the Profession, which fails.

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = professionRepository.findAll().size();
        // set the field null
        profession.setEndDate(null);

        // Create the Profession, which fails.

        restProfessionMockMvc.perform(post("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isBadRequest());

        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfessions() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get all the professionList
        restProfessionMockMvc.perform(get("/api/professions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profession.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].formOfEmployment").value(hasItem(DEFAULT_FORM_OF_EMPLOYMENT.toString())))
            .andExpect(jsonPath("$.[*].tasks").value(hasItem(DEFAULT_TASKS.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void getProfession() throws Exception {
        // Initialize the database
        professionRepository.saveAndFlush(profession);

        // Get the profession
        restProfessionMockMvc.perform(get("/api/professions/{id}", profession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profession.getId().intValue()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.formOfEmployment").value(DEFAULT_FORM_OF_EMPLOYMENT.toString()))
            .andExpect(jsonPath("$.tasks").value(DEFAULT_TASKS.toString()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME.toString()))
            .andExpect(jsonPath("$.domain").value(DEFAULT_DOMAIN.toString()))
            .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProfession() throws Exception {
        // Get the profession
        restProfessionMockMvc.perform(get("/api/professions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfession() throws Exception {
        // Initialize the database
        professionService.save(profession);

        int databaseSizeBeforeUpdate = professionRepository.findAll().size();

        // Update the profession
        Profession updatedProfession = professionRepository.findOne(profession.getId());
        updatedProfession
            .position(UPDATED_POSITION)
            .formOfEmployment(UPDATED_FORM_OF_EMPLOYMENT)
            .tasks(UPDATED_TASKS)
            .companyName(UPDATED_COMPANY_NAME)
            .domain(UPDATED_DOMAIN)
            .sector(UPDATED_SECTOR)
            .location(UPDATED_LOCATION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restProfessionMockMvc.perform(put("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProfession)))
            .andExpect(status().isOk());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate);
        Profession testProfession = professionList.get(professionList.size() - 1);
        assertThat(testProfession.getPosition()).isEqualTo(UPDATED_POSITION);
        assertThat(testProfession.getFormOfEmployment()).isEqualTo(UPDATED_FORM_OF_EMPLOYMENT);
        assertThat(testProfession.getTasks()).isEqualTo(UPDATED_TASKS);
        assertThat(testProfession.getCompanyName()).isEqualTo(UPDATED_COMPANY_NAME);
        assertThat(testProfession.getDomain()).isEqualTo(UPDATED_DOMAIN);
        assertThat(testProfession.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testProfession.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProfession.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testProfession.getEndDate()).isEqualTo(UPDATED_END_DATE);

        // Validate the Profession in Elasticsearch
        Profession professionEs = professionSearchRepository.findOne(testProfession.getId());
        assertThat(professionEs).isEqualToComparingFieldByField(testProfession);
    }

    @Test
    @Transactional
    public void updateNonExistingProfession() throws Exception {
        int databaseSizeBeforeUpdate = professionRepository.findAll().size();

        // Create the Profession

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restProfessionMockMvc.perform(put("/api/professions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profession)))
            .andExpect(status().isCreated());

        // Validate the Profession in the database
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteProfession() throws Exception {
        // Initialize the database
        professionService.save(profession);

        int databaseSizeBeforeDelete = professionRepository.findAll().size();

        // Get the profession
        restProfessionMockMvc.perform(delete("/api/professions/{id}", profession.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean professionExistsInEs = professionSearchRepository.exists(profession.getId());
        assertThat(professionExistsInEs).isFalse();

        // Validate the database is empty
        List<Profession> professionList = professionRepository.findAll();
        assertThat(professionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchProfession() throws Exception {
        // Initialize the database
        professionService.save(profession);

        // Search the profession
        restProfessionMockMvc.perform(get("/api/_search/professions?query=id:" + profession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profession.getId().intValue())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].formOfEmployment").value(hasItem(DEFAULT_FORM_OF_EMPLOYMENT.toString())))
            .andExpect(jsonPath("$.[*].tasks").value(hasItem(DEFAULT_TASKS.toString())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME.toString())))
            .andExpect(jsonPath("$.[*].domain").value(hasItem(DEFAULT_DOMAIN.toString())))
            .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profession.class);
    }
}
