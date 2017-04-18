package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.Searching;

import workstarter.repository.SearchingRepository;
import workstarter.repository.search.SearchingSearchRepository;
import workstarter.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Searching.
 */
@RestController
@RequestMapping("/api")
public class SearchingResource {

    private final Logger log = LoggerFactory.getLogger(SearchingResource.class);

    private static final String ENTITY_NAME = "searching";
        
    private final SearchingRepository searchingRepository;

    private final SearchingSearchRepository searchingSearchRepository;

    public SearchingResource(SearchingRepository searchingRepository, SearchingSearchRepository searchingSearchRepository) {
        this.searchingRepository = searchingRepository;
        this.searchingSearchRepository = searchingSearchRepository;
    }

    /**
     * POST  /searchings : Create a new searching.
     *
     * @param searching the searching to create
     * @return the ResponseEntity with status 201 (Created) and with body the new searching, or with status 400 (Bad Request) if the searching has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/searchings")
    @Timed
    public ResponseEntity<Searching> createSearching(@RequestBody Searching searching) throws URISyntaxException {
        log.debug("REST request to save Searching : {}", searching);
        if (searching.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new searching cannot already have an ID")).body(null);
        }
        Searching result = searchingRepository.save(searching);
        searchingSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/searchings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /searchings : Updates an existing searching.
     *
     * @param searching the searching to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated searching,
     * or with status 400 (Bad Request) if the searching is not valid,
     * or with status 500 (Internal Server Error) if the searching couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/searchings")
    @Timed
    public ResponseEntity<Searching> updateSearching(@RequestBody Searching searching) throws URISyntaxException {
        log.debug("REST request to update Searching : {}", searching);
        if (searching.getId() == null) {
            return createSearching(searching);
        }
        Searching result = searchingRepository.save(searching);
        searchingSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, searching.getId().toString()))
            .body(result);
    }

    /**
     * GET  /searchings : get all the searchings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of searchings in body
     */
    @GetMapping("/searchings")
    @Timed
    public List<Searching> getAllSearchings() {
        log.debug("REST request to get all Searchings");
        List<Searching> searchings = searchingRepository.findAll();
        return searchings;
    }

    /**
     * GET  /searchings/:id : get the "id" searching.
     *
     * @param id the id of the searching to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the searching, or with status 404 (Not Found)
     */
    @GetMapping("/searchings/{id}")
    @Timed
    public ResponseEntity<Searching> getSearching(@PathVariable Long id) {
        log.debug("REST request to get Searching : {}", id);
        Searching searching = searchingRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(searching));
    }

    /**
     * DELETE  /searchings/:id : delete the "id" searching.
     *
     * @param id the id of the searching to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/searchings/{id}")
    @Timed
    public ResponseEntity<Void> deleteSearching(@PathVariable Long id) {
        log.debug("REST request to delete Searching : {}", id);
        searchingRepository.delete(id);
        searchingSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/searchings?query=:query : search for the searching corresponding
     * to the query.
     *
     * @param query the query of the searching search 
     * @return the result of the search
     */
    @GetMapping("/_search/searchings")
    @Timed
    public List<Searching> searchSearchings(@RequestParam String query) {
        log.debug("REST request to search Searchings for query {}", query);
        return StreamSupport
            .stream(searchingSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
