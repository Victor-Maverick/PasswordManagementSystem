package africa.semicolon.secureVault.utils;
import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.data.models.User;
import africa.semicolon.secureVault.dtos.requests.AddCardRequest;
import africa.semicolon.secureVault.dtos.requests.EditCardDetailsRequest;
import africa.semicolon.secureVault.dtos.requests.PasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.RegisterRequest;
import africa.semicolon.secureVault.dtos.responses.*;
import africa.semicolon.secureVault.exceptions.InvalidCardException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static africa.semicolon.secureVault.data.models.CardType.*;
import static africa.semicolon.secureVault.utils.EncryptDecrypt.*;
import static africa.semicolon.secureVault.utils.GenerateKeys.generateKey;

public class Mapper {

    public static RegisterResponse map(User user){
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setDateCreated(user.getDateCreated());
        return response;
    }
    public static void map(User user, RegisterRequest registerRequest){
        int id = generateKey();
        user.setIdNumber(id * 456);
        user.setUsername(registerRequest.getUsername().toLowerCase());
        user.setPassword(encrypt(registerRequest.getPassword(), user.getIdNumber()/456));
    }
    public static LoginResponse mapLogin(User user){
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setLoggedIn(user.isLoggedIn());
        List<Notification> notifications = user.getNotificationList().stream().filter(notification -> !notification.isSeen()).toList();
        response.setNotifications(notifications);
        return response;
    }
    public static AddCardResponse map(CreditCardInformation cardInformation){
        AddCardResponse response = new AddCardResponse();
        response.setBankName(decrypt(cardInformation.getBankName(), cardInformation.getCardId()/56));
        response.setId(cardInformation.getId());
        response.setCardType(cardInformation.getCardType());
        return response;
    }

    public static void map(PasswordEntry passwordEntry, PasswordEntryRequest passwordRequest) throws Exception {
        int passKey = generateKey();
        passwordEntry.setPasswordId(passKey*47);
        passwordEntry.setPassword(encrypt(passwordRequest.getPassword(), passwordEntry.getPasswordId()/47));
        passwordEntry.setUsername(passwordRequest.getUsername());
        passwordEntry.setWebsite(passwordRequest.getWebsite());
    }
    public static PasswordEntryResponse map(PasswordEntry passwordEntry){
        PasswordEntryResponse response = new PasswordEntryResponse();
        response.setId(passwordEntry.getId());
        response.setWebsite(passwordEntry.getWebsite());
        return response;
    }
    public static ViewCardResponse mapCard(CreditCardInformation cardInformation) throws Exception {
        ViewCardResponse response = new ViewCardResponse();
        response.setUsername(cardInformation.getUsername());
        response.setCardType(cardInformation.getCardType());
        response.setPin(decrypt(cardInformation.getPin(), cardInformation.getIdNumber()/53));
        response.setNameOnCard(decrypt(cardInformation.getNameOnCard(), cardInformation.getCardId()/56));
        response.setCardNumber(decrypt(cardInformation.getCardNumber(), cardInformation.getCardId()/56));
        response.setBankName(decrypt(cardInformation.getBankName(), cardInformation.getCardId()/56));
        return response;
    }

    public static ViewPasswordResponse passwordMap(PasswordEntry passwordEntry) throws Exception {
        ViewPasswordResponse response = new ViewPasswordResponse();
        response.setId(passwordEntry.getId());
        response.setWebsite(passwordEntry.getWebsite());
        response.setPassword(decrypt(passwordEntry.getPassword(), passwordEntry.getPasswordId()/47));
        return response;
    }


    public static void map(CreditCardInformation cardInformation, AddCardRequest cardRequest) throws Exception {
        cardInformation.setCardNumber(cardRequest.getCardNumber());
        setCardType(cardInformation);
        int cardKey = generateKey();
        int pinKey = generateKey();
        cardInformation.setCardId(cardKey*56);
        cardInformation.setIdNumber(pinKey*53);
        cardInformation.setCardNumber(encrypt(cardRequest.getCardNumber(), cardInformation.getCardId()/56));
        cardInformation.setUsername(cardRequest.getUsername());
        cardInformation.setNameOnCard(encrypt(cardRequest.getNameOnCard(), cardInformation.getCardId()/56));
        cardInformation.setBankName(encrypt(cardRequest.getBankName(), cardInformation.getCardId()/56));
        cardInformation.setPin(encrypt(cardRequest.getPin(), cardInformation.getIdNumber()/53));
    }



    public static void setCardType(CreditCardInformation cardInformation) {
        switch (cardInformation.getCardNumber().charAt(0)){
            case '4'-> cardInformation.setCardType(VISA);
            case '5'-> cardInformation.setCardType(MASTER_CARD);
            case '6'-> cardInformation.setCardType(VERVE_CARD);
            case '3' -> {
                if (cardInformation.getCardNumber().charAt(1) == '7') cardInformation.setCardType(AMERICAN_EXPRESS);
            }
            default -> throw new InvalidCardException("invalid card");

        }
    }

    public static ShareDetailsResponse shareCardMap(User sender, User receiver, CreditCardInformation cardInformation){
        ShareDetailsResponse response = new ShareDetailsResponse();
        response.setDetailId(cardInformation.getId());
        response.setSenderName(sender.getUsername());
        response.setReceiverName(receiver.getUsername());
        return response;
    }
    public static ShareDetailsResponse passwordShareMap(User sender, User receiver, PasswordEntry passwordEntry){
        ShareDetailsResponse response = new ShareDetailsResponse();
        response.setDetailId(passwordEntry.getId());
        response.setSenderName(sender.getUsername());
        response.setReceiverName(receiver.getUsername());
        return response;
    }

    public static boolean isValidCardNumber(String cardNumber){
        cardNumber = cardNumber.replaceAll("\\D", "");
        if (cardNumber.length() < 13 || cardNumber.length() > 16) {
            return false;
        }
        int total = 0;
        for (int count = 0; count < cardNumber.length(); count++) {
            int digit = Character.getNumericValue(cardNumber.charAt(count));
            if (count % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            total += digit;
        }
        return total % 10 == 0;
    }

    public static void map(CreditCardInformation cardInformation, EditCardDetailsRequest request){
        cardInformation.setCardType(request.getCardType());
        cardInformation.setCardNumber(encrypt(request.getCardNumber(), cardInformation.getCardId()/56));
        cardInformation.setPin(encrypt(request.getPin(), cardInformation.getIdNumber()/53));
        cardInformation.setNameOnCard(encrypt(request.getNameOnCard(), cardInformation.getCardId()/56));
        cardInformation.setBankName(encrypt(request.getBankName(), cardInformation.getCardId()/56));
    }

    public static EditCardResponse mapEditCard(CreditCardInformation cardInformation){
        EditCardResponse response = new EditCardResponse();
        response.setDateEdited(LocalDateTime.now());
        response.setCardId(cardInformation.getId());
        response.setNewBankName(decrypt(cardInformation.getBankName(), cardInformation.getCardId()/56));
        return response;
    }
    public static EditPasswordResponse mapEditPassword(PasswordEntry passwordEntry){
        EditPasswordResponse response = new EditPasswordResponse();
        response.setPasswordId(passwordEntry.getId());
        response.setDateEdited(LocalDateTime.now());
        response.setWebsite(passwordEntry.getWebsite());
        return response;
    }
}
