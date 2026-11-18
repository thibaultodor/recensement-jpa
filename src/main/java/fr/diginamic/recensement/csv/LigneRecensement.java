package fr.diginamic.recensement.csv;

/** Données utiles d'une ligne du CSV. */
public class LigneRecensement {

    private final String codeRegion;
    private final String nomRegion;
    private final String codeDepartement;
    private final String codeCommune;
    private final String nomCommune;
    private final int populationTotale;

    public LigneRecensement(String codeRegion, String nomRegion, String codeDepartement, String codeCommune, String nomCommune, int populationTotale) {
        this.codeRegion = codeRegion;
        this.nomRegion = nomRegion;
        this.codeDepartement = codeDepartement;
        this.codeCommune = codeCommune;
        this.nomCommune = nomCommune;
        this.populationTotale = populationTotale;
    }

    public String getCodeRegion() {
        return codeRegion;
    }

    public String getNomRegion() {
        return nomRegion;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public String getCodeCommune() {
        return codeCommune;
    }

    public String getNomCommune() {
        return nomCommune;
    }

    public int getPopulationTotale() {
        return populationTotale;
    }
}
