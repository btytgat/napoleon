package eu.comexis.napoleon.shared.model;

public enum TypeOfRent {
  KOT,NONE,OFFICE,COMMERCIAL,MEUBLE,PRINCIPAL,SECONDAIRE;
  public static TypeOfRent fromStringToEnum(String value) {
    for (TypeOfRent t : values()) {
      if (t.name().equals(value)) {
        return t;
      }
    }
    return null;
  }
}
