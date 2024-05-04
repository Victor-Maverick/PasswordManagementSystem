package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.dtos.requests.*;
import africa.semicolon.secureVault.dtos.responses.*;
import africa.semicolon.secureVault.dtos.responses.ShareDetailsResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login(LoginRequest loginRequest);

    String logout(LogoutRequest logoutRequest);

    String deleteUser(DeleteUserRequest deleteUserRequest);

    AddCardResponse addCardInformation(AddCardRequest cardRequest);

    List<CreditCardInformation> findCardInformationFor(FindDetailsRequest request);

    String deleteCardInformation(DeleteCardRequest deleteRequest);

    ViewCardResponse viewCardDetails(ViewCardRequest viewRequest);

    PasswordEntryResponse addPasswordEntry(PasswordEntryRequest passwordRequest);

    List<PasswordEntry> findPasswordEntriesFor(FindUserEntriesRequest findRequest);

    String deletePasswordEntry(DeletePasswordEntryRequest deleteRequest);
    String deleteNotification(DeleteNotificationRequest deleteNotificationRequest);

    ViewPasswordResponse viewPassword(ViewPasswordRequest viewRequest);
    ShareDetailsResponse shareCardInformation(ShareCardDetailsRequest shareDetails);
    ShareDetailsResponse sharePassword(SharePasswordRequest shareRequest);
}
