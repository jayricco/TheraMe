package com.therame.controllers;

import com.therame.model.DetailedUserDetails;
import com.therame.model.Provider;
import com.therame.service.ProviderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

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

    @PreAuthorize("hasAuthority('SENTINEL')")
    @PostMapping("/api/provider/create")
    public ResponseEntity<?> createProvider(@Valid Provider provider) {
        return ResponseEntity.ok(providerService.createProvider(provider));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/api/provider/update")
    public ResponseEntity<?> updateProvider(@Valid Provider provider, @AuthenticationPrincipal DetailedUserDetails userDetails) {
        if (userDetails.getUser().getProvider() != null) {
            provider.setId(userDetails.getUser().getProvider().getId());
        }

        return ResponseEntity.ok(providerService.createProvider(provider));
    }

    @PreAuthorize("hasAuthority('SENTINEL')")
    @GetMapping("/registerProvider")
    public ModelAndView createView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("provider", new Provider());
        modelAndView.setViewName("register_provider");
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/updateProvider")
    public ModelAndView updateView(@AuthenticationPrincipal DetailedUserDetails userDetails) {
        if (userDetails.getUser().getProvider() == null) {
            return new ModelAndView("redirect:registerProvider");
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("provider", providerService.getProviderById(userDetails.getUser().getProvider().getId()).get());
        modelAndView.setViewName("update_provider");
        return modelAndView;
    }

}
