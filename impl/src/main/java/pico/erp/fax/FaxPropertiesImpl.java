package pico.erp.fax;

import kkojaeh.spring.boot.component.ComponentBean;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ComponentBean
@Data
@Configuration
@ConfigurationProperties("fax")
public class FaxPropertiesImpl implements FaxProperties {

  int expirationSeconds;

  int retryLimit;

  int retryIntervalSeconds;

}
