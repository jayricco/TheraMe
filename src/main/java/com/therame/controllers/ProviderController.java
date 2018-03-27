package com.therame.controllers;

import com.therame.model.Provider;
import com.therame.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProviderController {

    private ProviderService providerService;

    public ProviderController(ProviderService providerService) {
        this.providerService = providerService;
    }

    @PreAuthorize("hasAuthority('SENTINEL')")
    @GetMapping("/api/providers")
    public ResponseEntity<?> getProviders(@RequestParam(value = "q", required = false) String query) {
        List<Provider> providers = providerService.getProviders(query != null ? query : "");
        return ResponseEntity.ok(providers);
    }

}
