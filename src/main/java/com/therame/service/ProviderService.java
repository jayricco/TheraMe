package com.therame.service;

import com.therame.model.Provider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderService {

    List<Provider> getProviders(String q);

    Optional<Provider> getProviderById(UUID id);

    Provider createProvider(Provider provider);

}
