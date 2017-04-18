package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Searching;
import workstarter.repository.SearchingRepository;
import workstarter.repository.search.SearchingSearchRepository;
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
 * Test class for the SearchingResource REST controller.
 *
 * @see SearchingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class SearchingResourceIntTest {

    private static final String DEFAULT_SEARCHINGVALUES = "AAAAAAAAAA";
    private static final String UPDATED_SEARCHINGVALUES = "BBBBBBBBBB";

    @Autowired
    private SearchingRepository searchingRepository;

    @Autowired
    private SearchingSearchRepository searchingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSearchingMockMvc;

    private Searching searching;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SearchingResource searchingResource = new SearchingResource(searchingRepository, searchingSearchRepository);
        this.restSearchingMockMvc = MockMvcBuilders.standaloneSetup(searchingResource)
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
    public static Searching createEntity(EntityManager em) {
        Searching searching = new Searching()
            .searchingvalues(DEFAULT_SEARCHINGVALUES);
        return searching;
    }

    @Before
    public void initTest() {
        searchingSearchRepository.deleteAll();
        searching = createEntity(em);
    }

    @Test
    @Transactional
    public void createSearching() throws Exception {
        int databaseSizeBeforeCreate = searchingRepository.findAll().size();

        // Create the Searching
        restSearchingMockMvc.perform(post("/api/searchings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searching)))
            .andExpect(status().isCreated());

        // Validate the Searching in the database
        List<Searching> searchingList = searchingRepository.findAll();
        assertThat(searchingList).hasSize(databaseSizeBeforeCreate + 1);
        Searching testSearching = searchingList.get(searchingList.size() - 1);
        assertThat(testSearching.getSearchingvalues()).isEqualTo(DEFAULT_SEARCHINGVALUES);

        // Validate the Searching in Elasticsearch
        Searching searchingEs = searchingSearchRepository.findOne(testSearching.getId());
        assertThat(searchingEs).isEqualToComparingFieldByField(testSearching);
    }

    @Test
    @Transactional
    public void createSearchingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = searchingRepository.findAll().size();

        // Create the Searching with an existing ID
        searching.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchingMockMvc.perform(post("/api/searchings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searching)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Searching> searchingList = searchingRepository.findAll();
        assertThat(searchingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSearchings() throws Exception {
        // Initialize the database
        searchingRepository.saveAndFlush(searching);

        // Get all the searchingList
        restSearchingMockMvc.perform(get("/api/searchings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searching.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchingvalues").value(hasItem(DEFAULT_SEARCHINGVALUES.toString())));
    }

    @Test
    @Transactional
    public void getSearching() throws Exception {
        // Initialize the database
        searchingRepository.saveAndFlush(searching);

        // Get the searching
        restSearchingMockMvc.perform(get("/api/searchings/{id}", searching.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(searching.getId().intValue()))
            .andExpect(jsonPath("$.searchingvalues").value(DEFAULT_SEARCHINGVALUES.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSearching() throws Exception {
        // Get the searching
        restSearchingMockMvc.perform(get("/api/searchings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSearching() throws Exception {
        // Initialize the database
        searchingRepository.saveAndFlush(searching);
        searchingSearchRepository.save(searching);
        int databaseSizeBeforeUpdate = searchingRepository.findAll().size();

        // Update the searching
        Searching updatedSearching = searchingRepository.findOne(searching.getId());
        updatedSearching
            .searchingvalues(UPDATED_SEARCHINGVALUES);

        restSearchingMockMvc.perform(put("/api/searchings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSearching)))
            .andExpect(status().isOk());

        // Validate the Searching in the database
        List<Searching> searchingList = searchingRepository.findAll();
        assertThat(searchingList).hasSize(databaseSizeBeforeUpdate);
        Searching testSearching = searchingList.get(searchingList.size() - 1);
        assertThat(testSearching.getSearchingvalues()).isEqualTo(UPDATED_SEARCHINGVALUES);

        // Validate the Searching in Elasticsearch
        Searching searchingEs = searchingSearchRepository.findOne(testSearching.getId());
        assertThat(searchingEs).isEqualToComparingFieldByField(testSearching);
    }

    @Test
    @Transactional
    public void updateNonExistingSearching() throws Exception {
        int databaseSizeBeforeUpdate = searchingRepository.findAll().size();

        // Create the Searching

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSearchingMockMvc.perform(put("/api/searchings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(searching)))
            .andExpect(status().isCreated());

        // Validate the Searching in the database
        List<Searching> searchingList = searchingRepository.findAll();
        assertThat(searchingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSearching() throws Exception {
        // Initialize the database
        searchingRepository.saveAndFlush(searching);
        searchingSearchRepository.save(searching);
        int databaseSizeBeforeDelete = searchingRepository.findAll().size();

        // Get the searching
        restSearchingMockMvc.perform(delete("/api/searchings/{id}", searching.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean searchingExistsInEs = searchingSearchRepository.exists(searching.getId());
        assertThat(searchingExistsInEs).isFalse();

        // Validate the database is empty
        List<Searching> searchingList = searchingRepository.findAll();
        assertThat(searchingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSearching() throws Exception {
        // Initialize the database
        searchingRepository.saveAndFlush(searching);
        searchingSearchRepository.save(searching);

        // Search the searching
        restSearchingMockMvc.perform(get("/api/_search/searchings?query=id:" + searching.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searching.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchingvalues").value(hasItem(DEFAULT_SEARCHINGVALUES.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Searching.class);
    }
}
