package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.Keywords;

import workstarter.repository.KeywordsRepository;
import workstarter.repository.search.KeywordsSearchRepository;
import workstarter.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Keywords.
 */
@RestController
@RequestMapping("/api")
public class KeywordsResource {

    private final Logger log = LoggerFactory.getLogger(KeywordsResource.class);

    private static final String ENTITY_NAME = "keywords";
        
    private final KeywordsRepository keywordsRepository;

    private final KeywordsSearchRepository keywordsSearchRepository;

    public KeywordsResource(KeywordsRepository keywordsRepository, KeywordsSearchRepository keywordsSearchRepository) {
        this.keywordsRepository = keywordsRepository;
        this.keywordsSearchRepository = keywordsSearchRepository;
    }

    /**
     * POST  /keywords : Create a new keywords.
     *
     * @param keywords the keywords to create
     * @return the ResponseEntity with status 201 (Created) and with body the new keywords, or with status 400 (Bad Request) if the keywords has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/keywords")
    @Timed
    public ResponseEntity<Keywords> createKeywords(@Valid @RequestBody Keywords keywords) throws URISyntaxException {
        log.debug("REST request to save Keywords : {}", keywords);
        if (keywords.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new keywords cannot already have an ID")).body(null);
        }
        Keywords result = keywordsRepository.save(keywords);
        keywordsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/keywords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /keywords : Updates an existing keywords.
     *
     * @param keywords the keywords to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated keywords,
     * or with status 400 (Bad Request) if the keywords is not valid,
     * or with status 500 (Internal Server Error) if the keywords couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/keywords")
    @Timed
    public ResponseEntity<Keywords> updateKeywords(@Valid @RequestBody Keywords keywords) throws URISyntaxException {
        log.debug("REST request to update Keywords : {}", keywords);
        if (keywords.getId() == null) {
            return createKeywords(keywords);
        }
        Keywords result = keywordsRepository.save(keywords);
        keywordsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, keywords.getId().toString()))
            .body(result);
    }

    /**
     * GET  /keywords : get all the keywords.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of keywords in body
     */
    @GetMapping("/keywords")
    @Timed
    public List<Keywords> getAllKeywords() {
        log.debug("REST request to get all Keywords");
        List<Keywords> keywords = keywordsRepository.findAll();
        return keywords;
    }

    /**
     * GET  /keywords/:id : get the "id" keywords.
     *
     * @param id the id of the keywords to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the keywords, or with status 404 (Not Found)
     */
    @GetMapping("/keywords/{id}")
    @Timed
    public ResponseEntity<Keywords> getKeywords(@PathVariable Long id) {
        log.debug("REST request to get Keywords : {}", id);
        Keywords keywords = keywordsRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(keywords));
    }

    /**
     * DELETE  /keywords/:id : delete the "id" keywords.
     *
     * @param id the id of the keywords to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/keywords/{id}")
    @Timed
    public ResponseEntity<Void> deleteKeywords(@PathVariable Long id) {
        log.debug("REST request to delete Keywords : {}", id);
        keywordsRepository.delete(id);
        keywordsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/keywords?query=:query : search for the keywords corresponding
     * to the query.
     *
     * @param query the query of the keywords search 
     * @return the result of the search
     */
    @GetMapping("/_search/keywords")
    @Timed
    public List<Keywords> searchKeywords(@RequestParam String query) {
        log.debug("REST request to search Keywords for query {}", query);
        return StreamSupport
            .stream(keywordsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
