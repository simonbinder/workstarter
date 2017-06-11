package workstarter.service;

import workstarter.domain.Profession;
import java.util.List;

/**
 * Service Interface for managing Profession.
 */
public interface ProfessionService {

    /**
     * Save a profession.
     *
     * @param profession the entity to save
     * @return the persisted entity
     */
    Profession save(Profession profession);

    /**
     *  Get all the professions.
     *  
     *  @return the list of entities
     */
    List<Profession> findAll();

    /**
     *  Get the "id" profession.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Profession findOne(Long id);

    /**
     *  Delete the "id" profession.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the profession corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @return the list of entities
     */
    List<Profession> search(String query);
}
