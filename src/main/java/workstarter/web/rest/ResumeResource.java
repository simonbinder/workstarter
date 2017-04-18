package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.Resume;

import workstarter.repository.ResumeRepository;
import workstarter.repository.search.ResumeSearchRepository;
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
 * REST controller for managing Resume.
 */
@RestController
@RequestMapping("/api")
public class ResumeResource {

    private final Logger log = LoggerFactory.getLogger(ResumeResource.class);

    private static final String ENTITY_NAME = "resume";
        
    private final ResumeRepository resumeRepository;

    private final ResumeSearchRepository resumeSearchRepository;

    public ResumeResource(ResumeRepository resumeRepository, ResumeSearchRepository resumeSearchRepository) {
        this.resumeRepository = resumeRepository;
        this.resumeSearchRepository = resumeSearchRepository;
    }

    /**
     * POST  /resumes : Create a new resume.
     *
     * @param resume the resume to create
     * @return the ResponseEntity with status 201 (Created) and with body the new resume, or with status 400 (Bad Request) if the resume has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/resumes")
    @Timed
    public ResponseEntity<Resume> createResume(@Valid @RequestBody Resume resume) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resume);
        if (resume.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new resume cannot already have an ID")).body(null);
        }
        Resume result = resumeRepository.save(resume);
        resumeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/resumes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /resumes : Updates an existing resume.
     *
     * @param resume the resume to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated resume,
     * or with status 400 (Bad Request) if the resume is not valid,
     * or with status 500 (Internal Server Error) if the resume couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/resumes")
    @Timed
    public ResponseEntity<Resume> updateResume(@Valid @RequestBody Resume resume) throws URISyntaxException {
        log.debug("REST request to update Resume : {}", resume);
        if (resume.getId() == null) {
            return createResume(resume);
        }
        Resume result = resumeRepository.save(resume);
        resumeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, resume.getId().toString()))
            .body(result);
    }

    /**
     * GET  /resumes : get all the resumes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of resumes in body
     */
    @GetMapping("/resumes")
    @Timed
    public List<Resume> getAllResumes() {
        log.debug("REST request to get all Resumes");
        List<Resume> resumes = resumeRepository.findAll();
        return resumes;
    }

    /**
     * GET  /resumes/:id : get the "id" resume.
     *
     * @param id the id of the resume to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the resume, or with status 404 (Not Found)
     */
    @GetMapping("/resumes/{id}")
    @Timed
    public ResponseEntity<Resume> getResume(@PathVariable Long id) {
        log.debug("REST request to get Resume : {}", id);
        Resume resume = resumeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(resume));
    }

    /**
     * DELETE  /resumes/:id : delete the "id" resume.
     *
     * @param id the id of the resume to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/resumes/{id}")
    @Timed
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) {
        log.debug("REST request to delete Resume : {}", id);
        resumeRepository.delete(id);
        resumeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/resumes?query=:query : search for the resume corresponding
     * to the query.
     *
     * @param query the query of the resume search 
     * @return the result of the search
     */
    @GetMapping("/_search/resumes")
    @Timed
    public List<Resume> searchResumes(@RequestParam String query) {
        log.debug("REST request to search Resumes for query {}", query);
        return StreamSupport
            .stream(resumeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
