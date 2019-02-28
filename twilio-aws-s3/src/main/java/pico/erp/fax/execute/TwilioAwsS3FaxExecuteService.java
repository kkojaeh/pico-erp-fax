package pico.erp.fax.execute;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.twilio.Twilio;
import com.twilio.rest.fax.v1.Fax;
import com.twilio.rest.fax.v1.Fax.Status;
import com.twilio.rest.fax.v1.Fax.UpdateStatus;
import com.twilio.rest.fax.v1.FaxFetcher;
import com.twilio.rest.fax.v1.FaxUpdater;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import pico.erp.fax.execute.FaxExecuteRequests.ClearRequest;
import pico.erp.fax.execute.FaxExecuteRequests.ExecuteRequest;
import pico.erp.fax.execute.FaxExecuteRequests.RetryRequest;

public class TwilioAwsS3FaxExecuteService implements FaxExecuteService {

  private final AmazonS3 amazonS3;

  private final String amazonS3BucketName;

  private final String twilioFaxFrom;

  private List<Status> completed = Arrays.asList(
    Status.DELIVERED,
    Status.RECEIVED
  );

  private List<Status> processing = Arrays.asList(
    Status.QUEUED,
    Status.PROCESSING,
    Status.SENDING
  );

  private List<Status> failed = Arrays.asList(
    Status.NO_ANSWER,
    Status.BUSY,
    Status.FAILED,
    Status.CANCELED
  );

  public TwilioAwsS3FaxExecuteService(Config config) {
    amazonS3 = config.getAmazonS3();
    amazonS3BucketName = config.getAmazonS3BucketName();
    twilioFaxFrom = config.getTwilioFaxFrom();
    Twilio.init(config.getTwilioAccountSid(), config.getTwilioAuthToken());
  }

  @Override
  public void clear(ClearRequest request) {
    val executeId = request.getId().getValue();
    val faxId = request.getFaxId().toString();
    amazonS3.deleteObject(
      new DeleteObjectRequest(amazonS3BucketName, faxId)
    );
    FaxFetcher faxFetcher = Fax.fetcher(executeId);
    Fax fax = faxFetcher.fetch();
    if (isProcessing(fax)) {
      FaxUpdater faxUpdater = Fax.updater(executeId);
      faxUpdater.setStatus(UpdateStatus.CANCELED);
      faxUpdater.update();
    }
  }

  @SneakyThrows
  @Override
  public FaxExecuteData execute(ExecuteRequest request) {
    val faxId = request.getFaxId().toString();
    val content = request.getContent();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(content.getContentLength());
    metadata.setContentType(content.getContentType());
    amazonS3.putObject(
      new PutObjectRequest(
        amazonS3BucketName,
        faxId,
        content,
        metadata
      )
    );
    val uri = amazonS3.generatePresignedUrl(
      amazonS3BucketName,
      faxId,
      new Date(OffsetDateTime.now().plusDays(1).toInstant().toEpochMilli())
    ).toURI();
    val fax = Fax.creator(request.getFaxNumber(), uri)
      .setFrom(twilioFaxFrom)
      .create();
    return to(fax);
  }

  @Override
  public FaxExecuteData get(FaxExecuteId id) {
    FaxFetcher faxFetcher = Fax.fetcher(id.getValue());
    Fax fax = faxFetcher.fetch();
    return to(fax);
  }

  private boolean isCompleted(Fax fax) {
    return completed.contains(fax.getStatus());
  }

  private boolean isFailed(Fax fax) {
    return failed.contains(fax.getStatus());
  }

  private boolean isProcessing(Fax fax) {
    return processing.contains(fax.getStatus());
  }

  @SneakyThrows
  @Override
  public FaxExecuteData retry(RetryRequest request) {
    val executeId = request.getId().getValue();
    val faxId = request.getFaxId().toString();
    FaxFetcher faxFetcher = Fax.fetcher(executeId);
    Fax previous = faxFetcher.fetch();
    if (isProcessing(previous)) {
      FaxUpdater faxUpdater = Fax.updater(executeId);
      faxUpdater.setStatus(UpdateStatus.CANCELED);
      faxUpdater.update();
    }
    val uri = amazonS3.generatePresignedUrl(
      amazonS3BucketName,
      faxId,
      new Date(OffsetDateTime.now().plusDays(1).toInstant().toEpochMilli())
    ).toURI();

    val fax = Fax.creator(previous.getTo(), uri)
      .setFrom(twilioFaxFrom)
      .create();

    return to(fax);
  }

  private FaxExecuteData to(Fax fax) {
    return FaxExecuteData.builder()
      .id(FaxExecuteId.from(fax.getSid()))
      .completed(isCompleted(fax))
      .failed(isFailed(fax))
      .processing(isProcessing(fax))
      .build();

  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  public static class Config {

    AmazonS3 amazonS3;

    String amazonS3BucketName;

    String twilioAccountSid;

    String twilioAuthToken;

    String twilioFaxFrom;
  }
}
