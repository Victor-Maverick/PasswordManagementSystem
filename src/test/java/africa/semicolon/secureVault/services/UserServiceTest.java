package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.repositories.CardRepository;
import africa.semicolon.secureVault.data.repositories.PasswordEntries;
import africa.semicolon.secureVault.data.repositories.Users;
import africa.semicolon.secureVault.dtos.requests.*;
import africa.semicolon.secureVault.exceptions.SecureVaultAppExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    Users users;
    @Autowired
    CardRepository cards;
    @Autowired
    PasswordEntries passwordEntries;
    @BeforeEach
    public void setUp() {
        passwordEntries.deleteAll();
        users.deleteAll();
        cards.deleteAll();
    }

    @Test
    public void registerUser_userCountIncreasesTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        var user = userService.register(registerRequest);
        assertNotNull(user.getId());
        assertEquals(1, users.count());
    }



    @Test
    public void userLoginTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        var user = userService.register(registerRequest);
        assertNotNull(user.getId());
        assertEquals(1, users.count());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        assertTrue(users.findByUsername("username").isLoggedIn());
    }
    @Test
    public void loginWithWrongUsernameTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        var user = userService.register(registerRequest);
        assertNotNull(user.getId());
        assertEquals(1, users.count());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("wrong username");
        loginRequest.setPassword("password");
        try {
            userService.login(loginRequest);
        }
        catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), loginRequest.getUsername()+ " not found");
        }
        assertFalse(users.findByUsername("username").isLoggedIn());
    }

    @Test
    public void loginWithWrongPassword_throwsExceptionTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username");
        registerRequest.setPassword("password");
        var user = userService.register(registerRequest);
        assertNotNull(user.getId());
        assertEquals(1, users.count());
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username");
        loginRequest.setPassword("wrong password");
        try {
            userService.login(loginRequest);
        }
        catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), "wrong password");
        }
        assertFalse(users.findByUsername("username").isLoggedIn());
    }

    @Test
    public void LogoutTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("username4");
        userService.logout(logoutRequest);
        assertFalse(users.findByUsername("username4").isLoggedIn());
    }

    @Test
    public void deleteUserWhileLoggedInTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        DeleteUserRequest deleteRequest = new DeleteUserRequest();
        deleteRequest.setUsername("username4");
        userService.deleteUser(deleteRequest);
        assertEquals(0, users.count());
    }

    @Test
    public void deleteUserWithoutLogInTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");

        userService.register(registerRequest);
        DeleteUserRequest deleteRequest = new DeleteUserRequest();
        deleteRequest.setUsername("username4");
        try {
            userService.deleteUser(deleteRequest);
        }
        catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), "log in first");
        }
        assertEquals(1, users.count());
    }

    @Test
    public void addCardInformationWhileLoggedInTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username4");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        userService.addCardInformation(cardRequest);
        assertEquals(1, userService.findCardInformationFor("username4").size());
    }

    @Test
    public void addCardInformationWithoutLogIn_throwsExceptionTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);

        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username4");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        try {
            userService.addCardInformation(cardRequest);
        }catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), "log in first");
        }
    }

    @Test
    public void deleteCardInformation_cardListDecreasesTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username4");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        var card = userService.addCardInformation(cardRequest);
        assertEquals(1, userService.findCardInformationFor("username4").size());
        DeleteCardRequest deleteRequest = new DeleteCardRequest();
        deleteRequest.setUsername("username4");
        deleteRequest.setCardId(card.getId());
        userService.deleteCardInformation(deleteRequest);
        assertEquals(0, userService.findCardInformationFor("username4").size());
    }

    @Test
    public void deleteCardInformationWithoutLogin_throwsExceptionTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username4");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        var card = userService.addCardInformation(cardRequest);
        assertEquals(1, userService.findCardInformationFor("username4").size());
        DeleteCardRequest deleteRequest = new DeleteCardRequest();
        deleteRequest.setUsername("username4");
        deleteRequest.setCardId(card.getId());
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("username4");
        userService.logout(logoutRequest);
        try {
            userService.deleteCardInformation(deleteRequest);
        }catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), "log in first");
        }
    }
    @Test
    public void viewCardInformationWhileLoggedInTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username4");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        var card = userService.addCardInformation(cardRequest);
        assertEquals(1, userService.findCardInformationFor("username4").size());
        ViewCardRequest viewRequest = new ViewCardRequest();
        viewRequest.setId(card.getId());
        viewRequest.setViewerName("username4");
        var response = userService.viewCardDetails(viewRequest);
        assertEquals(response.getPin(), cardRequest.getPin());
    }

    @Test
    public void viewCardInformationWithoutLogInTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        AddCardRequest cardRequest = new AddCardRequest();
        cardRequest.setUsername("username4");
        cardRequest.setCardNumber("5199110726076091");
        cardRequest.setBankName("Wells Fargo");
        cardRequest.setPin("12345");
        var card = userService.addCardInformation(cardRequest);
        assertEquals(1, userService.findCardInformationFor("username4").size());
        ViewCardRequest viewRequest = new ViewCardRequest();
        viewRequest.setId(card.getId());
        viewRequest.setViewerName("username4");
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setUsername("username4");
        userService.logout(logoutRequest);
        try {
            userService.viewCardDetails(viewRequest);
        }catch (SecureVaultAppExceptions e){
            assertEquals(e.getMessage(), "log in first");
        }
    }

    @Test
    public void addPasswordEntryTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username4");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        userService.addPasswordEntry(passwordRequest);
        FindUserEntriesRequest request = new FindUserEntriesRequest();
        request.setUsername("username4");
        assertEquals(1, userService.findPasswordEntriesFor(request).size());
    }

    @Test
    public void deletePasswordEntryTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username4");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        var response = userService.addPasswordEntry(passwordRequest);
        FindUserEntriesRequest request = new FindUserEntriesRequest();
        request.setUsername("username4");
        assertEquals(1, userService.findPasswordEntriesFor(request).size());
        DeletePasswordEntryRequest deleteRequest = new DeletePasswordEntryRequest();
        deleteRequest.setId(response.getId());
        deleteRequest.setUsername("username4");
        userService.deletePasswordEntry(deleteRequest);
        assertEquals(0, userService.findPasswordEntriesFor(request).size());

    }

    @Test
    public void viewPasswordEntryTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("username4");
        registerRequest.setPassword("password");
        userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("username4");
        loginRequest.setPassword("password");
        userService.login(loginRequest);
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username4");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        var response = userService.addPasswordEntry(passwordRequest);
        ViewPasswordRequest viewRequest = new ViewPasswordRequest();
        viewRequest.setId(response.getId());
        viewRequest.setAuthorName("username4");
        var viewResponse = userService.viewPassword(viewRequest);
        assertEquals(viewResponse.getPassword(), "password");
    }



}