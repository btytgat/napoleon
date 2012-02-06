package eu.comexis.napoleon.client.core.tenant;

import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import eu.comexis.napoleon.shared.model.Tenant;

public class TenantDetailsView extends ViewImpl implements
		TenantDetailsPresenter.MyView {

	private final Widget widget;
	private TenantDetailUiHandlers presenter;
	public interface Binder extends UiBinder<Widget, TenantDetailsView> {
	}
	
	@UiField
	Element name;
	@UiField
	Element firstName;
	@UiField
	Element email;
	@UiField
	Element phoneNumber;
	@UiField
	Element mobileNumber;
	@UiField
	Element birthDay;
	@UiField
	Element addresse;
	@UiField
	Element maritalStatus;
	@UiField(provided = true)
  Button btnUpdate;
	@UiField(provided = true)
  Button btnDelete;
	@UiField(provided = true)
  Button btnToDashBoard;

	@Inject
	public TenantDetailsView(final Binder binder) {
	  init();
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	private void init(){
	  btnUpdate = new Button("Editer",new ClickListener() {
      public void onClick(Widget sender) {
        presenter.onButtonUpdateClick();
      }
	  });
	  btnToDashBoard = new Button("Retour vers le tableau de bord",new ClickListener() {
      public void onClick(Widget sender) {
        presenter.onButtonBackToDashBoardClick();
      }
    });
	  btnDelete = new Button("Supprimer",new ClickListener() {
      public void onClick(Widget sender) {
        Window.alert("Supprimer");
      }
    });
	}
	@Override
  public void setTenantDetailUiHandler(TenantDetailUiHandlers handler) {
    this.presenter = handler;

  }
	@Override
	public void setTenant(Tenant o) {
		//TODO improve and continue
		
		name.setInnerText(o.getLastName());
		firstName.setInnerText(o.getFirstName());
		email.setInnerText(o.getEmail());
		phoneNumber.setInnerText(o.getPhoneNumber());
		mobileNumber.setInnerText(o.getMobilePhoneNumber());
		birthDay.setInnerText(o.getDateOfBirth() != null ? o.getDateOfBirth().toGMTString() : "");
		addresse.setInnerText(o.getStreet()+ " " +o.getCity()+ " " +o.getCountry());
		maritalStatus.setInnerText(o.getMaritalStatus() != null ? o.getMaritalStatus().name().toLowerCase() : "");
		
		
		// TODO Auto-generated method stub
		
	}
}