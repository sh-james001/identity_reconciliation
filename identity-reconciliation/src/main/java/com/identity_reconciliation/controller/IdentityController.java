package com.identity_reconciliation.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.identity_reconciliation.reponse.IdentifyResponse;
import com.identity_reconciliation.request.IdentifyRequest;
import com.identity_reconciliation.service.IdentityService;

@RestController
public class IdentityController {
    private final IdentityService identityService;

    public IdentityController(IdentityService identityService) {
        this.identityService = identityService;
    }

    @PostMapping("/identify")
    public ResponseEntity<IdentifyResponse> identifyContact(@RequestBody IdentifyRequest request) {
        IdentifyResponse response = identityService.identifyContact(request);
        return ResponseEntity.ok(response);
    }
}
