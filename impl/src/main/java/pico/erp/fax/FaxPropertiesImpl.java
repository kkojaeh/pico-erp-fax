package pico.erp.fax;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pico.erp.shared.Public;

@Public
@Data
@Configuration
@ConfigurationProperties("fax")
public class FaxPropertiesImpl implements FaxProperties {

  int expirationSeconds;

  int retryLimit;

  int retryIntervalSeconds;

}
