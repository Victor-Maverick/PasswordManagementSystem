package africa.semicolon.secureVault.controllers;

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

    @GetMapping("/view-cardInfo")
    public ResponseEntity<?> viewCardInformation(@RequestBody ViewCardRequest viewRequest){
        try{
            ViewCardResponse response = userService.viewCardDetails(viewRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), OK);
        }catch (SecureVaultAppExceptions e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), BAD_REQUEST);
        }
    }

    @GetMapping("/view-cardsFor{username}")
    public ResponseEntity<?> viewAllCardsFor(@PathVariable("username") String username){
        try{
            var response = userService.findCardInformationFor(username);
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
    public ResponseEntity<?> viewAllPasswords(@RequestBody FindUserEntriesRequest findRequest){
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

}
//--> Unlock before a findAllPasswords
//--> Use a question to reset the unlock password
//--> Generate password
//-->
