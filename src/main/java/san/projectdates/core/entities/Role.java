package san.projectdates.core.entities;

public enum Role {
  USER(1),
  GERENT(2),
  ADMIN(3);

  private final int value;

  Role(int value){
    this.value = value;
  }

  public int getValue(){
    return value;
  }

  public static Role fromValue(int value){
    for(Role role : Role.values()){
      if(role.value == value){
        return role;
      }
    }
    throw new IllegalArgumentException("Rol invalido: " + value);
  
  }
}
