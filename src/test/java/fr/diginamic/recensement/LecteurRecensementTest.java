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
        // Lit les communes et complète les noms des départements.
        LecteurRecensement lecteur = new LecteurRecensement();
        List<LigneRecensement> lignes = lecteur.read(
                Path.of("data", "Communes.csv"), Path.of("data", "Departements.csv"));

        assertEquals(35340, lignes.size());

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
        assertEquals("Hérault", montpellier.getNomDepartement());
        assertEquals(286098, montpellier.getPopulationTotale());
    }

    @Test
    void testRegroupementArrondissementsLyon() throws IOException {
        LecteurRecensement lecteur = new LecteurRecensement();
        List<LigneRecensement> lignes = lecteur.read(
                Path.of("data", "Communes.csv"), Path.of("data", "Departements.csv"));

        // Cherche Lyon et vérifie que ses arrondissements ont disparu.
        LigneRecensement lyon = null;
        int nombreLyon = 0;
        int nombreArrondissements = 0;
        int populationTotale = 0;
        for (LigneRecensement ligne : lignes) {
            populationTotale += ligne.getPopulationTotale();
            if ("69".equals(ligne.getCodeDepartement())) {
                if ("Lyon".equals(ligne.getNomCommune())) {
                    lyon = ligne;
                    nombreLyon++;
                } else if (ligne.getNomCommune().startsWith("Lyon ")
                        && ligne.getNomCommune().endsWith(" Arrondissement")) {
                    nombreArrondissements++;
                }
            }
        }

        assertEquals(1, nombreLyon);
        assertEquals(0, nombreArrondissements);
        assertNotNull(lyon);
        assertEquals("123", lyon.getCodeCommune());
        assertEquals(523164, lyon.getPopulationTotale());
        // Le regroupement ne change pas la population totale du fichier.
        assertEquals(67751838, populationTotale);
    }
}
