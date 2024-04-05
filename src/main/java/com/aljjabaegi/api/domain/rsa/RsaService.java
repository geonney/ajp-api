package com.aljjabaegi.api.domain.rsa;

import com.aljjabaegi.api.common.response.ItemResponse;
import com.aljjabaegi.api.config.security.rsa.RsaProvider;
import com.aljjabaegi.api.domain.rsa.record.PublicKeyResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Base64;

/**
 * RSA service
 *
 * @author GEONLEE
 * @since 2024-04-02
 */
@Service
@RequiredArgsConstructor
public class RsaService {

    private final RsaProvider rsaProvider;

    @Transactional
    public ResponseEntity<ItemResponse<PublicKeyResponse>> getPublicKey() {
        PublicKey publicKey = rsaProvider.getPublicKey();
        return ResponseEntity.ok()
                .body(ItemResponse.<PublicKeyResponse>builder()
                        .status("OK")
                        .message("데이터를 조회하는데 성공하였습니다.")
                        .item(PublicKeyResponse.builder()
                                .publicKey(Base64.getEncoder().encodeToString(publicKey.getEncoded()))
                                .build())
                        .build());
    }
}
