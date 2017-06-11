package workstarter.service.impl;

import workstarter.service.ProfessionService;
import workstarter.domain.Profession;
import workstarter.repository.ProfessionRepository;
import workstarter.repository.search.ProfessionSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Profession.
 */
@Service
@Transactional
public class ProfessionServiceImpl implements ProfessionService{

    private final Logger log = LoggerFactory.getLogger(ProfessionServiceImpl.class);
    
    private final ProfessionRepository professionRepository;

    private final ProfessionSearchRepository professionSearchRepository;

    public ProfessionServiceImpl(ProfessionRepository professionRepository, ProfessionSearchRepository professionSearchRepository) {
        this.professionRepository = professionRepository;
        this.professionSearchRepository = professionSearchRepository;
    }

    /**
     * Save a profession.
     *
     * @param profession the entity to save
     * @return the persisted entity
     */
    @Override
    public Profession save(Profession profession) {
        log.debug("Request to save Profession : {}", profession);
        Profession result = professionRepository.save(profession);
        professionSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the professions.
     *  
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Profession> findAll() {
        log.debug("Request to get all Professions");
        List<Profession> result = professionRepository.findAll();

        return result;
    }

    /**
     *  Get one profession by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Profession findOne(Long id) {
        log.debug("Request to get Profession : {}", id);
        Profession profession = professionRepository.findOne(id);
        return profession;
    }

    /**
     *  Delete the  profession by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Profession : {}", id);
        professionRepository.delete(id);
        professionSearchRepository.delete(id);
    }

    /**
     * Search for the profession corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Profession> search(String query) {
        log.debug("Request to search Professions for query {}", query);
        return StreamSupport
            .stream(professionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
