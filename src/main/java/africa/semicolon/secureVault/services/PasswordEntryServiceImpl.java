package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.data.repositories.PasswordEntries;
import africa.semicolon.secureVault.dtos.requests.DeletePasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.FindUserEntriesRequest;
import africa.semicolon.secureVault.dtos.requests.PasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.ViewPasswordRequest;
import africa.semicolon.secureVault.dtos.responses.PasswordEntryResponse;
import africa.semicolon.secureVault.dtos.responses.ViewPasswordResponse;
import africa.semicolon.secureVault.exceptions.InvalidInputException;
import africa.semicolon.secureVault.exceptions.PasswordNotFoundException;
import africa.semicolon.secureVault.exceptions.SecureVaultAppExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static africa.semicolon.secureVault.utils.Mapper.map;
import static africa.semicolon.secureVault.utils.Mapper.passwordMap;

@Service
@AllArgsConstructor
public class PasswordEntryServiceImpl implements PasswordEntryServices{
    private final PasswordEntries passwordEntries;
    @Override
    public PasswordEntryResponse addPasswordEntry(PasswordEntryRequest passwordRequest) {
        if (!isWebsiteValid(passwordRequest.getWebsite()))throw new SecureVaultAppExceptions(passwordRequest.getWebsite()+" not valid");
        PasswordEntry passwordEntry = new PasswordEntry();
        try {
            map(passwordEntry,passwordRequest);
        } catch (Exception e) {
            throw new SecureVaultAppExceptions(e.getMessage());
        }
        passwordEntries.save(passwordEntry);
        return map(passwordEntry);
    }

    private boolean isWebsiteValid(String website) {
        website = website.toLowerCase();
        String emailRegex =  "^(https?://)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})$";
        return website.matches(emailRegex);
    }

    public static boolean isEmailValid(String email) {
        email = email.replaceAll(" ", "");
        email = email.toLowerCase();
        String emailRegex =  "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }


    @Override
    public String deletePassword(DeletePasswordEntryRequest deleteRequest) {
        PasswordEntry passwordEntry = passwordEntries.findPasswordEntryById(deleteRequest.getId());
        if(passwordEntry == null)throw new PasswordNotFoundException("password not found");
        passwordEntries.delete(passwordEntry);
        return "delete successful";
    }

    @Override
    public ViewPasswordResponse viewPassword(ViewPasswordRequest viewRequest) {
        PasswordEntry passwordEntry = findPasswordById(viewRequest.getId());
        if(passwordEntry == null) throw new PasswordNotFoundException("password not found");
        try {
            return passwordMap(passwordEntry);
        } catch (Exception e) {
            throw new SecureVaultAppExceptions(e.getMessage());
        }
    }

    @Override
    public List<PasswordEntry> findAllPasswordsFor(FindUserEntriesRequest findRequest) {
        List<PasswordEntry> userEntries = passwordEntries.findByUsername(findRequest.getUsername());
        if(userEntries.isEmpty()) return List.of();
        return userEntries;
    }

    @Override
    public PasswordEntry findPasswordById(String id) {
        return passwordEntries.findPasswordEntryById(id);
    }
}
