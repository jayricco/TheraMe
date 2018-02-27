package com.therame.repository.solr;

import com.therame.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("solrUserRepo")
public interface SolrUserRepository extends SolrCrudRepository<User, UUID> {
    List<User> findByEmail(String email);

    @Query("id:*?0* OR first_name:*?0* OR email:*?0* OR last_name:*?0*")
    Page<User> findByCustomQuery(String searchTerm, Pageable pageable);

    //@Query(name = "User.findByNamedQuery")
    //public Page<User> findByNamedQuery(String searchTerm, Pageable pageable);
}
