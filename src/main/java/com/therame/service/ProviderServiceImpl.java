package com.therame.service;

import com.therame.model.Provider;
import com.therame.model.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProviderServiceImpl implements ProviderService {

    private ProviderRepository providerRepository;

    public ProviderServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public List<Provider> getProviders(String q) {
        return providerRepository.findAllByName(q);
    }

    @Override
    public Optional<Provider> getProviderById(UUID id) {
        return Optional.ofNullable(providerRepository.findOne(id));
    }

    @Override
    public Provider createProvider(Provider provider) {
        return providerRepository.save(provider);
    }
}
