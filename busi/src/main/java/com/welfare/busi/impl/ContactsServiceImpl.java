package com.welfare.busi.impl;

import com.welfare.busi.ContactsService;
import com.welfare.persist.domain.Contacts;
import com.welfare.persist.repositories.ContactsRepositories;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ContactsServiceImpl  implements ContactsService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ContactsRepositories contactsRepositories;


    @Transactional
    @Override
    public void add(Contacts contacts) {
        contactsRepositories.save(contacts);
    }
}
