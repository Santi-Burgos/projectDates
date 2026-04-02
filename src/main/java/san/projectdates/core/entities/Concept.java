package san.projectdates.core.entities;

import java.util.UUID;

public class Concept {
  UUID id;
  String name;
  String details; 
  int capacity;
  Boolean isActive;
  Boolean is24h;

  public Concept(){ }

  public Concept(String name, String details, int capacity, Boolean isActive, Boolean is24h){
    this.setId();
    this.setName(name);
    this.setDetails(details);
    this.setIsActive(isActive);
    this.setIsActive(isActive);
  }
  
  public Concept(UUID id, String name, String details, int capacity, Boolean isActive, Boolean is24h ){
    this.id = id;
    this.name = name;
    this.details = details;
    this.capacity = capacity;
    this.isActive = isActive;
    this.is24h = is24h; 
  }

  public UUID getId(){
    return id;
  }

  public void setId(){
    this.id = UUID.randomUUID();
  }

  public String getName(){
    return name;
  }

  public void setName(String name){
    if(name.trim().length() == 0){
      throw new RuntimeException("Debe contener un nombre");
    }

    this.name = name;
  }

  public String getDetails(){
    return getDetails();
  }

  public void setDetails(String details){
    this.details = details;
  }

  public int getCapacity(){
    return capacity;
  }

  public void setCapacity(int capacity){
    if(capacity <= 0){
      throw new RuntimeException("Debe tener una capacidad");
    }

    this.capacity = capacity;
  }

  public Boolean getIsActive(){
    return isActive;
  }

  public void setIsActive(Boolean isActive){
    this.isActive = isActive;
  }

    public Boolean getIs24h(){
    return is24h;
  }

  public void setIs24h(Boolean is24h){
    this.is24h = is24h;
  }

}
