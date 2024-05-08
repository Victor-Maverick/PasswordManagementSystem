package africa.semicolon.secureVault.services;

import africa.semicolon.secureVault.data.models.CreditCardInformation;
import africa.semicolon.secureVault.dtos.requests.*;
import africa.semicolon.secureVault.dtos.responses.AddCardResponse;
import africa.semicolon.secureVault.dtos.responses.EditCardResponse;
import africa.semicolon.secureVault.dtos.responses.ViewCardResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CreditCardServices {

    AddCardResponse addCardInformation(AddCardRequest cardRequest) throws Exception;

    String deleteCardInformation(DeleteCardRequest deleteRequest);

    List<CreditCardInformation> findCardDetailsBelongingTo(FindDetailsRequest request);

    ViewCardResponse viewCardInformation(ViewCardRequest viewRequest);

    CreditCardInformation findById(String id);

    EditCardResponse editCardInformation(EditCardDetailsRequest request);
}
