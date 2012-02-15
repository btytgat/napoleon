package eu.comexis.napoleon.client.core.estate;

import static com.google.gwt.query.client.GQuery.$;

import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import eu.comexis.napoleon.client.utils.UiHelper;
import eu.comexis.napoleon.shared.model.Condo;
import eu.comexis.napoleon.shared.model.Country;
import eu.comexis.napoleon.shared.model.RealEstate;
import eu.comexis.napoleon.shared.model.RealEstateState;
import eu.comexis.napoleon.shared.model.Title;
import eu.comexis.napoleon.shared.model.City;
import eu.comexis.napoleon.shared.model.TypeOfRealEstate;
import eu.comexis.napoleon.shared.model.simple.SimpleOwner;
import eu.comexis.napoleon.shared.validation.ValidationMessage;

public class RealEstateUpdateView extends ViewImpl implements RealEstateUpdatePresenter.MyView {

  public interface Binder extends UiBinder<Widget, RealEstateUpdateView> {
  }

  private final Widget widget;
  private RealEstateUpdateUiHandlers presenter;

  @UiField
  TextBox reference;
  @UiField
  TextBox addressRealEstate;
  @UiField
  SuggestBox city;
  @UiField
  SuggestBox postalCode;
  @UiField
  SuggestBox country;
  @UiField
  TextBox condo;
  @UiField
  TextBox association;
  @UiField
  TextBox address;
  @UiField
  TextBox phoneNumber;
  @UiField
  TextBox mobileNumber;
  @UiField
  TextBox email;
  @UiField
  TextBox number;
  @UiField
  TextBox box;
  @UiField
  SuggestBox square;
  @UiField(provided = true)
  ListBox type;
  @UiField(provided = true)
  ListBox state;
  @UiField
  TextBox dimension;
  @UiField
  ListBox ownerName;

  @Inject
  public RealEstateUpdateView(final Binder binder) {
    init();
    widget = binder.createAndBindUi(this);
  }

  @Override
  public Widget asWidget() {
    return widget;
  }

  @Override
  public void displayError(String error) {
    Window.alert(error);
  }

  @Override
  public void displayValidationMessage(List<ValidationMessage> validationMessages) {
    // TODO Display the messages in a PopupPanel
    StringBuilder msgBuilder = new StringBuilder("Vueillez corriger les erreurs suivantes : \n\n");

    for (ValidationMessage msg : validationMessages) {
      msgBuilder.append(msg.getMessage()).append("\n");
    }

    Window.alert(msgBuilder.toString());

  }

  @Override
  public void fillCityList(List<String> cities) {
    MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) city.getSuggestOracle();
    oracle.clear();
    if (cities != null) {
      for (String sCity : cities) {
        oracle.add(sCity);
      }
    }
  }

  @Override
  public void fillCountryList(List<Country> countries) {
    MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) country.getSuggestOracle();
    oracle.clear();
    if (countries != null) {
      for (Country cnty : countries) {
        oracle.add(cnty.getName());
      }
    }
  }

  @Override
  public void fillOwnerList(List<SimpleOwner> owners) {
    ownerName.clear();
    for (SimpleOwner o : owners) {
      ownerName.addItem(o.getName(), o.getId());
    }
    ownerName.addItem("(...)", "(...)");
  }

  @Override
  public void fillPostalCodeList(List<String> postCdes) {
    MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) postalCode.getSuggestOracle();
    oracle.clear();
    if (postCdes != null) {
      for (String sPC : postCdes) {
        if (sPC != null) {
          oracle.add(sPC);
        }
      }
    }
  }

  @Override
  public void fillSquareList(List<String> squares) {
    MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) square.getSuggestOracle();
    oracle.clear();
    if (squares != null) {
      for (String sSquare : squares) {
        oracle.add(sSquare);
      }
    }
  }

  @Override
  public String getOwnerId() {
    return ownerName.getValue(ownerName.getSelectedIndex());
  }

  @Override
  public String getSelectedCountry() {
    return country.getValue();
  }

  @UiHandler("btnCancel")
  public void onCancel(ClickEvent e) {
    presenter.onButtonCancelClick();
  }

  @UiHandler("city")
  public void onCityChange(ValueChangeEvent<String> event) {
    square.setValue("");
  }

  @UiHandler("city")
  public void onCitySelect(SelectionEvent<SuggestOracle.Suggestion> event) {
    presenter.onCitySelect(event.getSelectedItem().getReplacementString());
    square.setValue("");
  }

  @UiHandler("country")
  public void onCountryChange(ValueChangeEvent<String> event) {
    city.setValue("");
    postalCode.setValue("");
  }

  @UiHandler("country")
  public void onCountrySelect(SelectionEvent<SuggestOracle.Suggestion> event) {
    presenter.onCountrySelect(event.getSelectedItem().getReplacementString());
    city.setValue("");
    postalCode.setValue("");
  }

  @UiHandler("postalCode")
  public void onPostalCodeChange(ValueChangeEvent<String> event) {
    city.setValue("");
  }

  @UiHandler("postalCode")
  public void onPostalCodeSelect(SelectionEvent<SuggestOracle.Suggestion> event) {
    presenter.onPostalCodeSelect(event.getSelectedItem().getReplacementString());
    city.setValue("");
  }

  @UiHandler("btnSave")
  public void onSave(ClickEvent e) {
    presenter.onButtonSaveClick();
  }

  @Override
  public void setRealEstate(RealEstate e, SimpleOwner o, Condo cdo) {
    // cleanup
    this.reference.setText("");
    this.addressRealEstate.setText("");
    this.number.setText("");
    this.box.setText("");
    this.square.setText("");
    this.dimension.setText("");
    this.condo.setText("");
    this.association.setText("");
    this.address.setText("");
    this.city.setText("");
    this.postalCode.setText("");
    this.country.setText("");
    this.email.setText("");
    this.mobileNumber.setText("");
    this.phoneNumber.setText("");
    UiHelper.selectTextItemBoxByValue(this.ownerName, "(...)");
    if (e != null) {
      this.reference.setText(e.getReference());
      this.country.setValue(e.getCountry());
      this.presenter.onCountrySelect(country.getValue());
      this.addressRealEstate.setText(e.getStreet());
      this.postalCode.setText(e.getPostalCode());
      this.presenter.onPostalCodeSelect(postalCode.getValue());
      this.city.setValue(o.getCity());
      this.presenter.onCitySelect(city.getValue());
      this.number.setText(e.getNumber());
      this.box.setText(e.getBox());
      this.square.setText(e.getSquare());
      this.dimension.setText(e.getDimension());
      UiHelper.selectTextItemBoxByValue(this.state, e.getState().name());
      UiHelper.selectTextItemBoxByValue(this.type, e.getType().name());
      UiHelper.selectTextItemBoxByValue(this.ownerName, o.getId());
    }
    if (cdo != null) {
      this.condo.setText(cdo.getName());
      this.association.setText(cdo.getHomeownerAssociation());
      this.address.setText(cdo.getStreet());
      this.email.setText(cdo.getEmail());
      this.mobileNumber.setText(cdo.getMobilePhoneNumber());
      this.phoneNumber.setText(cdo.getPhoneNumber());
    }

  }

  @Override
  public void setRealEstateUpdateUiHandler(RealEstateUpdateUiHandlers handler) {
    this.presenter = handler;
  }

  @Override
  public Condo updateCondo(Condo cdo) {
    if (!condo.getValue().isEmpty()) {
      if (cdo == null) {
        cdo = new Condo();
      }
      cdo.setName(condo.getValue());
      cdo.setHomeownerAssociation(association.getValue());
      cdo.setStreet(address.getValue());
      cdo.setEmail(email.getValue());
      cdo.setMobilePhoneNumber(mobileNumber.getValue());
      cdo.setPhoneNumber(phoneNumber.getValue());
      return cdo;
    }
    return null;
  }

  @Override
  public RealEstate updateRealEstate(RealEstate e) {
    e.setReference(reference.getValue());
    e.setStreet(addressRealEstate.getValue());
    e.setNumber(number.getValue());
    e.setBox(box.getValue());
    e.setDimension(dimension.getValue());
    e.setSquare(square.getValue());
    e.setType(TypeOfRealEstate.valueOf(type.getValue(type.getSelectedIndex())));
    e.setState(RealEstateState.valueOf(state.getValue(state.getSelectedIndex())));
    e.setPostalCode(postalCode.getValue());
    e.setCity(city.getValue());
    e.setCountry(country.getValue());
    return e;
  }

  private void init() {
    type = UiHelper.createListBoxForEnum(TypeOfRealEstate.class, "TypeOfRealEstate_", false);
    state = UiHelper.createListBoxForEnum(RealEstateState.class, "RealEstateState_", false);
  }
}