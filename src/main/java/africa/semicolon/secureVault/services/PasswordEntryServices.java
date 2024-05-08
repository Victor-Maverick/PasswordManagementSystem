package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.dtos.requests.*;
import africa.semicolon.secureVault.dtos.responses.EditPasswordResponse;
import africa.semicolon.secureVault.dtos.responses.PasswordEntryResponse;
import africa.semicolon.secureVault.dtos.responses.ViewPasswordResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PasswordEntryServices {

    PasswordEntryResponse addPasswordEntry(PasswordEntryRequest passwordRequest);

    String deletePassword(DeletePasswordEntryRequest deleteRequest);

    ViewPasswordResponse viewPassword(ViewPasswordRequest viewRequest);

    List<PasswordEntry> findAllPasswordsFor(FindUserPasswordsRequest username);

    PasswordEntry findPasswordById(String id);

    EditPasswordResponse editPassword(EditPasswordRequest request);
}
