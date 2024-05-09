package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.data.repositories.PasswordEntries;
import africa.semicolon.secureVault.dtos.requests.*;
import africa.semicolon.secureVault.dtos.responses.EditCardResponse;
import africa.semicolon.secureVault.dtos.responses.EditPasswordResponse;
import africa.semicolon.secureVault.dtos.responses.PasswordEntryResponse;
import africa.semicolon.secureVault.dtos.responses.ViewPasswordResponse;
import africa.semicolon.secureVault.exceptions.PasswordNotFoundException;
import africa.semicolon.secureVault.exceptions.SecureVaultAppExceptions;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static africa.semicolon.secureVault.utils.EncryptDecrypt.encrypt;
import static africa.semicolon.secureVault.utils.Mapper.*;

@Service
@AllArgsConstructor
public class PasswordEntryServiceImpl implements PasswordEntryServices{
    private final PasswordEntries passwordEntries;
    @Override
    public PasswordEntryResponse addPasswordEntry(PasswordEntryRequest passwordRequest) {
        validateWebsite(passwordRequest.getWebsite());
        PasswordEntry passwordEntry = new PasswordEntry();
        try {
            map(passwordEntry,passwordRequest);
        } catch (Exception e) {
            throw new SecureVaultAppExceptions(e.getMessage());
        }
        passwordEntries.save(passwordEntry);
        return map(passwordEntry);
    }


    private void validateWebsite(String website) {
        website = website.toLowerCase();
        String emailRegex =  "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        String webRegex =  "^(https?://)?(www\\.)?[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})$";
        if (!website.matches(emailRegex) && !website.matches(webRegex)) {
            throw new SecureVaultAppExceptions(website+" not valid");
        }
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
    public List<PasswordEntry> findAllPasswordsFor(FindUserPasswordsRequest findRequest) {
        List<PasswordEntry> userEntries = passwordEntries.findByUsername(findRequest.getUsername());
        if(userEntries.isEmpty()) return List.of();
        return userEntries;
    }

    @Override
    public PasswordEntry findPasswordById(String id) {
        return passwordEntries.findPasswordEntryById(id);
    }

    @Override
    public EditPasswordResponse editPassword(EditPasswordRequest request) {
        PasswordEntry passwordEntry = passwordEntries.findPasswordEntryById(request.getPasswordId());
        if(!request.getNewPassword().equals(request.getConfirmPassword()))throw new PasswordNotFoundException("Passwords do not match");
        passwordEntry.setUsername(request.getUsername());
        passwordEntry.setWebsite(request.getWebsite());
        passwordEntry.setPassword(encrypt(request.getConfirmPassword(), passwordEntry.getPasswordId()/47));
        passwordEntries.save(passwordEntry);
        return mapEditPassword(passwordEntry);
    }
}
