package accesscontrol;

import com.medenterprise.jsftoolkit.ejb.PersistenceResolverApi;
import com.medenterprise.jsftoolkit.util.FacesUtils;
import javax.ejb.Singleton;

@Singleton
public class PersistenceResolver implements PersistenceResolverApi {

   @Override
   public String getPersistenseUnitName(String entityName) {
      String puName = (String) FacesUtils.getSessionObject("persistenceContext");
      return puName;
   }
}
