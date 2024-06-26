package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.data.models.User;
import africa.semicolon.secureVault.data.repositories.CardRepository;
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
        private final NotificationService notificationService;
    private final CardRepository cardRepository;

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
        AddCardResponse response = addCardToUser(cardRequest, user);
        users.save(user);
        return response;
    }

    private AddCardResponse addCardToUser(AddCardRequest cardRequest, User user) {
        AddCardResponse response = cardServices.addCardInformation(cardRequest);
        CreditCardInformation cardInformation = cardServices.findById(response.getId());
        List<CreditCardInformation> cardList = user.getCardInformationList();
        cardList.add(cardInformation);
        user.setCardInformationList(cardList);
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
    public List<PasswordEntry> findPasswordEntriesFor(FindUserPasswordsRequest findRequest) {
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
    public String deleteNotification(DeleteNotificationRequest request) {
            User user = users.findByUsername(request.getUsername().toLowerCase());
            if (user == null) throw new UserNotFoundException(request.getUsername()+" not found");
            validateUserLogin(user);
            List<Notification> notificationList = user.getNotificationList();
            notificationList.removeIf(notification -> notification.getId().equals(request.getNotificationId()));
            user.setNotificationList(notificationList);
            users.save(user);
            return notificationService.deleteNotification(request);
    }

    @Override
    public ViewNotificationResponse viewNotification(ViewNotificationRequest viewNotificationRequest) {
            User user = users.findByUsername(viewNotificationRequest.getUsername().toLowerCase());
            if(user == null)throw new UserNotFoundException(viewNotificationRequest.getUsername()+" not found");
            validateUserLogin(user);
        return notificationService.viewNotification(viewNotificationRequest);
    }

    @Override
    public List<Notification> findNotificationsFor(FindNotificationRequest findRequest) {
            User user = users.findByUsername(findRequest.getUsername().toLowerCase());
            if(user == null)throw new UserNotFoundException(findRequest.getUsername()+" not found");
            validateUserLogin(user);
        return notificationService.viewAllNotificationsFor(findRequest);
    }

    @Override
    public ViewPasswordResponse viewPassword(ViewPasswordRequest viewRequest) {
        User owner = users.findByUsername(viewRequest.getAuthorName().toLowerCase());
        if (owner == null) throw new UserNotFoundException(viewRequest.getAuthorName()+" not found");
        User viewer = users.findByUsername(viewRequest.getViewerName().toLowerCase());
        if (viewer == null) throw new UserNotFoundException(viewRequest.getViewerName()+" not found");
        validateUserLogin(viewer);
        return passwordEntryServices.viewPassword(viewRequest);
    }

    @Override
    public ShareDetailsResponse shareCardInformation(ShareCardDetailsRequest shareRequest) {
        User sender = users.findByUsername(shareRequest.getSenderUsername().toLowerCase());
        if(sender == null) throw new UserNotFoundException(shareRequest.getSenderUsername()+" not found");
        validateUserLogin(sender);
        User receiver = users.findByUsername(shareRequest.getReceiverUsername().toLowerCase());
        if(receiver == null) throw new UserNotFoundException(shareRequest.getReceiverUsername()+" not found");
        CreditCardInformation cardInformation = shareCardToReceiver(shareRequest, receiver);
        notifyReceiver(sender, cardInformation, receiver);
        users.save(receiver);
        return shareCardMap(sender, receiver, cardInformation);
    }


    @Override
    public ShareDetailsResponse sharePassword(SharePasswordRequest shareRequest) {
        User sender = users.findByUsername(shareRequest.getSenderName().toLowerCase());
        if(sender == null) throw new UserNotFoundException(shareRequest.getSenderName()+" not found");
        validateUserLogin(sender);
        List<PasswordEntry> senderPasswords = sender.getPasswordEntryList();
        PasswordEntry passwordEntry = passwordEntryServices.findPasswordById(shareRequest.getPasswordId());
        //if (!senderPasswords.contains(passwordEntry))throw new PasswordNotFoundException("not found");
        User receiver = users.findByUsername(shareRequest.getReceiverName().toLowerCase());
        passwordEntry = addPasswordToReceiver(shareRequest, receiver);
        notifyReceiverForPassword(sender, passwordEntry, receiver);
        users.save(receiver);
        return passwordShareMap(sender, receiver, passwordEntry);
    }

    @Override
    public EditPasswordResponse editPassword(EditPasswordRequest editPasswordRequest) {
            User user = users.findByUsername(editPasswordRequest.getUsername());
            if(user == null) throw new UserNotFoundException(editPasswordRequest.getUsername()+" not found");
            validateUserLogin(user);
            var editResponse = passwordEntryServices.editPassword(editPasswordRequest);
            PasswordEntry passwordEntry = passwordEntryServices.findPasswordById(editResponse.getPasswordId());
            List<PasswordEntry> passwordEntryList = user.getPasswordEntryList();
            passwordEntryList.removeIf(passwordEntry1 -> passwordEntry1.getId().equals(editPasswordRequest.getPasswordId()));
            passwordEntryList.add(passwordEntry);
            user.setPasswordEntryList(passwordEntryList);
            users.save(user);
          return editResponse;
    }

    @Override
    public EditCardResponse editCardInformation(EditCardDetailsRequest request) {
            User user = users.findByUsername(request.getUsername().toLowerCase());
            if(user == null) throw new UserNotFoundException(request.getUsername()+" not found");
            validateUserLogin(user);
            var editResponse = cardServices.editCardInformation(request);
            CreditCardInformation cardInformation = cardRepository.findCardById(editResponse.getCardId());
            List<CreditCardInformation>cardInformationList = user.getCardInformationList();
            cardInformationList.removeIf(cardInformation1 -> cardInformation1.getId().equals(editResponse.getCardId()));
            cardInformationList.add(cardInformation);
            user.setCardInformationList(cardInformationList);
            users.save(user);
        return editResponse;
    }

    private void notifyReceiverForPassword(User sender, PasswordEntry passwordEntry, User receiver) {
        NotificationRequest request = new NotificationRequest();
        request.setMessage(sender.getUsername() + " sent you a password");
        request.setDetailId(passwordEntry.getId());
        request.setRecipientName(receiver.getUsername());
        var notificationResponse = notificationService.sendNotification(request);
        addNotificationTo(receiver, notificationResponse);
    }



    private void notifyReceiver(User sender, CreditCardInformation cardInformation, User receiver) {
        NotificationRequest request = new NotificationRequest();
        request.setMessage(sender.getUsername() + " sent you a card!!");
        request.setDetailId(cardInformation.getId());
        request.setRecipientName(receiver.getUsername());
        var notificationResponse = notificationService.sendNotification(request);
        addNotificationTo(receiver, notificationResponse);
    }

    private void addNotificationTo(User receiver, NotificationResponse notificationResponse) {
        Notification notification = notificationService.findById(notificationResponse.getNotificationId());
        List<Notification>receiverNotifications = receiver.getNotificationList();
        receiverNotifications.add(notification);
        receiver.setNotificationList(receiverNotifications);
    }

    private CreditCardInformation shareCardToReceiver(ShareCardDetailsRequest shareRequest, User receiver) {
        CreditCardInformation cardInformation = cardServices.findById(shareRequest.getCardId());
        List<CreditCardInformation> cardList = receiver.getCardInformationList();
        cardList.add(cardInformation);
        receiver.setCardInformationList(cardList);
        return cardInformation;
    }

    private PasswordEntry addPasswordToReceiver(SharePasswordRequest shareRequest, User receiver) {
        if(receiver == null) throw new UserNotFoundException(shareRequest.getReceiverName()+" not found");
        PasswordEntry passwordEntry = passwordEntryServices.findPasswordById(shareRequest.getPasswordId());
        List<PasswordEntry> passwordEntryList = receiver.getPasswordEntryList();
        passwordEntryList.add(passwordEntry);
        receiver.setPasswordEntryList(passwordEntryList);
        return passwordEntry;
    }




    private void validateRegistration(RegisterRequest registerRequest) {
            users.findAll().forEach(user -> {if (user.getUsername().equalsIgnoreCase(registerRequest.getUsername()))throw new UsernameExistsException(registerRequest.getUsername()+" exists");});
            if (!registerRequest.getUsername().matches("^[a-zA-Z0-9]+$")) throw new InputMisMatchException("Invalid Input for username");
            if (registerRequest.getUsername().isEmpty())throw new InputMisMatchException("Invalid Input for username");
            if (registerRequest.getPassword().isEmpty())
                throw new InputMisMatchException("Invalid Password, provide a Password");
    }



}

