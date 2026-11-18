package fr.diginamic.recensement.csv;

/** Données utiles pour importer une commune. */
public class LigneRecensement {

    private final String codeRegion;
    private final String nomRegion;
    private final String codeDepartement;
    private final String nomDepartement;
    private final String codeCommune;
    private final String nomCommune;
    private final int populationTotale;

    public LigneRecensement(String codeRegion, String nomRegion, String codeDepartement, String nomDepartement, String codeCommune, String nomCommune, int populationTotale) {
        this.codeRegion = codeRegion;
        this.nomRegion = nomRegion;
        this.codeDepartement = codeDepartement;
        this.nomDepartement = nomDepartement;
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

    public String getNomDepartement() {
        return nomDepartement;
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
