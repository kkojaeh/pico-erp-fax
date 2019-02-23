package pico.erp.fax;

public interface FaxProperties {

  int getExpirationSeconds();

  int getRetryIntervalSeconds();

  int getRetryLimit();

}
