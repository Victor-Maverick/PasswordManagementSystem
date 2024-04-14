package africa.semicolon.secureVault.data.repositories;

import africa.semicolon.secureVault.data.models.PasswordEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PasswordEntriesTest {
    @Autowired
    PasswordEntries passwordEntries;
    @BeforeEach
    public void setUp() {
        passwordEntries.deleteAll();
    }

    @Test
    public void addPassWordTest(){
        PasswordEntry passwordEntry = new PasswordEntry();
        passwordEntries.save(passwordEntry);
        assertEquals(1, passwordEntries.count());
    }

    @Test
    public void deletePasswordTest(){
        PasswordEntry passwordEntry = new PasswordEntry();
        passwordEntries.save(passwordEntry);
        assertEquals(1, passwordEntries.count());
        passwordEntries.delete(passwordEntry);
        assertEquals(0, passwordEntries.count());
    }

}