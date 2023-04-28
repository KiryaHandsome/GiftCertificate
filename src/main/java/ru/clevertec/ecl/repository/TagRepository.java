package ru.clevertec.ecl.repository;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.repository.api.CrudRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagRepository implements CrudRepository<Tag, Integer> {

    private final SessionFactory sessionFactory;

    /**
     * Deletes tag by id.
     *
     * @param id id of tag for deleting
     */
    @Override
    public void delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Tag tag = session.find(Tag.class, id);
            if (tag == null)
                throw new EntityNotFoundException("No such tag to delete", id);
            session.remove(tag);
            session.getTransaction().commit();
        }
    }

    /**
     * Saves new tag.
     *
     * @param entity tag to create
     * @return tag with created id
     */
    @Override
    public Tag save(Tag entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
        return entity;
    }

    /**
     * Updates name of tag.
     *
     * @param id     of updatable tag
     * @param entity object with new tag name
     * @return updated tag
     */
    @Override
    public Tag update(Integer id, Tag entity) {
        Tag updatableTag;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            updatableTag = session.find(Tag.class, id);
            if(updatableTag == null)
                throw new EntityNotFoundException("No such certificate to update", id);
            updatableTag.setName(entity.getName());
            session.getTransaction().commit();
        }
        return updatableTag;
    }

    /**
     * Finds all tags.
     *
     * @return list of tags
     */
    @Override
    public List<Tag> findAll() {
        List<Tag> list;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            list = session.createQuery("SELECT a FROM Tag a", Tag.class).list();
            session.getTransaction().commit();
        }
        return list;
    }

    /**
     * Finds tag by id.
     *
     * @param id id of desired tag
     * @return optional of tag
     */
    @Override
    public Optional<Tag> find(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Tag tag = session.get(Tag.class, id);
            session.getTransaction().commit();
            return Optional.ofNullable(tag);
        }
    }
}
