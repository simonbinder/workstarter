package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Keywords;
import workstarter.repository.KeywordsRepository;
import workstarter.repository.search.KeywordsSearchRepository;
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
 * Test class for the KeywordsResource REST controller.
 *
 * @see KeywordsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class KeywordsResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private KeywordsRepository keywordsRepository;

    @Autowired
    private KeywordsSearchRepository keywordsSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restKeywordsMockMvc;

    private Keywords keywords;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        KeywordsResource keywordsResource = new KeywordsResource(keywordsRepository, keywordsSearchRepository);
        this.restKeywordsMockMvc = MockMvcBuilders.standaloneSetup(keywordsResource)
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
    public static Keywords createEntity(EntityManager em) {
        Keywords keywords = new Keywords()
            .name(DEFAULT_NAME);
        return keywords;
    }

    @Before
    public void initTest() {
        keywordsSearchRepository.deleteAll();
        keywords = createEntity(em);
    }

    @Test
    @Transactional
    public void createKeywords() throws Exception {
        int databaseSizeBeforeCreate = keywordsRepository.findAll().size();

        // Create the Keywords
        restKeywordsMockMvc.perform(post("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keywords)))
            .andExpect(status().isCreated());

        // Validate the Keywords in the database
        List<Keywords> keywordsList = keywordsRepository.findAll();
        assertThat(keywordsList).hasSize(databaseSizeBeforeCreate + 1);
        Keywords testKeywords = keywordsList.get(keywordsList.size() - 1);
        assertThat(testKeywords.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Keywords in Elasticsearch
        Keywords keywordsEs = keywordsSearchRepository.findOne(testKeywords.getId());
        assertThat(keywordsEs).isEqualToComparingFieldByField(testKeywords);
    }

    @Test
    @Transactional
    public void createKeywordsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = keywordsRepository.findAll().size();

        // Create the Keywords with an existing ID
        keywords.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKeywordsMockMvc.perform(post("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keywords)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Keywords> keywordsList = keywordsRepository.findAll();
        assertThat(keywordsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = keywordsRepository.findAll().size();
        // set the field null
        keywords.setName(null);

        // Create the Keywords, which fails.

        restKeywordsMockMvc.perform(post("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keywords)))
            .andExpect(status().isBadRequest());

        List<Keywords> keywordsList = keywordsRepository.findAll();
        assertThat(keywordsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKeywords() throws Exception {
        // Initialize the database
        keywordsRepository.saveAndFlush(keywords);

        // Get all the keywordsList
        restKeywordsMockMvc.perform(get("/api/keywords?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keywords.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getKeywords() throws Exception {
        // Initialize the database
        keywordsRepository.saveAndFlush(keywords);

        // Get the keywords
        restKeywordsMockMvc.perform(get("/api/keywords/{id}", keywords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(keywords.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKeywords() throws Exception {
        // Get the keywords
        restKeywordsMockMvc.perform(get("/api/keywords/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKeywords() throws Exception {
        // Initialize the database
        keywordsRepository.saveAndFlush(keywords);
        keywordsSearchRepository.save(keywords);
        int databaseSizeBeforeUpdate = keywordsRepository.findAll().size();

        // Update the keywords
        Keywords updatedKeywords = keywordsRepository.findOne(keywords.getId());
        updatedKeywords
            .name(UPDATED_NAME);

        restKeywordsMockMvc.perform(put("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedKeywords)))
            .andExpect(status().isOk());

        // Validate the Keywords in the database
        List<Keywords> keywordsList = keywordsRepository.findAll();
        assertThat(keywordsList).hasSize(databaseSizeBeforeUpdate);
        Keywords testKeywords = keywordsList.get(keywordsList.size() - 1);
        assertThat(testKeywords.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Keywords in Elasticsearch
        Keywords keywordsEs = keywordsSearchRepository.findOne(testKeywords.getId());
        assertThat(keywordsEs).isEqualToComparingFieldByField(testKeywords);
    }

    @Test
    @Transactional
    public void updateNonExistingKeywords() throws Exception {
        int databaseSizeBeforeUpdate = keywordsRepository.findAll().size();

        // Create the Keywords

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restKeywordsMockMvc.perform(put("/api/keywords")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(keywords)))
            .andExpect(status().isCreated());

        // Validate the Keywords in the database
        List<Keywords> keywordsList = keywordsRepository.findAll();
        assertThat(keywordsList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteKeywords() throws Exception {
        // Initialize the database
        keywordsRepository.saveAndFlush(keywords);
        keywordsSearchRepository.save(keywords);
        int databaseSizeBeforeDelete = keywordsRepository.findAll().size();

        // Get the keywords
        restKeywordsMockMvc.perform(delete("/api/keywords/{id}", keywords.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean keywordsExistsInEs = keywordsSearchRepository.exists(keywords.getId());
        assertThat(keywordsExistsInEs).isFalse();

        // Validate the database is empty
        List<Keywords> keywordsList = keywordsRepository.findAll();
        assertThat(keywordsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchKeywords() throws Exception {
        // Initialize the database
        keywordsRepository.saveAndFlush(keywords);
        keywordsSearchRepository.save(keywords);

        // Search the keywords
        restKeywordsMockMvc.perform(get("/api/_search/keywords?query=id:" + keywords.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(keywords.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Keywords.class);
    }
}
