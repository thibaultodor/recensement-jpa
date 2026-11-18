package fr.diginamic.recensement;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fr.diginamic.recensement.csv.LecteurDepartements;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class LecteurDepartementsTest {

    @Test
    void testLectureDepartements() throws IOException {
        // Lit les codes et les noms du fichier.
        LecteurDepartements lecteur = new LecteurDepartements();
        Map<String, String> nomsDepartements = lecteur.read(Path.of("data", "Departements.csv"));

        // Vérifie l'Hérault et un département avec un code contenant une lettre.
        assertEquals(100, nomsDepartements.size());
        assertEquals("Hérault", nomsDepartements.get("34"));
        assertEquals("Corse-du-Sud", nomsDepartements.get("2A"));
    }
}
