package fr.diginamic.recensement.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class LecteurDepartements {

    public Map<String, String> read(Path fichier) throws IOException {
        Map<String, String> nomsDepartements = new HashMap<>();

        try (BufferedReader lecteur = Files.newBufferedReader(fichier, StandardCharsets.UTF_8)) {
            // La première ligne contient les titres des colonnes.
            if (!"CODDEP;DEP".equals(lecteur.readLine())) {
                throw new IllegalArgumentException("En-tête du fichier Departements.csv invalide.");
            }

            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (!ligne.isBlank()) {
                    String[] colonnes = ligne.split(";", -1);
                    if (colonnes.length != 2) {
                        throw new IllegalArgumentException("Une ligne de Departements.csv doit contenir 2 colonnes.");
                    }
                    // Associe chaque code à son nom, par exemple 34 à Hérault.
                    nomsDepartements.put(colonnes[0].trim(), colonnes[1].trim());
                }
            }
        }
        return nomsDepartements;
    }
}
