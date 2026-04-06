package san.projectdates.core.entities;

import java.time.LocalTime;

public class TimeRange {
  private String openTime;
  private String closeTime;

  public TimeRange() {
  }

  public TimeRange(String openTime, String closeTime) {
    this.openTime = openTime;
    this.closeTime = closeTime;
    validate();
  }

  public String getOpenTime() {
    return openTime;
  }

  public LocalTime openAsLocalTime() {
    return LocalTime.parse(openTime);
  }

  public String getCloseTime() {
    return closeTime;
  }

  public LocalTime closeAsLocalTime() {
    return LocalTime.parse(closeTime);
  }

  private void validate() {
    LocalTime open = openAsLocalTime();
    LocalTime close = closeAsLocalTime();
    if (open.isAfter(close)) {
      throw new RuntimeException("Apertura no puede ser después del cierre");
    }
  }

}
