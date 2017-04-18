package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.CompanyAdmin;
import workstarter.repository.CompanyAdminRepository;
import workstarter.repository.search.CompanyAdminSearchRepository;
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
 * Test class for the CompanyAdminResource REST controller.
 *
 * @see CompanyAdminResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class CompanyAdminResourceIntTest {

    private static final String DEFAULT_FIRSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "AAAAAAAAAA";
    private static final String UPDATED_LASTNAME = "BBBBBBBBBB";

    @Autowired
    private CompanyAdminRepository companyAdminRepository;

    @Autowired
    private CompanyAdminSearchRepository companyAdminSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompanyAdminMockMvc;

    private CompanyAdmin companyAdmin;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyAdminResource companyAdminResource = new CompanyAdminResource(companyAdminRepository, companyAdminSearchRepository);
        this.restCompanyAdminMockMvc = MockMvcBuilders.standaloneSetup(companyAdminResource)
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
    public static CompanyAdmin createEntity(EntityManager em) {
        CompanyAdmin companyAdmin = new CompanyAdmin();
        return companyAdmin;
    }

    @Before
    public void initTest() {
        companyAdminSearchRepository.deleteAll();
        companyAdmin = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyAdmin() throws Exception {
        int databaseSizeBeforeCreate = companyAdminRepository.findAll().size();

        // Create the CompanyAdmin
        restCompanyAdminMockMvc.perform(post("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyAdmin)))
            .andExpect(status().isCreated());

        // Validate the CompanyAdmin in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeCreate + 1);
        CompanyAdmin testCompanyAdmin = companyAdminList.get(companyAdminList.size() - 1);

        // Validate the CompanyAdmin in Elasticsearch
        CompanyAdmin companyAdminEs = companyAdminSearchRepository.findOne(testCompanyAdmin.getId());
        assertThat(companyAdminEs).isEqualToComparingFieldByField(testCompanyAdmin);
    }

    @Test
    @Transactional
    public void createCompanyAdminWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = companyAdminRepository.findAll().size();

        // Create the CompanyAdmin with an existing ID
        companyAdmin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyAdminMockMvc.perform(post("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyAdmin)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompanyAdmins() throws Exception {
        // Initialize the database
        companyAdminRepository.saveAndFlush(companyAdmin);

        // Get all the companyAdminList
        restCompanyAdminMockMvc.perform(get("/api/company-admins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyAdmin.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())));
    }

    @Test
    @Transactional
    public void getCompanyAdmin() throws Exception {
        // Initialize the database
        companyAdminRepository.saveAndFlush(companyAdmin);

        // Get the companyAdmin
        restCompanyAdminMockMvc.perform(get("/api/company-admins/{id}", companyAdmin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyAdmin.getId().intValue()))
            .andExpect(jsonPath("$.firstname").value(DEFAULT_FIRSTNAME.toString()))
            .andExpect(jsonPath("$.lastname").value(DEFAULT_LASTNAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyAdmin() throws Exception {
        // Get the companyAdmin
        restCompanyAdminMockMvc.perform(get("/api/company-admins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyAdmin() throws Exception {
        // Initialize the database
        companyAdminRepository.saveAndFlush(companyAdmin);
        companyAdminSearchRepository.save(companyAdmin);
        int databaseSizeBeforeUpdate = companyAdminRepository.findAll().size();

        // Update the companyAdmin
        CompanyAdmin updatedCompanyAdmin = companyAdminRepository.findOne(companyAdmin.getId());

        restCompanyAdminMockMvc.perform(put("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompanyAdmin)))
            .andExpect(status().isOk());

        // Validate the CompanyAdmin in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeUpdate);
        CompanyAdmin testCompanyAdmin = companyAdminList.get(companyAdminList.size() - 1);

        // Validate the CompanyAdmin in Elasticsearch
        CompanyAdmin companyAdminEs = companyAdminSearchRepository.findOne(testCompanyAdmin.getId());
        assertThat(companyAdminEs).isEqualToComparingFieldByField(testCompanyAdmin);
    }

    @Test
    @Transactional
    public void updateNonExistingCompanyAdmin() throws Exception {
        int databaseSizeBeforeUpdate = companyAdminRepository.findAll().size();

        // Create the CompanyAdmin

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCompanyAdminMockMvc.perform(put("/api/company-admins")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(companyAdmin)))
            .andExpect(status().isCreated());

        // Validate the CompanyAdmin in the database
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCompanyAdmin() throws Exception {
        // Initialize the database
        companyAdminRepository.saveAndFlush(companyAdmin);
        companyAdminSearchRepository.save(companyAdmin);
        int databaseSizeBeforeDelete = companyAdminRepository.findAll().size();

        // Get the companyAdmin
        restCompanyAdminMockMvc.perform(delete("/api/company-admins/{id}", companyAdmin.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean companyAdminExistsInEs = companyAdminSearchRepository.exists(companyAdmin.getId());
        assertThat(companyAdminExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanyAdmin> companyAdminList = companyAdminRepository.findAll();
        assertThat(companyAdminList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanyAdmin() throws Exception {
        // Initialize the database
        companyAdminRepository.saveAndFlush(companyAdmin);
        companyAdminSearchRepository.save(companyAdmin);

        // Search the companyAdmin
        restCompanyAdminMockMvc.perform(get("/api/_search/company-admins?query=id:" + companyAdmin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyAdmin.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstname").value(hasItem(DEFAULT_FIRSTNAME.toString())))
            .andExpect(jsonPath("$.[*].lastname").value(hasItem(DEFAULT_LASTNAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompanyAdmin.class);
    }
}
