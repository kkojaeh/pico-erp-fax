package pico.erp.fax;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pico.erp.fax.FaxRequests.RevalidateRequest;
import pico.erp.fax.FaxRequests.SendRequest;
import pico.erp.fax.execute.FaxExecuteRequests;
import pico.erp.fax.execute.FaxExecuteService;
import pico.erp.shared.Public;
import pico.erp.shared.event.EventPublisher;

@SuppressWarnings("Duplicates")
@Service
@Public
@Transactional
@Validated
public class FaxServiceLogic implements FaxService {

  @Autowired
  private FaxRepository faxRepository;

  @Autowired
  private EventPublisher eventPublisher;

  @Autowired
  private FaxMapper mapper;

  @Lazy
  @Autowired
  private FaxExecuteService faxExecuteService;

  @Autowired
  private FaxProperties properties;


  private Fax create(SendRequest request) {
    val fax = new Fax();
    val response = fax.apply(mapper.map(request));
    if (faxRepository.exists(fax.getId())) {
      throw new FaxExceptions.AlreadyExistsException();
    }
    val created = faxRepository.create(fax);
    eventPublisher.publishEvents(response.getEvents());
    return created;
  }

  @Override
  public FaxData get(FaxId id) {
    return faxRepository.findBy(id)
      .map(mapper::map)
      .orElseThrow(FaxExceptions.NotFoundException::new);
  }

  @Override
  public void revalidate(RevalidateRequest request) {
    faxRepository.findAllProcessing().forEach(fax -> {
      val response = fax.apply(
        FaxMessages.Execute.Request.builder()
          .previous(faxExecuteService.get(fax.getExecuteId()))
          .retry(
            () -> faxExecuteService.retry(
              FaxExecuteRequests.RetryRequest.builder()
                .faxId(fax.getId())
                .id(fax.getExecuteId())
                .build())
          )
          .retryLimit(properties.getRetryLimit())
          .expirationSeconds(properties.getExpirationSeconds())
          .retryIntervalSeconds(properties.getRetryIntervalSeconds())
          .build()
      );
      faxRepository.update(fax);
      if (fax.isTerminated()) {
        faxExecuteService.clear(
          FaxExecuteRequests.ClearRequest.builder()
            .id(fax.getExecuteId())
            .faxId(fax.getId())
            .build()
        );
      }
      eventPublisher.publishEvents(response.getEvents());
    });
  }

  @Override
  public FaxData send(SendRequest request) {
    val fax = create(request);
    val response = fax.apply(
      FaxMessages.Execute.Request.builder()
        .execute(
          () -> faxExecuteService.execute(
            FaxExecuteRequests.ExecuteRequest.builder()
              .faxId(fax.getId())
              .content(request.getContent())
              .faxNumber(request.getFaxNumber())
              .build())
        )
        .retryLimit(properties.getRetryLimit())
        .expirationSeconds(properties.getExpirationSeconds())
        .retryIntervalSeconds(properties.getRetryIntervalSeconds())
        .build()
    );
    faxRepository.update(fax);
    eventPublisher.publishEvents(response.getEvents());
    return mapper.map(fax);
  }

}
