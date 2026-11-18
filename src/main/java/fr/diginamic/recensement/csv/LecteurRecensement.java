package fr.diginamic.recensement.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LecteurRecensement {

    private static final String ENTETE = "Code région;Nom de la région;Code département;Code arrondissement;Code canton;Code commune;Nom de la commune;Population municipale;Population comptée à part;Population totale";

    public List<LigneRecensement> read(Path fichier, Path fichierDepartements) throws IOException {
        // Lit les noms des départements une seule fois, avant les communes.
        LecteurDepartements lecteurDepartements = new LecteurDepartements();
        Map<String, String> nomsDepartements = lecteurDepartements.read(fichierDepartements);
        List<LigneRecensement> lignes = new ArrayList<>();
        LigneRecensement arrondissementLyon = null;
        int populationLyon = 0;
        int nombreArrondissementsLyon = 0;

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
                    LigneRecensement commune = parseLine(ligne, numeroLigne, nomsDepartements);
                    // Dans ce fichier, Lyon est répartie sur neuf lignes.
                    if ("69".equals(commune.getCodeDepartement())
                            && commune.getNomCommune().startsWith("Lyon ")
                            && commune.getNomCommune().endsWith(" Arrondissement")) {
                        arrondissementLyon = commune;
                        populationLyon += commune.getPopulationTotale();
                        nombreArrondissementsLyon++;
                    } else {
                        lignes.add(commune);
                    }
                }
            }
        }

        if (arrondissementLyon != null) {
            if (nombreArrondissementsLyon != 9) {
                throw new IllegalArgumentException("Les 9 arrondissements de Lyon sont nécessaires au regroupement.");
            }
            // Ajoute une seule commune avec la somme des populations et le code de Lyon.
            lignes.add(new LigneRecensement(arrondissementLyon.getCodeRegion(), arrondissementLyon.getNomRegion(),
                    "69", arrondissementLyon.getNomDepartement(), "123", "Lyon", populationLyon));
        }
        return lignes;
    }

    private LigneRecensement parseLine(String ligne, int numeroLigne, Map<String, String> nomsDepartements) {
        // Le fichier fourni utilise le point-virgule comme séparateur.
        String[] colonnes = ligne.split(";", -1);

        // On vérifie que l'on a bien 10 colonnes.
        if (colonnes.length != 10) {
            throw new IllegalArgumentException("La ligne " + numeroLigne + " doit contenir 10 colonnes.");
        }

        // Retrouve le nom du département grâce à son code.
        String codeDepartement = colonnes[2].trim();
        String nomDepartement = nomsDepartements.get(codeDepartement);
        if (nomDepartement == null) {
            throw new IllegalArgumentException("Nom introuvable pour le département " + codeDepartement + " à la ligne " + numeroLigne + ".");
        }

        // La dernière colonne contient la population totale.
        int population;
        try {
            population = Integer.parseInt(colonnes[9].replace(" ", ""));
        } catch (NumberFormatException erreur) {
            throw new IllegalArgumentException("Population invalide à la ligne " + numeroLigne + ".", erreur);
        }

        // On utilise trim() afin de supprimer les espaces en début et fin de chaîne (facultatif).
        return new LigneRecensement(colonnes[0].trim(), colonnes[1].trim(), codeDepartement, nomDepartement,  colonnes[5].trim(), colonnes[6].trim(), population);
    }
}
