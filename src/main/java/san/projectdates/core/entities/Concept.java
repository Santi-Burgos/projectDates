package san.projectdates.core.entities;

import java.util.List;
import java.util.UUID;

public class Concept {
  UUID id;
  String name;
  String details;
  int capacity;
  Boolean isActive;
  Boolean is24h;
  List<TimeRange> schedule;

  public Concept() {
  }

  public Concept(String name, String details, int capacity, Boolean isActive, Boolean is24h, List<TimeRange> schedule) {
    this.setId();
    this.setName(name);
    this.setDetails(details);
    this.setIsActive(isActive);
    this.setIs24h(is24h);
    this.setSchedule(is24h, schedule);
  }

  public Concept(UUID id, String name, String details, int capacity, Boolean isActive, Boolean is24h) {
    this.id = id;
    this.name = name;
    this.details = details;
    this.capacity = capacity;
    this.isActive = isActive;
    this.is24h = is24h;
  }

  public UUID getId() {
    return id;
  }

  public void setId(){
    this.id = UUID.randomUUID();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (name.trim().length() == 0) {
      throw new RuntimeException("Debe contener un nombre");
    }

    this.name = name;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    if (capacity <= 0) {
      throw new RuntimeException("Debe tener una capacidad");
    }

    this.capacity = capacity;
  }

  public Boolean getIsActive() {
    return isActive;
  }

  public void setIsActive(Boolean isActive) {
    this.isActive = isActive;
  }

  public Boolean getIs24h() {
    return is24h;
  }

  public void setIs24h(Boolean is24h) {
    this.is24h = is24h;
  }

  public List<TimeRange> getSchedule() {
    return schedule;
  }

  public void setToggleStatus(){
    this.isActive = !this.isActive;
  }

  public void setSchedule(Boolean is24h, List<TimeRange> schedule) {
    if (!is24h) {
      if (schedule == null || schedule.isEmpty()) {
        throw new RuntimeException("Si no es 24h, debes aclarar en que horarios se encuentra disponible");
      }
      validateNoOverlap(schedule);
    }
    this.schedule = schedule;
  }

  private void validateNoOverlap(List<TimeRange> schedule) {
    for (int i = 0; i < schedule.size(); i++) {
      for (int j = i + 1; j < schedule.size(); j++) {
        if (rangesOverlap(schedule.get(i), schedule.get(j))) {
          throw new RuntimeException("Los rangos horarios se superponen");
        }
      }
    }
  }

  private boolean rangesOverlap(TimeRange a, TimeRange b) {
    return a.openAsLocalTime().isBefore(b.closeAsLocalTime()) &&
        b.openAsLocalTime().isBefore(a.closeAsLocalTime());
  }
}
