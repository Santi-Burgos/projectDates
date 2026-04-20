package san.projectdates.core.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import san.projectdates.core.dtos.UserResponse;
import san.projectdates.core.entities.Role;
import san.projectdates.core.entities.User;
import san.projectdates.core.repositories.UserRepository;
import san.projectdates.infrastructure.factories.ErrorFactoryImpl;

import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

class UserServicesTest {
  private UserRepository mockRepository;
  private UserService userService;

  @BeforeEach
  public void setUp() {
    this.mockRepository = new UserRepository() {
      @Override
      public User saveUser(User newUser) {
        return newUser;
      }

      @Override
      public Boolean emailIsAlreadyUse(String email) {
        return false;
      }

      @Override public Boolean validateUserExist(UUID id) { return true; }
      @Override public int deleteUserById(UUID id) { return 1; }
      @Override public User getUserByEmail(String email) { return new User(
        "Santi", 
        email, 
        "password123", 
        "La Pampa", 
        Role.USER
      );}
      @Override public List<User> findAllUsers() { return new ArrayList<>(); }
    };

    ErrorFactoryImpl errorFactoryImpl = new ErrorFactoryImpl();

    this.userService = new UserService(mockRepository, errorFactoryImpl);
  }

  @Test
  public void shouldCreateNewUserSuccessfully() {
    UserResponse result = userService.createNewUser(
      "Santi",
      "santi@example.com",
      "password123",
      "Burgos",
      Role.USER
    );

    Assertions.assertNotNull(result, "El objeto UserResponse no debería ser nulo");
    Assertions.assertEquals("santi@example.com", result.email(), "El email devuelto debe coincidir");
    Assertions.assertEquals("Santi", result.username());
  }

  @Test
  public void shouldGetUserByEmail(){
    String emailFind = "santi@example.com";
    UserResponse result = userService.findUserByEmail(emailFind);

    Assertions.assertNotNull(result, "El objeto UserResponse no debería ser nulo");
    Assertions.assertEquals(emailFind, result.email());
  }

  @Test
  public void shoulThhrowExceptionWhenEmailDoesNotExist(){
    String emailfind = "no-existing@example.com";
    
    RuntimeException exception = Assertions.assertThrows(RuntimeException.class, 
      () -> { userService.findUserByEmail(emailfind);       
    });
    
    Assertions.assertEquals("El usuario con este email no existe", exception.getMessage());
  }
}