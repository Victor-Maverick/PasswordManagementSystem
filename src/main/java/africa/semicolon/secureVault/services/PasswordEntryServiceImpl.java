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
        //validate(passwordRequest);
        PasswordEntry passwordEntry = new PasswordEntry();
        try {
            map(passwordEntry,passwordRequest);
        } catch (Exception e) {
            throw new SecureVaultAppExceptions(e.getMessage());
        }
        passwordEntries.save(passwordEntry);
        return map(passwordEntry);
    }

    public static boolean isEmailValid(String email) {
        email = email.replaceAll(" ", "");
        email = email.toLowerCase();
        String regex = "([a-z]\\.)?[a-z][A-z]+@(enum|semicolon|learnspace|native).africa";
        return email.matches(regex);
    }

    private void validate(PasswordEntryRequest passwordRequest) {
        String website = passwordRequest.getWebsite();
        website = website.replaceAll(" ", "");
        website = website.toLowerCase();
        passwordRequest.setWebsite(website);
        String regex = "(https?://)?(www\\.)?([a-zA-Z]+([0-9]*)?\\.)?[a-zA-Z0-9]+.[a-zA-Z]{2,}";
        String emailRegex = "([a-z]\\.)?[a-z][A-Z]+@(gmail|yahoo|outlook|hotmail|semicolon|enum|learnspace|native)\\.(com|africa)";
        if (!(passwordRequest.getWebsite().matches(regex))|| !(passwordRequest.getWebsite().matches(emailRegex)))throw new InvalidInputException(passwordRequest.getWebsite()+" not valid");
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
