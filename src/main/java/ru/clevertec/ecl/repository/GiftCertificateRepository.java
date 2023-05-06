package ru.clevertec.ecl.repository;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.exception.EntityNotFoundException;
import ru.clevertec.ecl.exception.InvalidOrderException;
import ru.clevertec.ecl.model.GiftCertificate;
import ru.clevertec.ecl.model.GiftCertificate_;
import ru.clevertec.ecl.model.Tag;
import ru.clevertec.ecl.model.Tag_;
import ru.clevertec.ecl.repository.api.IGiftCertificateRepository;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class GiftCertificateRepository implements IGiftCertificateRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public GiftCertificateRepository(SessionFactory factory) {
        this.sessionFactory = factory;
    }
    /**
     * Deletes certificate by id.
     *
     * @param id id of certificate for deleting
     */
    @Override
    public void delete(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            GiftCertificate certificate = session.find(GiftCertificate.class, id);
            if (certificate == null)
                throw new EntityNotFoundException("No such certificate to delete", id);
            session.remove(certificate);
            session.getTransaction().commit();
        }
    }

    /**
     * Saves new entity with tags. If tag isn't presented in database
     * then it creates new tag.
     *
     * @param entity entity to save
     * @return created certificate
     */
    @Override
    public GiftCertificate save(GiftCertificate entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
            entity.setCreateDate(currentTime);
            entity.setLastUpdateDate(currentTime);

            List<Tag> tags = entity.getTags();
            entity.setTags(new ArrayList<>());
            addTagsToCertificate(tags, entity, session);

            session.persist(entity);
            session.getTransaction().commit();
        }

        return entity;
    }

    private void addTagsToCertificate(List<Tag> tags, GiftCertificate certificate, Session session) {
        for (Tag tag : tags) {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
            Root<Tag> root = query.from(Tag.class);
            query.select(root)  //check if such tags already exist
                    .where(builder.equal(root.get(Tag_.name), tag.getName()));
            Tag foundTag = session.createQuery(query).getSingleResultOrNull();
            if (foundTag == null) {
                session.persist(tag);
                certificate.addTag(tag);
            } else {
                certificate.addTag(foundTag);
            }
        }
    }

    /**
     * Partial update of certificate.
     * <p>
     * It updates next fields if they are not null: description, name, duration, tags.
     * Calls {@link ru.clevertec.ecl.model.GiftCertificate#setLastUpdateDate(ZonedDateTime)} with current time.
     *
     * @param id     id of updatable certificate
     * @param entity object with fields to update
     * @return updated certificate
     */
    @Override
    public GiftCertificate update(Integer id, GiftCertificate entity) {
        GiftCertificate certificate;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            certificate = session.find(GiftCertificate.class, id);
            if (certificate == null)
                throw new EntityNotFoundException("No such certificate to update", id);
            setAttributesIfNotNull(entity, certificate, session);
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
            certificate.setLastUpdateDate(currentTime);
            session.getTransaction().commit();
        }

        return certificate;
    }

    private void setAttributesIfNotNull(GiftCertificate source, GiftCertificate destination, Session session) {
        if (source.getName() != null) {
            destination.setName(source.getName());
        }
        if (source.getDescription() != null) {
            destination.setDescription(source.getDescription());
        }
        if (source.getDuration() != null) {
            destination.setDuration(source.getDuration());
        }
        if (source.getTags() != null) {
            destination.setTags(new ArrayList<>());
            addTagsToCertificate(source.getTags(), destination, session);
        }
    }


    /**
     * Finds all certificates
     *
     * @return list of certificates
     */
    @Override
    public List<GiftCertificate> findAll() {
        List<GiftCertificate> list;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> query = builder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> root = query.from(GiftCertificate.class);
            query.select(root);
            list = session.createQuery(query).getResultList();
            session.getTransaction().commit();
        }

        return list;
    }

    /**
     * Finds certificate by id.
     *
     * @param id id of desired certificate
     * @return Optional of certificate
     */
    @Override
    public Optional<GiftCertificate> find(Integer id) {
        GiftCertificate certificate;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            certificate = session.get(GiftCertificate.class, id);
            session.getTransaction().commit();
        }

        return Optional.ofNullable(certificate);
    }

    /**
     * Finds all certificates considering passed parameters.
     * All params are optional.
     *
     * @param tagName     tag name. If it is presented then function returns list of certificates
     *                    which contains tag with such name.
     * @param sortByDate  sorting order by date. If it's presented then function returns list of certificates
     *                    sorted by date. Value must be either {@code asc} or {@code desc} (case doesn't matter).
     * @param sortByName  sorting order by name. If it's presented then function returns list of certificates
     *                    sorted by name. If it's used together with sortByDate it will sort by date firstly.
     *                    Value must be either {@code asc} or {@code desc} (case doesn't matter).
     * @param description part of description of desired certificates. If it's presented function will return certificates
     *                    which contain passed description as substring. Case-insensitive
     * @return list with found certificates
     */
    @Override
    public List<GiftCertificate> findAll(String tagName,
                                         String sortByDate,
                                         String sortByName,
                                         String description) {
        List<GiftCertificate> certificates;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
            criteriaQuery.select(root);
            //fetch to avoid n + 1 problem
            root.fetch(GiftCertificate_.tags, JoinType.LEFT);

            List<Predicate> wherePredicates = new ArrayList<>();
            if (tagName != null)
                wherePredicates.add(getTagNamePredicate(tagName, criteriaBuilder, criteriaQuery, root));
            if (description != null)
                wherePredicates.add(getDescriptionPredicate(description, criteriaBuilder, root));
            if (!wherePredicates.isEmpty())       //add predicates to list with 'WHERE' restriction
                criteriaQuery.where(wherePredicates.toArray(new Predicate[0]));

            List<Order> orderList = createOrderList(sortByDate, sortByName, criteriaBuilder, root);
            criteriaQuery.orderBy(orderList);

            TypedQuery<GiftCertificate> query = session.createQuery(criteriaQuery);
            certificates = query.getResultList();
            session.getTransaction().commit();
        }

        return certificates;
    }

    private Predicate getTagNamePredicate(String tagName,
                                          CriteriaBuilder builder,
                                          CriteriaQuery<GiftCertificate> criteriaQuery,
                                          Root<GiftCertificate> root) {
        Subquery<Tag> subquery = criteriaQuery.subquery(Tag.class);
        Root<Tag> subRoot = subquery.from(Tag.class);
        subquery.select(subRoot);
        subquery.where(builder.equal(subRoot.get(Tag_.name), tagName),
                builder.isMember(subRoot, root.get(GiftCertificate_.tags)));
        return builder.exists(subquery);
    }

    private Predicate getDescriptionPredicate(String description,
                                              HibernateCriteriaBuilder builder,
                                              Root<GiftCertificate> certificate) {
        String pattern = "%" + description + "%";
        return builder.ilike(certificate.get(GiftCertificate_.description), pattern);
    }

    /**
     * Creates list for 'ORDER BY' conditions
     */
    private List<Order> createOrderList(String sortByDate,
                                        String sortByName,
                                        CriteriaBuilder criteriaBuilder,
                                        Root<GiftCertificate> root) {
        List<Order> orderList = new ArrayList<>();
        if (sortByDate != null) {
            Path<ZonedDateTime> createDatePath = root.get(GiftCertificate_.createDate);
            Function<Expression<?>, Order> orderFunction = getOrder(sortByDate, criteriaBuilder);
            orderList.add(orderFunction.apply(createDatePath));
        }
        if (sortByName != null) {
            Path<String> namePath = root.get(GiftCertificate_.name);
            Function<Expression<?>, Order> orderFunction = getOrder(sortByName, criteriaBuilder);
            orderList.add(orderFunction.apply(namePath));
        }

        return orderList;
    }

    /**
     * Gets order function
     *
     * @return {@link java.util.function.Function} that presents
     * {@link jakarta.persistence.criteria.CriteriaBuilder#asc(Expression)} or
     * {@link jakarta.persistence.criteria.CriteriaBuilder#desc(Expression)}
     * @throws InvalidOrderException when order not 'asc' or 'desc'(case doesn't matter)
     */
    private Function<Expression<?>, Order> getOrder(String order, CriteriaBuilder criteriaBuilder) {
        if ("asc".equalsIgnoreCase(order)) {
            return criteriaBuilder::asc;
        } else if ("desc".equalsIgnoreCase(order)) {
            return criteriaBuilder::desc;
        }
        throw new InvalidOrderException("Passed order is invalid", order);
    }
}
