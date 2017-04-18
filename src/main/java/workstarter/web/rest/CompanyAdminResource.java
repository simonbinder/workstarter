package workstarter.web.rest;

import com.codahale.metrics.annotation.Timed;
import workstarter.domain.CompanyAdmin;

import workstarter.repository.CompanyAdminRepository;
import workstarter.repository.search.CompanyAdminSearchRepository;
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
 * REST controller for managing CompanyAdmin.
 */
@RestController
@RequestMapping("/api")
public class CompanyAdminResource {

    private final Logger log = LoggerFactory.getLogger(CompanyAdminResource.class);

    private static final String ENTITY_NAME = "companyAdmin";
        
    private final CompanyAdminRepository companyAdminRepository;

    private final CompanyAdminSearchRepository companyAdminSearchRepository;

    public CompanyAdminResource(CompanyAdminRepository companyAdminRepository, CompanyAdminSearchRepository companyAdminSearchRepository) {
        this.companyAdminRepository = companyAdminRepository;
        this.companyAdminSearchRepository = companyAdminSearchRepository;
    }

    /**
     * POST  /company-admins : Create a new companyAdmin.
     *
     * @param companyAdmin the companyAdmin to create
     * @return the ResponseEntity with status 201 (Created) and with body the new companyAdmin, or with status 400 (Bad Request) if the companyAdmin has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/company-admins")
    @Timed
    public ResponseEntity<CompanyAdmin> createCompanyAdmin(@RequestBody CompanyAdmin companyAdmin) throws URISyntaxException {
        log.debug("REST request to save CompanyAdmin : {}", companyAdmin);
        if (companyAdmin.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new companyAdmin cannot already have an ID")).body(null);
        }
        CompanyAdmin result = companyAdminRepository.save(companyAdmin);
        companyAdminSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/company-admins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /company-admins : Updates an existing companyAdmin.
     *
     * @param companyAdmin the companyAdmin to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated companyAdmin,
     * or with status 400 (Bad Request) if the companyAdmin is not valid,
     * or with status 500 (Internal Server Error) if the companyAdmin couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/company-admins")
    @Timed
    public ResponseEntity<CompanyAdmin> updateCompanyAdmin(@RequestBody CompanyAdmin companyAdmin) throws URISyntaxException {
        log.debug("REST request to update CompanyAdmin : {}", companyAdmin);
        if (companyAdmin.getId() == null) {
            return createCompanyAdmin(companyAdmin);
        }
        CompanyAdmin result = companyAdminRepository.save(companyAdmin);
        companyAdminSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, companyAdmin.getId().toString()))
            .body(result);
    }

    /**
     * GET  /company-admins : get all the companyAdmins.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of companyAdmins in body
     */
    @GetMapping("/company-admins")
    @Timed
    public List<CompanyAdmin> getAllCompanyAdmins() {
        log.debug("REST request to get all CompanyAdmins");
        List<CompanyAdmin> companyAdmins = companyAdminRepository.findAll();
        return companyAdmins;
    }

    /**
     * GET  /company-admins/:id : get the "id" companyAdmin.
     *
     * @param id the id of the companyAdmin to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the companyAdmin, or with status 404 (Not Found)
     */
    @GetMapping("/company-admins/{id}")
    @Timed
    public ResponseEntity<CompanyAdmin> getCompanyAdmin(@PathVariable Long id) {
        log.debug("REST request to get CompanyAdmin : {}", id);
        CompanyAdmin companyAdmin = companyAdminRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(companyAdmin));
    }

    /**
     * DELETE  /company-admins/:id : delete the "id" companyAdmin.
     *
     * @param id the id of the companyAdmin to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/company-admins/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompanyAdmin(@PathVariable Long id) {
        log.debug("REST request to delete CompanyAdmin : {}", id);
        companyAdminRepository.delete(id);
        companyAdminSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/company-admins?query=:query : search for the companyAdmin corresponding
     * to the query.
     *
     * @param query the query of the companyAdmin search 
     * @return the result of the search
     */
    @GetMapping("/_search/company-admins")
    @Timed
    public List<CompanyAdmin> searchCompanyAdmins(@RequestParam String query) {
        log.debug("REST request to search CompanyAdmins for query {}", query);
        return StreamSupport
            .stream(companyAdminSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
