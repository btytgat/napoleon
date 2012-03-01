package eu.comexis.napoleon.shared.validation;

import static eu.comexis.napoleon.client.resources.ValidationMessages.VALIDATION_MESSAGES;

import java.util.ArrayList;
import java.util.List;

import eu.comexis.napoleon.shared.model.Lease;

public class LeaseValidator extends AbstractValidator<Lease> {

  @Override
  public List<ValidationMessage> validate(Lease lease) {
    List<ValidationMessage> messages = new ArrayList<ValidationMessage>();
    
    validateRealEstate(lease, messages);
    validateTenant(lease, messages);
    
    return messages;
  }
  
  private void validateRealEstate(Lease lease, List<ValidationMessage> messages) {
    
    if (lease.getRealEstate()==null){
      messages.add(new ValidationMessage(VALIDATION_MESSAGES.fieldIsMandatory("référence"), "reference"));
    }
    
  }
  private void validateTenant(Lease lease, List<ValidationMessage> messages) {
    
    if (lease.getRealEstate()==null){
      messages.add(new ValidationMessage(VALIDATION_MESSAGES.fieldIsMandatory("locataire"), "tenantName"));
    }
    
  }

}
