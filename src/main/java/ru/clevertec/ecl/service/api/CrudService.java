package ru.clevertec.ecl.service.api;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<RESP> findAll(Pageable pageable);

    RESP find(ID id);
}

