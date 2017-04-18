package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.School;

import workstarter.repository.SchoolRepository;
import workstarter.repository.search.SchoolSearchRepository;
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
 * REST controller for managing School.
 */
@RestController
@RequestMapping("/api")
public class SchoolResource {

    private final Logger log = LoggerFactory.getLogger(SchoolResource.class);

    private static final String ENTITY_NAME = "school";
        
    private final SchoolRepository schoolRepository;

    private final SchoolSearchRepository schoolSearchRepository;

    public SchoolResource(SchoolRepository schoolRepository, SchoolSearchRepository schoolSearchRepository) {
        this.schoolRepository = schoolRepository;
        this.schoolSearchRepository = schoolSearchRepository;
    }

    /**
     * POST  /schools : Create a new school.
     *
     * @param school the school to create
     * @return the ResponseEntity with status 201 (Created) and with body the new school, or with status 400 (Bad Request) if the school has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/schools")
    @Timed
    public ResponseEntity<School> createSchool(@Valid @RequestBody School school) throws URISyntaxException {
        log.debug("REST request to save School : {}", school);
        if (school.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new school cannot already have an ID")).body(null);
        }
        School result = schoolRepository.save(school);
        schoolSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/schools/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /schools : Updates an existing school.
     *
     * @param school the school to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated school,
     * or with status 400 (Bad Request) if the school is not valid,
     * or with status 500 (Internal Server Error) if the school couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/schools")
    @Timed
    public ResponseEntity<School> updateSchool(@Valid @RequestBody School school) throws URISyntaxException {
        log.debug("REST request to update School : {}", school);
        if (school.getId() == null) {
            return createSchool(school);
        }
        School result = schoolRepository.save(school);
        schoolSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, school.getId().toString()))
            .body(result);
    }

    /**
     * GET  /schools : get all the schools.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of schools in body
     */
    @GetMapping("/schools")
    @Timed
    public List<School> getAllSchools() {
        log.debug("REST request to get all Schools");
        List<School> schools = schoolRepository.findAll();
        return schools;
    }

    /**
     * GET  /schools/:id : get the "id" school.
     *
     * @param id the id of the school to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the school, or with status 404 (Not Found)
     */
    @GetMapping("/schools/{id}")
    @Timed
    public ResponseEntity<School> getSchool(@PathVariable Long id) {
        log.debug("REST request to get School : {}", id);
        School school = schoolRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(school));
    }

    /**
     * DELETE  /schools/:id : delete the "id" school.
     *
     * @param id the id of the school to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/schools/{id}")
    @Timed
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        log.debug("REST request to delete School : {}", id);
        schoolRepository.delete(id);
        schoolSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/schools?query=:query : search for the school corresponding
     * to the query.
     *
     * @param query the query of the school search 
     * @return the result of the search
     */
    @GetMapping("/_search/schools")
    @Timed
    public List<School> searchSchools(@RequestParam String query) {
        log.debug("REST request to search Schools for query {}", query);
        return StreamSupport
            .stream(schoolSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
