package eu.comexis.napoleon.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface ValidationMessages extends Messages {
  
  public ValidationMessages VALIDATION_MESSAGES = GWT.create(ValidationMessages.class);
  
  @DefaultMessage("Le champ {0} est obligatoire.")
  public String fieldIsMandatory(String field);

  @DefaultMessage("Le champ {0} doit être inférieur à {1} .")
  public String fieldMustBeLessThan(String string, String string2);

  @DefaultMessage("Le champ {0} doit être supérieur ou égale à {1} .")
  public String fieldMustBeGreaterOrEqualsThan(String string, String string2);

  @DefaultMessage("L''addresse email ''{0}'' n''est pas une addresse email valide.")
  public String emailInvalid(String email);

}
