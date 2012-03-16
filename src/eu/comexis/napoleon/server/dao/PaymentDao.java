package eu.comexis.napoleon.server.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;
import com.googlecode.objectify.util.DAOBase;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import eu.comexis.napoleon.shared.model.AcademicYear;
import eu.comexis.napoleon.shared.model.ApplicationUser;
import eu.comexis.napoleon.shared.model.Association;
import eu.comexis.napoleon.shared.model.City;
import eu.comexis.napoleon.shared.model.Company;
import eu.comexis.napoleon.shared.model.Condo;
import eu.comexis.napoleon.shared.model.Country;
import eu.comexis.napoleon.shared.model.FeeUnit;
import eu.comexis.napoleon.shared.model.JobTitle;
import eu.comexis.napoleon.shared.model.Lease;
import eu.comexis.napoleon.shared.model.Nationality;
import eu.comexis.napoleon.shared.model.Owner;
import eu.comexis.napoleon.shared.model.Ownership;
import eu.comexis.napoleon.shared.model.Payment;
import eu.comexis.napoleon.shared.model.PaymentOwner;
import eu.comexis.napoleon.shared.model.PaymentTenant;
import eu.comexis.napoleon.shared.model.RealEstate;
import eu.comexis.napoleon.shared.model.Tenant;
import eu.comexis.napoleon.shared.model.simple.PaymentListItem;

public class PaymentDao<T extends Payment> extends DAOBase{
  protected Class<T> clazz;
  static {
    ObjectifyService.register(Payment.class);
    ObjectifyService.register(PaymentOwner.class);
    ObjectifyService.register(PaymentTenant.class);
  }
  public Class<T> getClazz() {
    return clazz;
  }
  public void setClazz(Class<T> clazz) {
    this.clazz = clazz;
  }
  public PaymentDao() {
  }
  public List<T> getPaymentsForLease(String leaseId, String realEstateId, String companyId){
    Key<Company> companyKey = new Key<Company>(Company.class, companyId);
    Key<RealEstate> estateKey = new Key<RealEstate>(companyKey,RealEstate.class, realEstateId);
    Key<Lease> leaseKey = new Key<Lease>(estateKey,Lease.class, leaseId);
    LOG.info("Get list payment (" + clazz + ") for lease " + leaseId);
    Query<T> q = ofy().query(clazz);
    q.ancestor(leaseKey);
    List<T> payments = q.list();
    return payments;
  }
  public static Log LOG = LogFactory.getLog(PaymentDao.class);


  public T getById(String paymentId, String leaseId, String realEstateId, String companyId) {
    Key<Company> companyKey = new Key<Company>(Company.class, companyId);
    Key<RealEstate> estateKey = new Key<RealEstate>(companyKey,RealEstate.class, realEstateId);
    Key<Lease> leaseKey = new Key<Lease>(estateKey,Lease.class, leaseId);
    T p = ofy().find(new Key<T>(leaseKey, clazz, paymentId));
    p.setLeaseId(leaseId);
    p.setEstateId(realEstateId);
    return p;
  }
  public List<T> getPaymentForLease(Key<Lease> leaseKey) {
    // get the lease for the company
    Query<T> ql = ofy().query(clazz);
    ql.ancestor(leaseKey);
    return ql.list();
  }
  public T isAlreadyPaid(String id,Key<Lease> leaseKey, Date startDate, Date endDate) {
    Query<T> q = ofy().query(clazz);
    q.ancestor(leaseKey);
    List<T> payments = q.filter("periodEndDate >=", startDate).order("periodEndDate").list();
    for (T p:payments){
      if (!p.getId().equals(id) && p.getPeriodStartDate().compareTo(endDate) <= 0){
        return p;
      }
    }
    return null;
  }
  public T update(T payment,String companyId) {
    Key<Company> companyKey = new Key<Company>(Company.class, companyId);
    return update(payment,companyKey);
  }
  public T update(T payment, Key<Company> companyKey) {
    LOG.info("Update Payment");
    try{
      String paymentId = payment.getId();
      Key<Lease> leaseKey = null;
      Key<RealEstate> estateKey = null;
      // create unique id if new entity
      
      if (paymentId == null || paymentId.length() == 0) {
        UUID uuid = UUID.randomUUID();
        System.out.println("Creating Uuid " + uuid.toString());
        payment.setId(uuid.toString());
      }
      // set parent
      if (payment.getLeaseKey() == null) {
        if (payment.getLeaseId() != null && payment.getEstateId() != null) {
          estateKey = new Key<RealEstate>(companyKey,RealEstate.class, payment.getEstateId());
          leaseKey =
              new Key<Lease>(estateKey,Lease.class, payment.getLeaseId());
          payment.setLeaseKey(leaseKey);
          
        } else {
          LOG.fatal("Lease cannot be updated, missing parent RealEstate");
          return null;
        }
      }else{
        leaseKey =payment.getLeaseKey();
      }
      T actualPayment =isAlreadyPaid(payment.getId(),leaseKey,payment.getPeriodStartDate(),payment.getPeriodEndDate());
      if (actualPayment!=null){
        LOG.info("Payment cannot be updated because there is already a payment for that period " + actualPayment.getId());
        return null;
      }
      // force all the time to be a noon to avoid CEST-GMT problem
      payment.setPaymentDate(setTime(payment.getPaymentDate()));
      payment.setPeriodStartDate(setTime(payment.getPeriodStartDate()));
      payment.setPeriodEndDate(setTime(payment.getPeriodEndDate()));
      Key<T> paymentKey = ofy().put(payment);
      LOG.info("Payment has been updated");
      Lease l = ofy().get(payment.getLeaseKey());
      RealEstate e = ofy().get(l.getRealEstateKey());
      T pt = ofy().get(paymentKey);
      pt.setEstateId(e.getId());
      pt.setLeaseId(l.getId());
      return pt;
    } catch (Exception e) {
      LOG.fatal("Payment cannot be updated: ", e);
      return null;
    }
  }
  private Date setTime(Date date){
    if (date!=null){
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      cal.set(Calendar.HOUR, 12);
      date = cal.getTime();
    }
    return date;
  }
  private Float getFee(Float rent, Float fee, FeeUnit unit){
    if (unit.equals(FeeUnit.RENT_PERCENTAGE)){
      return rent * fee/100;
    }else{
      return fee;
    }
  }
  public List<PaymentListItem> getPaymentDashboardForLease(String leaseId, String realEstateId, String companyId){
    LOG.info("Generate payments board");
    try{
      DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      Key<Company> companyKey = new Key<Company>(Company.class, companyId);
      Key<RealEstate> estateKey = new Key<RealEstate>(companyKey,RealEstate.class, realEstateId);
      Key<Lease> leaseKey = new Key<Lease>(estateKey,Lease.class, leaseId);
      
      String sDate = "";
      PaymentListItem item = null;
      PaymentListItem itemPo = null;
      
      HashMap<String,PaymentListItem> allPaymentsProprio = new HashMap<String,PaymentListItem>();
      Query<PaymentOwner> qpo = ofy().query(PaymentOwner.class);
      qpo.ancestor(leaseKey);
      for (PaymentOwner po: qpo.order("periodEndDate").list()){
        sDate = dateFormat.format(po.getPeriodEndDate());
        LOG.info("Paiement proprio: " + sDate);
        item = new PaymentListItem();
        item.setPaymentOwnerDate(po.getPaymentDate());
        item.setPaidToOwner(po.getAmount());
        item.setToBePaidToOwner(po.getRentWithoutFee() + (po.getPreviousbalance()!=null?po.getPreviousbalance():0f));
        item.setBalance(po.getBalance());
        allPaymentsProprio.put(sDate, item);
        
      }
      
      HashMap<String,PaymentListItem> allPayments = new HashMap<String,PaymentListItem>();
      Query<PaymentTenant> qpt = ofy().query(PaymentTenant.class);
      qpt.ancestor(leaseKey);
      
      Float balance = 0f;
      Float fee = 0f;
      for (PaymentTenant pt:qpt.order("periodEndDate").list()){
        sDate = dateFormat.format(pt.getPeriodEndDate());
        LOG.info("Paiement loyer: " + sDate);
        fee = getFee(pt.getAmount(),pt.getFee(),pt.getFeeUnit());
        balance +=pt.getAmount() - fee;
        item = new PaymentListItem();
        item.setFromDate(pt.getPeriodStartDate());
        item.setToDate(pt.getPeriodEndDate());
        item.setPaymentTenantDate(pt.getPaymentDate());
        item.setRent(pt.getAmount());
        item.setFee(getFee(pt.getAmount(),pt.getFee(),pt.getFeeUnit()));
        item.setToBePaidToOwner(balance);
        itemPo = allPaymentsProprio.get(sDate);
        item.setBalance(balance);
        if (itemPo!=null){
          item.setPaymentOwnerDate(itemPo.getPaymentOwnerDate());
          item.setPaidToOwner(itemPo.getPaidToOwner());
          item.setToBePaidToOwner(itemPo.getToBePaidToOwner());
          item.setBalance(itemPo.getBalance());
          balance = itemPo.getBalance();
        }
        allPayments.put(sDate, item);
      }
      
      
      List<PaymentListItem> paymentDashboard = new ArrayList<PaymentListItem>();
      for (PaymentListItem itm: allPayments.values()){
        paymentDashboard.add(itm);
      }
      LOG.info("Board is generated");
      return paymentDashboard;
    } catch (Exception e) {
      LOG.fatal("Cannot build payments board: ", e);
      return null;
    }  
  }
  public PaymentOwner getNextPaymentOwnerForLease(String leaseId, String realEstateId, String companyId){
    try{
      Key<Company> companyKey = new Key<Company>(Company.class, companyId);
      Key<RealEstate> estateKey = new Key<RealEstate>(companyKey,RealEstate.class, realEstateId);
      Key<Lease> leaseKey = new Key<Lease>(estateKey,Lease.class, leaseId);
      Lease lease = ofy().get(leaseKey);
      RealEstate estate = ofy().get(estateKey);
      Owner owner = ofy().get(estate.getOwnerKey());
      Query<PaymentOwner> q = ofy().query(PaymentOwner.class);
      q.ancestor(leaseKey);
      // pas de possibilité de calcul si pas d'honoraire ni de loyer
      if (lease.getRent()==null || owner.getFee()==null){
        return null;
      }
      // recherche du dernier versement proprio
      PaymentOwner lastpo = q.order("-periodEndDate").get();
      // recherche du premier et du dernier payment de loyer
      Query<PaymentTenant> q2 = ofy().query(PaymentTenant.class);
      q2.ancestor(leaseKey);
      PaymentTenant lastpt = q2.order("-periodEndDate").get();
      q2 = ofy().query(PaymentTenant.class);
      q2.ancestor(leaseKey);
      PaymentTenant firstpt = q2.order("periodStartDate").get();
      PaymentOwner nextPaymentOwner = new PaymentOwner();
      if (lastpo!=null){
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastpo.getPeriodEndDate());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        nextPaymentOwner.setPeriodStartDate(setTime(cal.getTime()));
      }else{
        if (firstpt!=null){
          nextPaymentOwner.setPeriodStartDate(firstpt.getPeriodStartDate());
        }else{
          // si pas de paiement du locataire, alors pas de paiement proprio
          return null;
        }
      }
      if (lastpt!=null){
        nextPaymentOwner.setPeriodEndDate(setTime(lastpt.getPeriodEndDate()));
      }else{
        // si pas de paiement du locataire, alors pas de paiement proprio. Théoriquement on ne devrait pas passer dans ce code.
        return null;
      }
      if (nextPaymentOwner.getPeriodEndDate().before(nextPaymentOwner.getPeriodStartDate())){
        return null;
      }
      // calcul du montant des loyers perçu dans la période et du nombre de périodes de loyer.
      // on prend toutes les perceptions qui sont dans la période
      Float sum = new Float("0");
      Long nbrPeriod = 0L;
      q2 = ofy().query(PaymentTenant.class);
      q2.ancestor(leaseKey);
      q2.filter("periodStartDate >=", nextPaymentOwner.getPeriodStartDate());
      for (PaymentTenant pt:q2.list()){
        sum +=pt.getAmount();
        // ajust the fee and rent to the actual values, if owner convention is changed.
        if (pt.getFee()==null || pt.getFeeUnit()==null || !pt.getFee().equals(owner.getFee().floatValue()) || !pt.getFeeUnit().equals(owner.getUnit())){
          // fee changed since last write
          // update the fee
          pt.setFee(owner.getFee().floatValue());
          pt.setFeeUnit(owner.getUnit());
          ofy().put(pt);
        }
        nbrPeriod +=1; // todo, ne pas compter les periodes identiques.
      }
      // calcul du paiement
      // si le proprio prend un %
      Float dueToOwner = new Float("0");
      nextPaymentOwner.setFeeUnit(owner.getUnit());
      nextPaymentOwner.setFee(owner.getFee().floatValue());
      if (owner.getUnit().equals(FeeUnit.RENT_PERCENTAGE)){
        dueToOwner = sum * ((100 - owner.getFee().floatValue())/100);
      }else{
        dueToOwner = sum - (nbrPeriod * owner.getFee().floatValue());
      }
      nextPaymentOwner.setRentWithoutFee(dueToOwner);
      // Calcul du solde (ajoute de l'ancien solde)
      if (lastpo!=null){
        nextPaymentOwner.setPreviousbalance(lastpo.getBalance());
        dueToOwner += lastpo.getBalance();
      }
      nextPaymentOwner.setBalance(dueToOwner);
      nextPaymentOwner.setLeaseKey(leaseKey);
      nextPaymentOwner.setEstateId(realEstateId);
      nextPaymentOwner.setLeaseId(leaseId);
      nextPaymentOwner.setRent(sum);
      nextPaymentOwner.setNbrPeriod(nbrPeriod);
      return nextPaymentOwner;
    } catch (Exception e) {
      LOG.fatal("Next payment cannot be given: ", e);
      return null;
    }
  }
  public PaymentTenant getNextPaymentTenantForLease(String leaseId, String realEstateId, String companyId){
    try{
      Key<Company> companyKey = new Key<Company>(Company.class, companyId);
      Key<RealEstate> estateKey = new Key<RealEstate>(companyKey,RealEstate.class, realEstateId);
      Key<Lease> leaseKey = new Key<Lease>(estateKey,Lease.class, leaseId);
      Lease lease = ofy().get(leaseKey);
      RealEstate estate = ofy().get(estateKey);
      Owner owner = ofy().get(estate.getOwnerKey());
      PaymentTenant nextPaymentTenant = new PaymentTenant();
      Query<PaymentTenant> q2 = ofy().query(PaymentTenant.class);
      q2.ancestor(leaseKey);
      
      if (lease.getRent()==null){
        return null;
      }
      // chercher le dernier loyer percu
      PaymentTenant lastpt = q2.order("-periodEndDate").get();
      Calendar cal = Calendar.getInstance();
      // si pas encore de perception
      if (lastpt==null){
        nextPaymentTenant.setPeriodStartDate(setTime(lease.getStartDate()));
      }else{
        cal.setTime(lastpt.getPeriodEndDate());
        cal.add(Calendar.DAY_OF_MONTH, 1);
        nextPaymentTenant.setPeriodStartDate(setTime(cal.getTime()));
      }
      // si la date de début est supérieur à la date de fin de bail, alors, plus de payement possible.
      if (nextPaymentTenant.getPeriodStartDate().after(lease.getEndDate())){
        return null;
      }
      nextPaymentTenant.setAmount(lease.getRent());
      // on ajoute 1 mois à la date de début et on enleve 1 jour
      cal.setTime(nextPaymentTenant.getPeriodStartDate());
      cal.add(Calendar.MONTH, 1);
      cal.add(Calendar.DAY_OF_MONTH, -1);
      nextPaymentTenant.setPeriodEndDate(setTime(cal.getTime())); 
      // si la date de fin de période est supérieur à celle du bail, on ramène cette date de fin à la fin du bail
      // cas des baux inférieurs à 1 mois
      if (nextPaymentTenant.getPeriodEndDate().after(lease.getEndDate())){
        nextPaymentTenant.setPeriodEndDate(setTime(lease.getEndDate()));  
      }
      nextPaymentTenant.setLeaseKey(leaseKey);
      nextPaymentTenant.setEstateId(realEstateId);
      nextPaymentTenant.setLeaseId(leaseId);
      // keep the values from lease and owner convention
      nextPaymentTenant.setRent(lease.getRent());
      nextPaymentTenant.setFee(owner.getFee().floatValue());
      nextPaymentTenant.setFeeUnit(owner.getUnit());
      return nextPaymentTenant;
    } catch (Exception e) {
      LOG.fatal("Next payment cannot be given: ", e);
      return null;
    }
  }
}
