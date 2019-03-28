package pico.erp.fax;

import kkojaeh.spring.boot.component.Give;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Give
@Data
@Configuration
@ConfigurationProperties("fax")
public class FaxPropertiesImpl implements FaxProperties {

  int expirationSeconds;

  int retryLimit;

  int retryIntervalSeconds;

}
