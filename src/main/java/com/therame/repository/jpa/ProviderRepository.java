package com.therame.repository.jpa;

import com.therame.model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("providerRepo")
public interface ProviderRepository extends JpaRepository<Provider, UUID> {

    @Query("select p from Provider p where upper(name) like upper(concat('%', :name, '%'))")
    List<Provider> findAllByName(@Param("name") String title);

}
