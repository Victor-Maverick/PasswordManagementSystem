package africa.semicolon.secureVault.controllers;

import africa.semicolon.secureVault.data.models.Notification;
import africa.semicolon.secureVault.data.models.PasswordEntry;
import africa.semicolon.secureVault.dtos.requests.*;
import africa.semicolon.secureVault.dtos.responses.*;
import africa.semicolon.secureVault.exceptions.SecureVaultAppExceptions;
import africa.semicolon.secureVault.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        try{
            RegisterResponse response = userService.register(registerRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        try{
            LoginResponse response = userService.login(loginRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @PostMapping("/sign-out")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest){
        try{
            String response = userService.logout(logoutRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/sign-off")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest deleteRequest){
        try{
            String response = userService.deleteUser(deleteRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/add-cardInfo")
    public ResponseEntity<?> addCardDetails(@RequestBody AddCardRequest cardRequest){
        try{
            AddCardResponse response = userService.addCardInformation(cardRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }


    @PatchMapping("/edit-card-info")
    public ResponseEntity<?> editCardInformation(@RequestBody EditCardDetailsRequest request){
        try{
            EditCardResponse response = userService.editCardInformation(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/view-cardInfo")
    public ResponseEntity<?> viewCardInformation(@RequestBody ViewCardRequest viewRequest){
        try{
            ViewCardResponse response = userService.viewCardDetails(viewRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/view-cardsFor")
    public ResponseEntity<?> viewAllCardsFor(@RequestBody FindDetailsRequest request){
        try{
            var response = userService.findCardInformationFor(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-Card")
    public ResponseEntity<?> deleteCardDetails(@RequestBody DeleteCardRequest deleteRequest){
        try{
            String response = userService.deleteCardInformation(deleteRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/add-password")
    public ResponseEntity<?> addPasswordEntry(@RequestBody PasswordEntryRequest passwordRequest){
        try{
            PasswordEntryResponse response = userService.addPasswordEntry(passwordRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }
    @PatchMapping("/edit-password")
    public ResponseEntity<?> editPassword(@RequestBody EditPasswordRequest request){
        try{
            EditPasswordResponse response = userService.editPassword(request);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/view-password")
    public ResponseEntity<?> viewPassword(@RequestBody ViewPasswordRequest viewRequest){
        try{
            ViewPasswordResponse response = userService.viewPassword(viewRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/view-allPasswordsFor")
    public ResponseEntity<?> viewAllPasswords(@RequestBody FindUserPasswordsRequest findRequest){
        try{
            List<PasswordEntry> response = userService.findPasswordEntriesFor(findRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-password")
    public ResponseEntity<?> deletePassword(@RequestBody DeletePasswordEntryRequest deleteRequest){
        try{
            String response = userService.deletePasswordEntry(deleteRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/share-card")
    public ResponseEntity<?> shareCardDetails(@RequestBody ShareCardDetailsRequest request){
        try{
            var response = userService.shareCardInformation(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @PostMapping("/share-password")
    public ResponseEntity<?> sharePassword(@RequestBody SharePasswordRequest request){
        try{
            var response = userService.sharePassword(request);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/view-notification")
    public ResponseEntity<?> viewNotification(@RequestBody ViewNotificationRequest viewRequest){
        try{
            ViewNotificationResponse response = userService.viewNotification(viewRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotification(@RequestBody DeleteNotificationRequest deleteRequest){
        try{
            var response = userService.deleteNotification(deleteRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> viewAllNotifications(@RequestBody FindNotificationRequest findRequest){
        try{
            List<Notification> notifications = userService.findNotificationsFor(findRequest);
            return new ResponseEntity<>(new ApiResponse(true, notifications), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

}
