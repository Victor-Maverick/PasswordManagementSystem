package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.data.repositories.PasswordEntries;
import africa.semicolon.secureVault.dtos.requests.DeletePasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.PasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.ViewPasswordRequest;
import africa.semicolon.secureVault.dtos.responses.PasswordEntryResponse;
import africa.semicolon.secureVault.dtos.responses.ViewPasswordResponse;
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
        PasswordEntry passwordEntry = new PasswordEntry();
        try {
            map(passwordEntry,passwordRequest);
        } catch (Exception e) {
            throw new SecureVaultAppExceptions(e.getMessage());
        }
        passwordEntries.save(passwordEntry);
        return map(passwordEntry);
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
    public List<PasswordEntry> findAllPasswordsFor(String username) {
        return passwordEntries.findByUsername(username);
    }

    @Override
    public PasswordEntry findPasswordById(String id) {
        return passwordEntries.findPasswordEntryById(id);
    }
}
