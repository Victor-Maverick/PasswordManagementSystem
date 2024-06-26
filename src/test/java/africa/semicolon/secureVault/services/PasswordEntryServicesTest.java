package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.repositories.PasswordEntries;
import africa.semicolon.secureVault.dtos.requests.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordEntryServicesTest {
    @Autowired
    PasswordEntryServices passwordEntryServices;
    @Autowired
    PasswordEntries passwordEntries;
    @BeforeEach
    public void setUp() {
        passwordEntries.deleteAll();
    }

    @Test
    public void addPassword_listOfPasswordsIncreaseTest(){
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        passwordEntryServices.addPasswordEntry(passwordRequest);
        assertEquals(1, passwordEntries.count());
    }

    @Test
    public void deletePassWordEntry_listOfPasswordEntriesReducesTest(){
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        var response = passwordEntryServices.addPasswordEntry(passwordRequest);
        assertEquals(1, passwordEntries.count());
        DeletePasswordEntryRequest deleteRequest = new DeletePasswordEntryRequest();
        deleteRequest.setId(response.getId());
        deleteRequest.setUsername("username");
        passwordEntryServices.deletePassword(deleteRequest);
        assertEquals(0, passwordEntries.count());
    }

    @Test
    public void viewPasswordTest(){
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        var response = passwordEntryServices.addPasswordEntry(passwordRequest);
        assertEquals(1, passwordEntries.count());
        ViewPasswordRequest viewRequest = new ViewPasswordRequest();
        viewRequest.setId(response.getId());
        viewRequest.setAuthorName("username");
        var viewResponse = passwordEntryServices.viewPassword(viewRequest);
        assertEquals(viewResponse.getPassword(), "password");
    }

    @Test
    public void deletePassword_listOfPasswordsDecreasesTest(){
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        var response = passwordEntryServices.addPasswordEntry(passwordRequest);
        assertEquals(1, passwordEntries.count());
        DeletePasswordEntryRequest deleteRequest = new DeletePasswordEntryRequest();
        deleteRequest.setId(response.getId());
        deleteRequest.setUsername("username");
        passwordEntryServices.deletePassword(deleteRequest);
        assertEquals(0, passwordEntries.count());
    }
    @Test
    public void viewAllPasswordsTest(){
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        passwordEntryServices.addPasswordEntry(passwordRequest);
        assertEquals(1, passwordEntries.count());
        PasswordEntryRequest passwordRequest2 = new PasswordEntryRequest();
        passwordRequest2.setUsername("username");
        passwordRequest2.setPassword("password2");
        passwordRequest2.setWebsite("www.hotbox.com");
        passwordEntryServices.addPasswordEntry(passwordRequest2);
        FindUserPasswordsRequest request = new FindUserPasswordsRequest();
        request.setUsername("username");
        assertEquals(2, passwordEntries.count());
        assertEquals(2, passwordEntryServices.findAllPasswordsFor(request).size());
    }

    @Test
    public void editPasswordTest(){
        PasswordEntryRequest passwordRequest = new PasswordEntryRequest();
        passwordRequest.setUsername("username");
        passwordRequest.setPassword("password");
        passwordRequest.setWebsite("www.pling.com");
        var response = passwordEntryServices.addPasswordEntry(passwordRequest);
        assertEquals(1, passwordEntries.count());
        EditPasswordRequest request = new EditPasswordRequest();
        request.setPasswordId(response.getId());
        request.setNewPassword("new password");
        request.setWebsite("www.pling.com");
        request.setConfirmPassword("new password");
        request.setUsername("username");
        var editResponse = passwordEntryServices.editPassword(request);
        assertEquals(1, passwordEntries.count());
        assertEquals(response.getId(), editResponse.getPasswordId());

    }


}