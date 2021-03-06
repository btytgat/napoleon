package eu.comexis.napoleon.client.core.paymentTenant;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

import eu.comexis.napoleon.client.core.AbstractShortListPresenter;
import eu.comexis.napoleon.client.core.MainLayoutPresenter.Menus;
import eu.comexis.napoleon.client.core.lease.LeaseDetailsPresenter;
import eu.comexis.napoleon.client.place.NameTokens;
import eu.comexis.napoleon.client.resources.Literals;
import eu.comexis.napoleon.client.rpc.callback.GotAllPayment;
import eu.comexis.napoleon.shared.command.payment.GetAllPaymentCommand;
import eu.comexis.napoleon.shared.model.PaymentTenant;

public class PaymentTenantListPresenter extends
    AbstractShortListPresenter<PaymentTenant, PaymentTenantListPresenter.MyView, PaymentTenantListPresenter.MyProxy> {
  
  public static final String UUID_PARAMETER = "uuid";
  public static final String ESTATE_UUID_PARAMETER = "estate_uuid";
  public static final String LEASE_UUID_PARAMETER = "lease_uuid";
  private static final Logger LOG = Logger.getLogger(PaymentTenantListPresenter.class.getName());
  
  private String id;
  private String estateId;
  

  @ProxyCodeSplit
  @NameToken(NameTokens.paymentTenantlist)
  public interface MyProxy extends ProxyPlace<PaymentTenantListPresenter> {
  }

  public interface MyView extends AbstractShortListPresenter.MyView<PaymentTenant> {
  }

  @Inject
  public PaymentTenantListPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
      final PlaceManager placeManager) {
    super(eventBus, view, proxy, placeManager);

  }

  @Override
  protected String getDetailsNameTokens() {
    return NameTokens.paymentTenant;
  }

  @Override
  protected String getUpdateNameTokens() {
    return NameTokens.updatePaymentTenant;
  }
  @Override
  public void onButtonNewClick() {
    PlaceRequest myRequest = new PlaceRequest(getUpdateNameTokens());
    myRequest = myRequest.with(UUID_PARAMETER, "next");
    myRequest = myRequest.with(ESTATE_UUID_PARAMETER, estateId);
    myRequest = myRequest.with(LEASE_UUID_PARAMETER, id);
    super.getPlaceManager().revealPlace(myRequest);
  }
  @Override
  public void onButtonDeleteClick() {
    //
  }

  protected void requestData() {
    new GetAllPaymentCommand<PaymentTenant>(id,estateId,PaymentTenant.class.toString()).dispatch(new GotAllPayment<PaymentTenant>() {
      @Override
      public void got(String title,List<PaymentTenant> payments) {
        setDatas(title,payments);
      }
    });
  }
  
  @Override
  public void prepareFromRequest(PlaceRequest placeRequest) {
    super.prepareFromRequest(placeRequest);
    // In the next call, "view" is the default value,
    // returned if "action" is not found on the URL.
    id = placeRequest.getParameter(UUID_PARAMETER, null);
    this.estateId = placeRequest.getParameter(ESTATE_UUID_PARAMETER, null);
    
    if (id == null || id.length() == 0 
        || estateId == null || estateId.length() ==0) {
      if (LogConfiguration.loggingIsEnabled()) {
        LOG.severe("invalid id is null or empty");
      }
      super.getPlaceManager().revealErrorPlace(placeRequest.getNameToken());
    }
  }
  @Override
  public void onSelect(PaymentTenant data) {
    if (data == null){
      return;
    }
    
    PlaceRequest myRequest = new PlaceRequest(getUpdateNameTokens());
    // add the id of the owner to load
    myRequest = myRequest.with(UUID_PARAMETER, data.getId());
    myRequest = myRequest.with(ESTATE_UUID_PARAMETER, estateId);
    myRequest = myRequest.with(LEASE_UUID_PARAMETER, id);
    super.getPlaceManager().revealPlace(myRequest);
  }

  @Override
  protected Menus getMenu() {
    return Menus.REAL_ESTATE;
  }

  @Override
  protected String getTitle() {
    return Literals.INSTANCE.paymentListTitle();
  }
  @Override
  public void showParent() {
    LeaseDetailsPresenter.show(id, estateId);
  }
}
