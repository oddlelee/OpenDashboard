/**
 * 
 */
package od.providers.enrollment;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import od.providers.ProviderData;
import od.providers.ProviderException;
import od.providers.config.ProviderConfiguration;
import od.providers.config.ProviderConfigurationOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import unicon.oneroster.Enrollment;
import unicon.oneroster.Role;
import unicon.oneroster.Status;
import unicon.oneroster.User;
import unicon.oneroster.Vocabulary;

/**
 * @author ggilbert
 *
 */
@Component("roster_demo")
public class DemoEnrollmentProvider implements EnrollmentProvider {

  private static final Logger log = LoggerFactory.getLogger(DemoEnrollmentProvider.class);
  
  private static final String KEY = "roster_demo";
  private static final String BASE = "OD_DEMO_ENROLLMENTS";
  private static final String NAME = String.format("%s_NAME", BASE);
  private static final String DESC = String.format("%s_DESC", BASE);
  
  private Map<String, Set<Enrollment>> studentEnrollments;
  private Set<Enrollment> staffEnrollments;
  
  @Override
  public String getKey() {
    return KEY;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getDesc() {
    return DESC;
  }

  @Override
  public ProviderConfiguration getProviderConfiguration() {
    // Not needed for demo provider
    return new ProviderConfiguration() {
      
      @Override
      public LinkedList<ProviderConfigurationOption> getOptions() {
        return new LinkedList<>();
      }
      
      @Override
      public ProviderConfigurationOption getByKey(String key) {
        return null;
      }
    };
  }
  
  @PostConstruct
  public void init() {
    staffEnrollments = new HashSet<>();
    Map<String, unicon.oneroster.Class> classes = new HashMap<>();
    
    Map<String, String> metadata1 = new HashMap<>();   
    metadata1.put(Vocabulary.CLASS_START_DATE, LocalDate.of(2016, 8, 30).toString());
    metadata1.put(Vocabulary.CLASS_END_DATE, LocalDate.of(2016, 12, 11).toString());
    metadata1.put(Vocabulary.SOURCE_SYSTEM, "DEMO");
    
    Map<String, String> metadata2 = new HashMap<>();
    metadata2.put(Vocabulary.CLASS_START_DATE, LocalDate.of(2016, 9, 1).toString());
    metadata2.put(Vocabulary.CLASS_END_DATE, LocalDate.of(2016, 12, 10).toString());
    metadata2.put(Vocabulary.SOURCE_SYSTEM, "DEMO");

    Map<String, String> metadata3 = new HashMap<>();
    metadata3.put(Vocabulary.CLASS_START_DATE, LocalDate.of(2016, 9, 8).toString());
    metadata3.put(Vocabulary.CLASS_END_DATE, LocalDate.of(2016, 12, 13).toString());
    metadata3.put(Vocabulary.SOURCE_SYSTEM, "DEMO");
    
    unicon.oneroster.Class class1
      = new unicon.oneroster.Class.Builder()
          .withSourcedId("demo-class-1")
          .withTitle("Introduction to Organic Chemistry")
          .withMetadata(metadata1)
          .withStatus(Status.active)
          .build();
    
    unicon.oneroster.Class class2
      = new unicon.oneroster.Class.Builder()
        .withSourcedId("demo-class-2")
        .withTitle("Advanced Chemistry 303")
        .withMetadata(metadata2)
        .withStatus(Status.active)
        .build();

    unicon.oneroster.Class class3
      = new unicon.oneroster.Class.Builder()
        .withSourcedId("demo-class-3")
        .withTitle("MicroBiology 201")
        .withMetadata(metadata3)
        .withStatus(Status.active)
        .build();
    
    classes.put("demo-class-1", class1);
    classes.put("demo-class-2", class2);
    classes.put("demo-class-3", class3);

    User teacher 
      = new User.Builder()
        .withSourcedId("teacher-sourcedId-1")
        .withRole(Role.teacher)
        .withFamilyName("Wooden")
        .withGivenName("John")
        .withUserId("teacher-userid-1")
        .withStatus(Status.active)
        .build();
    
    Enrollment teacherEnrollment1
      = new Enrollment.Builder()
          .withKlass(class1)
          .withUser(teacher)
          .withPrimary(true)
          .withSourcedId("teacher-enrollment-1")
          .withRole(Role.teacher)
          .withStatus(Status.active)
          .build();
    
    staffEnrollments.add(teacherEnrollment1);
    
    Enrollment teacherEnrollment2
    = new Enrollment.Builder()
        .withKlass(class2)
        .withUser(teacher)
        .withPrimary(true)
        .withSourcedId("teacher-enrollment-2")
        .withRole(Role.teacher)
        .withStatus(Status.active)
        .build();
  
    staffEnrollments.add(teacherEnrollment2);

    Enrollment teacherEnrollment3
    = new Enrollment.Builder()
        .withKlass(class3)
        .withUser(teacher)
        .withPrimary(true)
        .withSourcedId("teacher-enrollment-3")
        .withRole(Role.teacher)
        .withStatus(Status.active)
        .build();
  
    staffEnrollments.add(teacherEnrollment3);

  }

  @Override
  public Set<Enrollment> getEnrollmentsForClass(ProviderData providerData, String classSourcedId, boolean activeOnly) throws ProviderException {
     return staffEnrollments;
  }

  @Override
  public Set<Enrollment> getEnrollmentsForUser(ProviderData providerData, String userSourcedId, boolean activeOnly) throws ProviderException {
    return staffEnrollments;  
  }

}
