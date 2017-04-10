package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.Offering;

import workstarter.repository.OfferingRepository;
import workstarter.repository.search.OfferingSearchRepository;
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
 * REST controller for managing Offering.
 */
@RestController
@RequestMapping("/api")
public class OfferingResource {

    private final Logger log = LoggerFactory.getLogger(OfferingResource.class);

    private static final String ENTITY_NAME = "offering";
        
    private final OfferingRepository offeringRepository;

    private final OfferingSearchRepository offeringSearchRepository;

    public OfferingResource(OfferingRepository offeringRepository, OfferingSearchRepository offeringSearchRepository) {
        this.offeringRepository = offeringRepository;
        this.offeringSearchRepository = offeringSearchRepository;
    }

    /**
     * POST  /offerings : Create a new offering.
     *
     * @param offering the offering to create
     * @return the ResponseEntity with status 201 (Created) and with body the new offering, or with status 400 (Bad Request) if the offering has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/offerings")
    @Timed
    public ResponseEntity<Offering> createOffering(@RequestBody Offering offering) throws URISyntaxException {
        log.debug("REST request to save Offering : {}", offering);
        if (offering.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new offering cannot already have an ID")).body(null);
        }
        Offering result = offeringRepository.save(offering);
        offeringSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/offerings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /offerings : Updates an existing offering.
     *
     * @param offering the offering to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated offering,
     * or with status 400 (Bad Request) if the offering is not valid,
     * or with status 500 (Internal Server Error) if the offering couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/offerings")
    @Timed
    public ResponseEntity<Offering> updateOffering(@RequestBody Offering offering) throws URISyntaxException {
        log.debug("REST request to update Offering : {}", offering);
        if (offering.getId() == null) {
            return createOffering(offering);
        }
        Offering result = offeringRepository.save(offering);
        offeringSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, offering.getId().toString()))
            .body(result);
    }

    /**
     * GET  /offerings : get all the offerings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of offerings in body
     */
    @GetMapping("/offerings")
    @Timed
    public List<Offering> getAllOfferings() {
        log.debug("REST request to get all Offerings");
        List<Offering> offerings = offeringRepository.findAll();
        return offerings;
    }

    /**
     * GET  /offerings/:id : get the "id" offering.
     *
     * @param id the id of the offering to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the offering, or with status 404 (Not Found)
     */
    @GetMapping("/offerings/{id}")
    @Timed
    public ResponseEntity<Offering> getOffering(@PathVariable Long id) {
        log.debug("REST request to get Offering : {}", id);
        Offering offering = offeringRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(offering));
    }

    /**
     * DELETE  /offerings/:id : delete the "id" offering.
     *
     * @param id the id of the offering to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/offerings/{id}")
    @Timed
    public ResponseEntity<Void> deleteOffering(@PathVariable Long id) {
        log.debug("REST request to delete Offering : {}", id);
        offeringRepository.delete(id);
        offeringSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/offerings?query=:query : search for the offering corresponding
     * to the query.
     *
     * @param query the query of the offering search 
     * @return the result of the search
     */
    @GetMapping("/_search/offerings")
    @Timed
    public List<Offering> searchOfferings(@RequestParam String query) {
        log.debug("REST request to search Offerings for query {}", query);
        return StreamSupport
            .stream(offeringSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
