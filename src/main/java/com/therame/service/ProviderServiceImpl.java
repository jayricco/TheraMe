package com.therame.service;

import com.therame.model.Provider;
import com.therame.model.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public Provider createProvider(Provider provider) {
        return providerRepository.save(provider);
    }
}
