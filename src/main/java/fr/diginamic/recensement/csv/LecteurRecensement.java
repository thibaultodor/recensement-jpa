package fr.diginamic.recensement.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LecteurRecensement {

    private static final String ENTETE = "Code région;Nom de la région;Code département;Code arrondissement;Code canton;Code commune;Nom de la commune;Population municipale;Population comptée à part;Population totale";

    public List<LigneRecensement> read(Path fichier) throws IOException {
        List<LigneRecensement> lignes = new ArrayList<>();

        try (BufferedReader lecteur = Files.newBufferedReader(fichier, StandardCharsets.UTF_8)) {
            // Vérifie la première ligne, qui contient les titres des colonnes.
            String entete = lecteur.readLine();
            if (!ENTETE.equals(entete)) {
                throw new IllegalArgumentException("En-tête du fichier Communes.csv invalide.");
            }

            String ligne;
            int numeroLigne = 1;
            while ((ligne = lecteur.readLine()) != null) {
                numeroLigne++;
                if (!ligne.isBlank()) {
                    lignes.add(parseLine(ligne, numeroLigne));
                }
            }
        }
        return lignes;
    }

    private LigneRecensement parseLine(String ligne, int numeroLigne) {
        // Le fichier fourni utilise le point-virgule comme séparateur.
        String[] colonnes = ligne.split(";", -1);

        // On vérifie que l'on a bien 10 colonnes.
        if (colonnes.length != 10) {
            throw new IllegalArgumentException("La ligne " + numeroLigne + " doit contenir 10 colonnes.");
        }

        // La dernière colonne contient la population totale.
        int population;
        try {
            population = Integer.parseInt(colonnes[9].replace(" ", ""));
        } catch (NumberFormatException erreur) {
            throw new IllegalArgumentException("Population invalide à la ligne " + numeroLigne + ".", erreur);
        }

        // On utilise trim() afin de supprimer les espaces en début et fin de chaîne (facultatif).
        return new LigneRecensement(colonnes[0].trim(), colonnes[1].trim(), colonnes[2].trim(), colonnes[5].trim(), colonnes[6].trim(), population);
    }
}
