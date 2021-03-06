package workstarter.web.rest;

import workstarter.WorkstarterApp;

import workstarter.domain.Portfolio;
import workstarter.repository.PortfolioRepository;
import workstarter.repository.search.PortfolioSearchRepository;
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
 * Test class for the PortfolioResource REST controller.
 *
 * @see PortfolioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WorkstarterApp.class)
public class PortfolioResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_APPENDIX = "AAAAAAAAAA";
    private static final String UPDATED_APPENDIX = "BBBBBBBBBB";

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private PortfolioSearchRepository portfolioSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPortfolioMockMvc;

    private Portfolio portfolio;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PortfolioResource portfolioResource = new PortfolioResource(portfolioRepository, portfolioSearchRepository);
        this.restPortfolioMockMvc = MockMvcBuilders.standaloneSetup(portfolioResource)
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
    public static Portfolio createEntity(EntityManager em) {
        Portfolio portfolio = new Portfolio()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .titleImage(DEFAULT_TITLE_IMAGE)
            .appendix(DEFAULT_APPENDIX);
        return portfolio;
    }

    @Before
    public void initTest() {
        portfolioSearchRepository.deleteAll();
        portfolio = createEntity(em);
    }

    @Test
    @Transactional
    public void createPortfolio() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolio)))
            .andExpect(status().isCreated());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate + 1);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPortfolio.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPortfolio.getTitleImage()).isEqualTo(DEFAULT_TITLE_IMAGE);
        assertThat(testPortfolio.getAppendix()).isEqualTo(DEFAULT_APPENDIX);

        // Validate the Portfolio in Elasticsearch
        Portfolio portfolioEs = portfolioSearchRepository.findOne(testPortfolio.getId());
        assertThat(portfolioEs).isEqualToComparingFieldByField(testPortfolio);
    }

    @Test
    @Transactional
    public void createPortfolioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = portfolioRepository.findAll().size();

        // Create the Portfolio with an existing ID
        portfolio.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolio)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = portfolioRepository.findAll().size();
        // set the field null
        portfolio.setTitle(null);

        // Create the Portfolio, which fails.

        restPortfolioMockMvc.perform(post("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolio)))
            .andExpect(status().isBadRequest());

        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPortfolios() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get all the portfolioList
        restPortfolioMockMvc.perform(get("/api/portfolios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].titleImage").value(hasItem(DEFAULT_TITLE_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].appendix").value(hasItem(DEFAULT_APPENDIX.toString())));
    }

    @Test
    @Transactional
    public void getPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);

        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", portfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(portfolio.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.titleImage").value(DEFAULT_TITLE_IMAGE.toString()))
            .andExpect(jsonPath("$.appendix").value(DEFAULT_APPENDIX.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPortfolio() throws Exception {
        // Get the portfolio
        restPortfolioMockMvc.perform(get("/api/portfolios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);
        portfolioSearchRepository.save(portfolio);
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Update the portfolio
        Portfolio updatedPortfolio = portfolioRepository.findOne(portfolio.getId());
        updatedPortfolio
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .titleImage(UPDATED_TITLE_IMAGE)
            .appendix(UPDATED_APPENDIX);

        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPortfolio)))
            .andExpect(status().isOk());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate);
        Portfolio testPortfolio = portfolioList.get(portfolioList.size() - 1);
        assertThat(testPortfolio.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPortfolio.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPortfolio.getTitleImage()).isEqualTo(UPDATED_TITLE_IMAGE);
        assertThat(testPortfolio.getAppendix()).isEqualTo(UPDATED_APPENDIX);

        // Validate the Portfolio in Elasticsearch
        Portfolio portfolioEs = portfolioSearchRepository.findOne(testPortfolio.getId());
        assertThat(portfolioEs).isEqualToComparingFieldByField(testPortfolio);
    }

    @Test
    @Transactional
    public void updateNonExistingPortfolio() throws Exception {
        int databaseSizeBeforeUpdate = portfolioRepository.findAll().size();

        // Create the Portfolio

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPortfolioMockMvc.perform(put("/api/portfolios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(portfolio)))
            .andExpect(status().isCreated());

        // Validate the Portfolio in the database
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);
        portfolioSearchRepository.save(portfolio);
        int databaseSizeBeforeDelete = portfolioRepository.findAll().size();

        // Get the portfolio
        restPortfolioMockMvc.perform(delete("/api/portfolios/{id}", portfolio.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean portfolioExistsInEs = portfolioSearchRepository.exists(portfolio.getId());
        assertThat(portfolioExistsInEs).isFalse();

        // Validate the database is empty
        List<Portfolio> portfolioList = portfolioRepository.findAll();
        assertThat(portfolioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPortfolio() throws Exception {
        // Initialize the database
        portfolioRepository.saveAndFlush(portfolio);
        portfolioSearchRepository.save(portfolio);

        // Search the portfolio
        restPortfolioMockMvc.perform(get("/api/_search/portfolios?query=id:" + portfolio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(portfolio.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].titleImage").value(hasItem(DEFAULT_TITLE_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].appendix").value(hasItem(DEFAULT_APPENDIX.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Portfolio.class);
    }
}
