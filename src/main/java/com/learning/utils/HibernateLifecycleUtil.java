package com.learning.utils;

import jakarta.persistence.metamodel.EntityType;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Set;
import java.util.stream.Collectors;

public class HibernateLifecycleUtil {
    public static Set<String> getManagedEntities(Session session) {
        return session.getSessionFactory().getMetamodel().getEntities()
                .stream()
                .map(EntityType::getName)
                .collect(Collectors.toSet());
    }

    // Exécute un count(*) sur une entité donnée
    public static long queryCount(Session session, String entityName) {
        String hql = "SELECT COUNT(e) FROM " + entityName + " e";
        Query<Long> query = session.createQuery(hql, Long.class);
        return query.uniqueResult();
    }
}
