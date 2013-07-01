package accesscontrol;

import accesscontrol.entity.Policy;
import accesscontrol.entity.Role;
import accesscontrol.entity.User;
import com.medenterprise.jsftoolkit.ejb.CrudService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "biz")
@SessionScoped
public class BizController {

   @EJB
   private CrudService crudService;

   private Long generatedId = 0L;

   public BizController() {
   }

   public Map roleValidationMap(Role role) {
      Map validationMap = new HashMap();
      if (role.getName() == null || role.getName().trim().isEmpty()) {
         validationMap.put("name", true);
      }
      return validationMap;
   }
   
   private List<User> duplicateUserList(String name) {
      if (name == null) {
         return null;
      }
      Map params = new HashMap();
      params.put("name", name);
      return castList(crudService.findByNamedQuery("User.findByName", params), User.class);
   }
   
   private <T> List<T> castList(List list, Class<T> type) {
      List<T> resultList = new ArrayList();
      for (Object obj : list) {
         resultList.add((T) obj);
      }
      return resultList;
   }

   public Map userValidationMap(User user) {
      Map validationMap = new HashMap();
      if (user.getName() == null || user.getName().trim().isEmpty()) {
         validationMap.put("name", true);
      } else {
         List<User> duplicateUserList = duplicateUserList(user.getName());
         if (duplicateUserList != null && ! duplicateUserList.isEmpty()) {
            for (User u : duplicateUserList) {
               if (user.getId() == null || ! u.getId().equals(user.getId())) {
                  validationMap.put("duplicate", true);
               }
            }
         } 
      }
      if (user.getLogin() != null && user.getLogin().getName().length() < 2) {
         validationMap.put("minLogin", true);
      }
      if (user.getLogin() != null && user.getLogin().getPassword().length() < 3) {
         validationMap.put("minPassword", true);
      }
      return validationMap;
   }
   
   public Map policyValidationMap(Policy policy) {
      Map validationMap = new HashMap();
      if (policy.getApplication() == null || policy.getApplication().trim().isEmpty()) {
         validationMap.put("application", true);
      }
      if (policy.getUriExpression() == null || policy.getUriExpression().trim().isEmpty()) {
         validationMap.put("expression", true);
      }
      if (policy.getEffect() == null || policy.getEffect().trim().isEmpty()) {
         validationMap.put("effect", true);
      }
      if (policy.getRole() == null) {
         validationMap.put("role", true);
      }
      return validationMap;
   }

   public List applicationList() {
      List applicationList = crudService.findByNamedQuery("Policy.applicationList");
      return applicationList;
   }
   
   public List policyList(User user, String application, Role role) {
      Map params = new HashMap();
      params.put("userId", (user == null) ? null : user.getId());
      params.put("application", (application == null || application.isEmpty()) ? null : application);
      params.put("roleId", (role == null) ? null : role.getId());
      List policyList = crudService.findByNamedQuery("Policy.findByUserApplicationRole", params);
      return policyList;
   }
}