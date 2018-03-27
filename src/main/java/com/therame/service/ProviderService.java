package com.therame.service;

import com.therame.model.Provider;

import java.util.List;

public interface ProviderService {

    List<Provider> getProviders(String q);

}
