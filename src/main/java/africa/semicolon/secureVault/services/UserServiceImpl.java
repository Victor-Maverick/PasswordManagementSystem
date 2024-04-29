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

import static africa.semicolon.secureVault.utils.Mapper.map;
import static africa.semicolon.secureVault.utils.Mapper.mapLogin;

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
            User user = users.findByUsername(loginRequest.getUsername());
            if (user == null)throw new UserNotFoundException(loginRequest.getUsername()+" not found");
            if (!user.getPassword().equals(loginRequest.getPassword()))throw new IncorrectPasswordException("wrong password");
            user.setLoggedIn(true);
            users.save(user);
            return mapLogin(user);
        }

        @Override
        public String logout(LogoutRequest logoutRequest) {
            User user = users.findByUsername(logoutRequest.getUsername());
            if(user == null)throw new UserNotFoundException(logoutRequest.getUsername()+ " not found");
            user.setLoggedIn(false);
            users.save(user);
            return "logout success";
        }

    @Override
    public String turnSafeModeOn(String username) {
            User user = users.findByUsername(username);
            if(user == null)throw new UserNotFoundException(username+" not found");
            validateUserLogin(user);
            user.setSecureModeOn(true);
        return "Secure mode on";
    }

    @Override
        public String deleteUser(DeleteUserRequest deleteUserRequest) {
            User user = users.findByUsername(deleteUserRequest.getUsername());
            if(user == null)throw new UserNotFoundException(deleteUserRequest.getUsername()+ " not found");
            validateUserLogin(user);
            if (user.isSecureModeOn())throw new InvalidCardException("No details found");
            users.delete(user);
            return "delete success";
        }



    @Override
    public AddCardResponse addCardInformation(AddCardRequest cardRequest) {
         User user = users.findByUsername(cardRequest.getUsername());
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
    public List<CreditCardInformation> findCardInformationFor(String username) {
            User user = users.findByUsername(username);
            if(user == null)throw new UserNotFoundException(username+" not found");
            validateUserLogin(user);
            if (user.isSecureModeOn())throw new InvalidCardException("No details found");
        return cardServices.findCardDetailsBelongingTo(username);
    }

    @Override
    public String deleteCardInformation(DeleteCardRequest deleteRequest) {
            User user = users.findByUsername(deleteRequest.getUsername());
            if(user == null)throw new UserNotFoundException(deleteRequest.getUsername()+ " not found");
            validateUserLogin(user);
        if (user.isSecureModeOn())throw new InvalidCardException("No details found");
        CreditCardInformation cardInformation = cardServices.findById(deleteRequest.getCardId());
        List<CreditCardInformation> cardList = user.getCardInformationList();
        cardList.remove(cardInformation);
        user.setCardInformationList(cardList);
        users.save(user);
        return cardServices.deleteCardInformation(deleteRequest);
    }

    @Override
    public ViewCardResponse viewCardDetails(ViewCardRequest viewRequest) {
            User user = users.findByUsername(viewRequest.getUsername());
            if(user == null)throw new UserNotFoundException(viewRequest.getUsername()+" not found");
            validateUserLogin(user);
        if (user.isSecureModeOn())throw new InvalidCardException("No details found");
        return cardServices.viewCardInformation(viewRequest);
    }

    @Override
    public PasswordEntryResponse addPasswordEntry(PasswordEntryRequest passwordRequest) {
            User user = users.findByUsername(passwordRequest.getUsername());
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
    public List<PasswordEntry> findPasswordEntriesFor(String username) {
            User user = users.findByUsername(username);
            if(user == null)throw new UserNotFoundException(username+" not found");
            validateUserLogin(user);
        if (user.isSecureModeOn())throw new InvalidCardException("No details found");
        return passwordEntryServices.findAllPasswordsFor(username);
    }

    @Override
    public String deletePasswordEntry(DeletePasswordEntryRequest deleteRequest) {
        User user = users.findByUsername(deleteRequest.getUsername());
        if (user == null) throw new UserNotFoundException(deleteRequest.getUsername()+" not found");
        validateUserLogin(user);
        if (user.isSecureModeOn())throw new InvalidCardException("No details found");
        var response = passwordEntryServices.deletePassword(deleteRequest);
        List<PasswordEntry> passwordEntryList = user.getPasswordEntryList();
        passwordEntryList.removeIf(passwordEntry1 -> passwordEntry1.getId().equals(deleteRequest.getId()));
        user.setPasswordEntryList(passwordEntryList);
        users.save(user);
        return response;
    }

    @Override
    public ViewPasswordResponse viewPassword(ViewPasswordRequest viewRequest) {
        User user = users.findByUsername(viewRequest.getUsername());
        if (user == null) throw new UserNotFoundException(viewRequest.getUsername()+" not found");
        validateUserLogin(user);
        if (user.isSecureModeOn())throw new InvalidCardException("No details found");
        return passwordEntryServices.viewPassword(viewRequest);
    }

    private void validateRegistration(RegisterRequest registerRequest) {
            users.findAll().forEach(user -> {if (user.getUsername().equalsIgnoreCase(registerRequest.getUsername()))throw new UsernameExistsException(registerRequest.getUsername()+" exists");});
            if (!registerRequest.getUsername().matches("^[a-zA-Z0-9]+$")) throw new InputMisMatchException("Invalid Input for username");
            if (registerRequest.getUsername().isEmpty())throw new InputMisMatchException("Invalid Input for username");
            if (registerRequest.getPassword().isEmpty())
                throw new InputMisMatchException("Invalid Password, provide a Password");
    }



}

