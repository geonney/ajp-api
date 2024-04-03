package com.aljjabaegi.api.domain.rsa.record;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * RSA Public key 응답
 *
 * @author GEONLEE
 * @since 2024-04-03<br />
 **/
@Schema(description = "RSA PublicKey 응답")
@Builder
public record PublicKeyResponse(
        @Schema(description = "RSA Public key", example = "MIIBIjANBgkqhkiG9w0BAQEFAAOC...")
        String publicKey) {
}