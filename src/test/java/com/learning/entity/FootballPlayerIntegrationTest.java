package com.learning.entity;

import com.learning.utils.DirtyDataInspector;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FootballPlayerIntegrationTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    DirtyDataInspector dirtyDataInspector;

    @Test
    @Transactional
    public void testDirtyEntities() {
        Session session = sessionFactory.getCurrentSession();

        FootballPlayer footballPlayer = session.get(FootballPlayer.class, 1);
        if (footballPlayer == null) {
            footballPlayer = new FootballPlayer();
            footballPlayer.setName("Original");
            session.save(footballPlayer);
            session.flush();
        }

        // Modification de l'entité
        footballPlayer.setName("Nouvel Nom");

        // Flush pour déclencher l'interceptor
        session.flush();

        // Récupération des entités dirty via l'interceptor
        Set<Object> dirtyEntities = dirtyDataInspector.getDirtyEntities();
        System.out.println("Entités dirty : " + dirtyEntities);

        // Vérifie qu'il y a bien une entité modifiée
        assertThat(dirtyEntities).isNotEmpty();
        assertThat(dirtyEntities).contains(footballPlayer);

        dirtyDataInspector.clear(); // optionnel pour reset après test
    }

}