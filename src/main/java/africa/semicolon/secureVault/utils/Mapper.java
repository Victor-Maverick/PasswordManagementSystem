package africa.semicolon.secureVault.utils;
import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.data.models.User;
import africa.semicolon.secureVault.dtos.requests.AddCardRequest;
import africa.semicolon.secureVault.dtos.requests.PasswordEntryRequest;
import africa.semicolon.secureVault.dtos.requests.RegisterRequest;
import africa.semicolon.secureVault.dtos.responses.*;
import africa.semicolon.secureVault.exceptions.InvalidCardException;

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
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
    }
    public static LoginResponse mapLogin(User user){
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setLoggedIn(user.isLoggedIn());
        response.setUsername(user.getUsername());
        return response;
    }
    public static AddCardResponse map(CreditCardInformation cardInformation){
        AddCardResponse response = new AddCardResponse();
        response.setBankName(cardInformation.getBankName());
        response.setId(cardInformation.getId());
        response.setCardType(cardInformation.getCardType());
        return response;
    }

    public static void map(PasswordEntry passwordEntry, PasswordEntryRequest passwordRequest) throws Exception {
        int passKey = generateKey();
        passwordEntry.setPasswordKey(passKey);
        passwordEntry.setPassword(encrypt(passwordRequest.getPassword(), passwordEntry.getPasswordKey()));
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
        response.setPin(decrypt(cardInformation.getPin(), cardInformation.getPinKey()));
        response.setNameOnCard(encrypt(cardInformation.getNameOnCard(), cardInformation.getCardNumberKey()));
        response.setCardNumber(decrypt(cardInformation.getCardNumber(), cardInformation.getCardNumberKey()));
        response.setBankName(cardInformation.getBankName());
        return response;
    }

    public static ViewPasswordResponse passwordMap(PasswordEntry passwordEntry) throws Exception {
        ViewPasswordResponse response = new ViewPasswordResponse();
        response.setId(passwordEntry.getId());
        response.setWebsite(passwordEntry.getWebsite());
        response.setPassword(decrypt(passwordEntry.getPassword(), passwordEntry.getPasswordKey()));
        return response;
    }


    public static void map(CreditCardInformation cardInformation, AddCardRequest cardRequest) throws Exception {
        setCardType(cardInformation);
        int cardKey = generateKey();
        int pinKey = generateKey();
        cardInformation.setCardNumberKey(cardKey);
        cardInformation.setPinKey(pinKey);
        cardInformation.setCardNumber(encrypt(cardRequest.getCardNumber(), cardInformation.getCardNumberKey()));
        cardInformation.setUsername(cardRequest.getUsername());
        cardInformation.setNameOnCard(encrypt(cardRequest.getNameOnCard(), cardInformation.getCardNumberKey()));
        cardInformation.setBankName(encrypt(cardRequest.getBankName(), cardInformation.getCardNumberKey()));
        cardInformation.setPin(encrypt(cardRequest.getPin(), cardInformation.getPinKey()));
    }



    private static void setCardType(CreditCardInformation cardInformation) {
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

    public static boolean isValidCardNumber(AddCardRequest cardRequest){
        String cardNumber = cardRequest.getCardNumber().replaceAll("\\D", "");
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
}
