package eu.comexis.napoleon.client.utils;

import static com.google.gwt.query.client.GQuery.$;

import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import eu.comexis.napoleon.client.resources.Literals;
import eu.comexis.napoleon.client.resources.Resources;
import eu.comexis.napoleon.client.widget.InformationDialog;
import eu.comexis.napoleon.shared.validation.ValidationMessage;

public class UiHelper {
  
  private static DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("dd/MM/yyyy");

  // static class
  private UiHelper() {
  }

  public static ListBox createListBoxForEnum(Class<? extends Enum<?>> enumClass, String prefix,
      boolean mutiple) {
    ListBox box = new ListBox(mutiple);
    for (Enum<?> e : enumClass.getEnumConstants()) {
      box.addItem(translateEnum(prefix, e), e.name());
    }

    return box;
  }

  public static String translateEnum(String prefix, Enum<?> e) {
    return translateEnum(prefix, e, "");
  }

  public static String translateEnum(String prefix, Enum<?> e, String suffix) {
    if (e == null) {
      return "";
    }
    String literalKey = new StringBuilder(prefix).append(e.name()).append(suffix).toString();
    return Literals.INSTANCE.getString(literalKey);
  }

  public static void selectTextItemBoxByValue(ListBox tb, String value) {

    for (int i = 0; i < tb.getItemCount(); i++) {
      if (tb.getValue(i).equals(value)) {
        tb.setSelectedIndex(i);
        break;
      }
    }
  }
  
  public static void selectTextItemBoxByValue(ListBox tb, Enum<?> value) {
   selectTextItemBoxByValue(tb, value, null);
  }
  
  public static void selectTextItemBoxByValue(ListBox tb, Enum<?> value, Enum<?> defaultValue) {
    if (value != null){
      selectTextItemBoxByValue(tb, value.name());
    }else if (defaultValue != null){
      selectTextItemBoxByValue(tb, defaultValue.name());
    }
  }
  
  public static void displayValidationMessage(List<ValidationMessage> validationMessages, Widget w){
    resetForm(w);
    
    InformationDialog.get().showValidationMessages(validationMessages);
    
    for (ValidationMessage m : validationMessages){
      String selector = "[name='"+m.getComponentId()+"' ]";
      $(selector, w).addClass(Resources.INSTANCE.css().fieldInError());
    }
  }
  
  
  public static void resetForm(Widget w){
    $("input, select", w).removeClass(Resources.INSTANCE.css().fieldInError());
  }
  
  /**
   * Give a string representation of a date for display purpose 
   * @param d
   * @return
   */
  public static String displayDate(Date d){
    if (d == null){
      return "";
    }
    
    return DATE_FORMAT.format(d);
  }
  public static String formatLastName(String name){
    if (name!=null){
      return name.toUpperCase();
    }else{
      return "";
    }
  }
  public static String formatFirstName(String name){
    return formatWordsFirstUpper(name);
  }
  public static String formatSuggest(String name){
    return formatWordsFirstUpper(name);
  }
  public static String formatWordsFirstUpper(String name){
    if (name!=null){
      String formatedName = "";
      String [] parts=name.toLowerCase().split(" ");
      for(int i=0; i < parts.length; i++){
        parts[i] = parts[i].substring(0,1).toUpperCase() + parts[i].substring(1);
        formatedName += parts[i] + " ";
      }
      parts=formatedName.split("-");
      formatedName = "";
      for(int i=0; i < parts.length; i++){
        parts[i] = parts[i].substring(0,1).toUpperCase() + parts[i].substring(1);
        formatedName += parts[i] + "-";
      }
      return formatedName.substring(0,formatedName.length()-1);
    }else{
      return "";
    }
  }
}
