package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.data.models.User;
import africa.semicolon.secureVault.data.repositories.Users;
import africa.semicolon.secureVault.dtos.requests.*;
import africa.semicolon.secureVault.dtos.responses.*;
import africa.semicolon.secureVault.exceptions.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static africa.semicolon.secureVault.utils.EncryptDecrypt.decrypt;
import static africa.semicolon.secureVault.utils.Mapper.*;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
        private final Users users;
        private final CreditCardServiceImpl cardServices;
        private final PasswordEntryServices passwordEntryServices;
        @Override
        public RegisterResponse register(RegisterRequest registerRequest) {
            validateRegistration(registerRequest);
            User user = new User();
            map(user, registerRequest);
            users.save(user);
            return map(user);
        }

        private static void validateUserLogin(User user) {
            if (!user.isLoggedIn())throw new LoginException("log in first");
        }


        @Override
        public LoginResponse login(LoginRequest loginRequest) {
            User user = users.findByUsername(loginRequest.getUsername().toLowerCase());
            if (user == null)throw new UserNotFoundException(loginRequest.getUsername()+" not found");
            if (!decrypt(user.getPassword(), user.getIdNumber()/456).equals(loginRequest.getPassword()))throw new IncorrectPasswordException("wrong password");
            user.setLoggedIn(true);
            users.save(user);
            return mapLogin(user);
        }

        @Override
        public String logout(LogoutRequest logoutRequest) {
            User user = users.findByUsername(logoutRequest.getUsername().toLowerCase());
            if(user == null)throw new UserNotFoundException(logoutRequest.getUsername()+ " not found");
            user.setLoggedIn(false);
            users.save(user);
            return "logout success";
        }


    @Override
        public String deleteUser(DeleteUserRequest deleteUserRequest) {
            User user = users.findByUsername(deleteUserRequest.getUsername().toLowerCase());
            if(user == null)throw new UserNotFoundException(deleteUserRequest.getUsername()+ " not found");
            validateUserLogin(user);
            users.delete(user);
            return "delete success";
        }



    @Override
    public AddCardResponse addCardInformation(AddCardRequest cardRequest) {
         User user = users.findByUsername(cardRequest.getUsername().toLowerCase());
         if (user == null)throw new UserNotFoundException(cardRequest.getUsername()+" not found");
         validateUserLogin(user);
         AddCardResponse response = cardServices.addCardInformation(cardRequest);
        CreditCardInformation cardInformation = cardServices.findById(response.getId());
        List<CreditCardInformation> cardList = user.getCardInformationList();
        cardList.add(cardInformation);
        user.setCardInformationList(cardList);
        users.save(user);
        return response;
    }

    @Override
    public List<CreditCardInformation> findCardInformationFor(FindDetailsRequest request) {
            User user = users.findByUsername(request.getUsername().toLowerCase());
            if(user == null)throw new UserNotFoundException(request.getUsername()+" not found");
            validateUserLogin(user);
            return user.getCardInformationList();
    }

    @Override
    public String deleteCardInformation(DeleteCardRequest deleteRequest) {
            User user = users.findByUsername(deleteRequest.getUsername().toLowerCase());
            if(user == null)throw new UserNotFoundException(deleteRequest.getUsername()+ " not found");
            validateUserLogin(user);
        CreditCardInformation cardInformation = cardServices.findById(deleteRequest.getCardId());
        List<CreditCardInformation> cardList = user.getCardInformationList();
        cardList.remove(cardInformation);
        user.setCardInformationList(cardList);
        users.save(user);
        return cardServices.deleteCardInformation(deleteRequest);
    }

    @Override
    public ViewCardResponse viewCardDetails(ViewCardRequest viewRequest) {
            User user = users.findByUsername(viewRequest.getViewerName().toLowerCase());
            if(user == null)throw new UserNotFoundException(viewRequest.getViewerName()+" not found");
            validateUserLogin(user);
        return cardServices.viewCardInformation(viewRequest);
    }

    @Override
    public PasswordEntryResponse addPasswordEntry(PasswordEntryRequest passwordRequest) {
            User user = users.findByUsername(passwordRequest.getUsername().toLowerCase());
            if (user == null) throw new UserNotFoundException(passwordRequest.getUsername()+" not found");
            validateUserLogin(user);
            var response = passwordEntryServices.addPasswordEntry(passwordRequest);
            List<PasswordEntry> passwordEntryList = user.getPasswordEntryList();
            PasswordEntry passwordEntry = passwordEntryServices.findPasswordById(response.getId());
            passwordEntryList.add(passwordEntry);
            user.setPasswordEntryList(passwordEntryList);
            users.save(user);
        return response;
    }

    @Override
    public List<PasswordEntry> findPasswordEntriesFor(FindUserEntriesRequest findRequest) {
            User user = users.findByUsername(findRequest.getUsername().toLowerCase());
            if(user == null)throw new UserNotFoundException(findRequest.getUsername()+" not found");
            validateUserLogin(user);
         return user.getPasswordEntryList();
    }


    @Override
    public String deletePasswordEntry(DeletePasswordEntryRequest deleteRequest) {
        User user = users.findByUsername(deleteRequest.getUsername().toLowerCase());
        if (user == null) throw new UserNotFoundException(deleteRequest.getUsername()+" not found");
        validateUserLogin(user);
        var response = passwordEntryServices.deletePassword(deleteRequest);
        List<PasswordEntry> passwordEntryList = user.getPasswordEntryList();
        passwordEntryList.removeIf(passwordEntry1 -> passwordEntry1.getId().equals(deleteRequest.getId()));
        user.setPasswordEntryList(passwordEntryList);
        users.save(user);
        return response;
    }

    @Override
    public ViewPasswordResponse viewPassword(ViewPasswordRequest viewRequest) {
        User owner = users.findByUsername(viewRequest.getAuthorName().toLowerCase());
        if (owner == null) throw new UserNotFoundException(viewRequest.getAuthorName()+" not found");
        User viewer = users.findByUsername(viewRequest.getViewerName().toLowerCase());
        if (viewer == null) throw new UserNotFoundException(viewRequest.getViewerName()+" not found");
        validateUserLogin(viewer);
        List<PasswordEntry> passwordEntries = viewer.getPasswordEntryList();
        passwordEntries.forEach(passwordEntry -> {if (!passwordEntry.getId().equals(viewRequest.getId()))throw new UserNotFoundException("Not allowed to view this password");});
        return passwordEntryServices.viewPassword(viewRequest);
    }

    @Override
    public ShareDetailsResponse shareCardInformation(ShareCardDetailsRequest shareRequest) {
        User sender = users.findByUsername(shareRequest.getSenderUsername().toLowerCase());
        if(sender == null) throw new UserNotFoundException(shareRequest.getSenderUsername()+" not found");
        validateUserLogin(sender);
        User receiver = users.findByUsername(shareRequest.getReceiverUsername().toLowerCase());
        if(receiver == null) throw new UserNotFoundException(shareRequest.getReceiverUsername()+" not found");
        CreditCardInformation cardInformation = cardServices.findById(shareRequest.getCardId());
        List<CreditCardInformation> cardList = receiver.getCardInformationList();
        cardList.add(cardInformation);
        receiver.setCardInformationList(cardList);
        users.save(receiver);
        return shareCardMap(sender, receiver, cardInformation);
    }

    @Override
    public ShareDetailsResponse sharePassword(SharePasswordRequest shareRequest) {
        User sender = users.findByUsername(shareRequest.getSenderName().toLowerCase());
        if(sender == null) throw new UserNotFoundException(shareRequest.getSenderName()+" not found");
        validateUserLogin(sender);
        sender.getPasswordEntryList().forEach(passwordEntry -> {if (!passwordEntry.getId().equals(shareRequest.getPasswordId()))throw new PasswordNotFoundException("not found");});
        User receiver = users.findByUsername(shareRequest.getReceiverName().toLowerCase());
        if(receiver == null) throw new UserNotFoundException(shareRequest.getReceiverName()+" not found");
        PasswordEntry passwordEntry = passwordEntryServices.findPasswordById(shareRequest.getPasswordId());
        List<PasswordEntry> passwordEntryList = receiver.getPasswordEntryList();
        passwordEntryList.add(passwordEntry);
        receiver.setPasswordEntryList(passwordEntryList);
        users.save(receiver);
        return passwordShareMap(sender, receiver, passwordEntry);
    }



    private void validateRegistration(RegisterRequest registerRequest) {
            users.findAll().forEach(user -> {if (user.getUsername().equalsIgnoreCase(registerRequest.getUsername()))throw new UsernameExistsException(registerRequest.getUsername()+" exists");});
            if (!registerRequest.getUsername().matches("^[a-zA-Z0-9]+$")) throw new InputMisMatchException("Invalid Input for username");
            if (registerRequest.getUsername().isEmpty())throw new InputMisMatchException("Invalid Input for username");
            if (registerRequest.getPassword().isEmpty())
                throw new InputMisMatchException("Invalid Password, provide a Password");
    }



}

