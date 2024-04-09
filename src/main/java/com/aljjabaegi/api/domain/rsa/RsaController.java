package com.aljjabaegi.api.domain.rsa;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.domain.rsa.record.PublicKeyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * RSA Controller
 *
 * @author GEONLEE
 * @since 2024-04-03
 */
@RestController
@Tag(name = "00. RSA Public key", description = "Responsibility: GEONLEE")
@RequiredArgsConstructor
public class RsaController {
    private final RsaService rsaService;

    @PostMapping(value = "/v1/public-key")
    @Operation(summary = "Request RSA Public key", operationId = "API-RSA")
    public ResponseEntity<ItemResponse<PublicKeyResponse>> createUser() throws NoSuchAlgorithmException, InvalidKeySpecException {
        return rsaService.getPublicKey();
    }
}
