package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.dtos.requests.DeletePasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.FindUserEntriesRequest;
import africa.semicolon.secureVault.dtos.requests.PasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.ViewPasswordRequest;
import africa.semicolon.secureVault.dtos.responses.PasswordEntryResponse;
import africa.semicolon.secureVault.dtos.responses.ViewPasswordResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PasswordEntryServices {

    PasswordEntryResponse addPasswordEntry(PasswordEntryRequest passwordRequest);

    String deletePassword(DeletePasswordEntryRequest deleteRequest);

    ViewPasswordResponse viewPassword(ViewPasswordRequest viewRequest);

    List<PasswordEntry> findAllPasswordsFor(FindUserEntriesRequest username);

    PasswordEntry findPasswordById(String id);
}
