package ru.clevertec.ecl.service.api;


import java.util.List;
import java.util.Optional;

/**
 * Interface that present service for CRUD operations
 * with different incoming and return types.
 *
 * @param <REQ>  incoming type
 * @param <RESP> return type
 * @param <ID>   id type
 */
public interface CrudService<REQ, RESP, ID> {

    void delete(ID id);

    RESP save(REQ entity);

    RESP update(ID id, REQ entity);

    List<RESP> findAll();

    RESP find(ID id);
}
