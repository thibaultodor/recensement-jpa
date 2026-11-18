package fr.diginamic.recensement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import fr.diginamic.recensement.csv.LecteurRecensement;
import fr.diginamic.recensement.csv.LigneRecensement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class LecteurRecensementTest {

    @Test
    void testLectureCsv() throws IOException {
        // Lit le fichier Communes.csv
        LecteurRecensement lecteur = new LecteurRecensement();
        List<LigneRecensement> lignes = lecteur.read(Path.of("data", "Communes.csv"));

        assertEquals(35348, lignes.size());

        // Cherche Montpellier dans la liste.
        LigneRecensement montpellier = null;
        for (LigneRecensement ligne : lignes) {
            if (ligne.getNomCommune().equals("Montpellier")) {
                montpellier = ligne;
                break;
            }
        }
        // Vérifie les données de Montpellier.
        assertNotNull(montpellier);
        assertEquals("34", montpellier.getCodeDepartement());
        assertEquals(286098, montpellier.getPopulationTotale());
    }
}
