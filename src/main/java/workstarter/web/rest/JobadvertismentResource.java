package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.Jobadvertisment;

import workstarter.repository.JobadvertismentRepository;
import workstarter.repository.search.JobadvertismentSearchRepository;
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
 * REST controller for managing Jobadvertisment.
 */
@RestController
@RequestMapping("/api")
public class JobadvertismentResource {

    private final Logger log = LoggerFactory.getLogger(JobadvertismentResource.class);

    private static final String ENTITY_NAME = "jobadvertisment";
        
    private final JobadvertismentRepository jobadvertismentRepository;

    private final JobadvertismentSearchRepository jobadvertismentSearchRepository;

    public JobadvertismentResource(JobadvertismentRepository jobadvertismentRepository, JobadvertismentSearchRepository jobadvertismentSearchRepository) {
        this.jobadvertismentRepository = jobadvertismentRepository;
        this.jobadvertismentSearchRepository = jobadvertismentSearchRepository;
    }

    /**
     * POST  /jobadvertisments : Create a new jobadvertisment.
     *
     * @param jobadvertisment the jobadvertisment to create
     * @return the ResponseEntity with status 201 (Created) and with body the new jobadvertisment, or with status 400 (Bad Request) if the jobadvertisment has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/jobadvertisments")
    @Timed
    public ResponseEntity<Jobadvertisment> createJobadvertisment(@Valid @RequestBody Jobadvertisment jobadvertisment) throws URISyntaxException {
        log.debug("REST request to save Jobadvertisment : {}", jobadvertisment);
        if (jobadvertisment.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new jobadvertisment cannot already have an ID")).body(null);
        }
        Jobadvertisment result = jobadvertismentRepository.save(jobadvertisment);
        jobadvertismentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/jobadvertisments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /jobadvertisments : Updates an existing jobadvertisment.
     *
     * @param jobadvertisment the jobadvertisment to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated jobadvertisment,
     * or with status 400 (Bad Request) if the jobadvertisment is not valid,
     * or with status 500 (Internal Server Error) if the jobadvertisment couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/jobadvertisments")
    @Timed
    public ResponseEntity<Jobadvertisment> updateJobadvertisment(@Valid @RequestBody Jobadvertisment jobadvertisment) throws URISyntaxException {
        log.debug("REST request to update Jobadvertisment : {}", jobadvertisment);
        if (jobadvertisment.getId() == null) {
            return createJobadvertisment(jobadvertisment);
        }
        Jobadvertisment result = jobadvertismentRepository.save(jobadvertisment);
        jobadvertismentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, jobadvertisment.getId().toString()))
            .body(result);
    }

    /**
     * GET  /jobadvertisments : get all the jobadvertisments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of jobadvertisments in body
     */
    @GetMapping("/jobadvertisments")
    @Timed
    public List<Jobadvertisment> getAllJobadvertisments() {
        log.debug("REST request to get all Jobadvertisments");
        List<Jobadvertisment> jobadvertisments = jobadvertismentRepository.findAll();
        return jobadvertisments;
    }

    /**
     * GET  /jobadvertisments/:id : get the "id" jobadvertisment.
     *
     * @param id the id of the jobadvertisment to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the jobadvertisment, or with status 404 (Not Found)
     */
    @GetMapping("/jobadvertisments/{id}")
    @Timed
    public ResponseEntity<Jobadvertisment> getJobadvertisment(@PathVariable Long id) {
        log.debug("REST request to get Jobadvertisment : {}", id);
        Jobadvertisment jobadvertisment = jobadvertismentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(jobadvertisment));
    }

    /**
     * DELETE  /jobadvertisments/:id : delete the "id" jobadvertisment.
     *
     * @param id the id of the jobadvertisment to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/jobadvertisments/{id}")
    @Timed
    public ResponseEntity<Void> deleteJobadvertisment(@PathVariable Long id) {
        log.debug("REST request to delete Jobadvertisment : {}", id);
        jobadvertismentRepository.delete(id);
        jobadvertismentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/jobadvertisments?query=:query : search for the jobadvertisment corresponding
     * to the query.
     *
     * @param query the query of the jobadvertisment search 
     * @return the result of the search
     */
    @GetMapping("/_search/jobadvertisments")
    @Timed
    public List<Jobadvertisment> searchJobadvertisments(@RequestParam String query) {
        log.debug("REST request to search Jobadvertisments for query {}", query);
        return StreamSupport
            .stream(jobadvertismentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
