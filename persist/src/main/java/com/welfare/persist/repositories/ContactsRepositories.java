package com.welfare.persist.repositories;

import com.welfare.persist.domain.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactsRepositories extends JpaRepository<Contacts,Long> {
}