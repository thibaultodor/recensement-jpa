package fr.diginamic.recensement;

import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.Session;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/** Vérifie la connexion à MariaDB. XAMPP doit être démarré. */
@Tag("integration")
public class ConnexionJpaTest {

    @Test
    void testConnexionALaBaseRecensement() {
        // Charge la configuration JPA.
        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("recensement-jpa");
             EntityManager em = factory.createEntityManager()) {

            // Vérifie que la base répond.
            em.unwrap(Session.class).doWork(connexion -> {
                assertTrue(connexion.isValid(5), "MariaDB doit fournir une connexion valide.");
            });
        }
    }
}
