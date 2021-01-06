package com.welfare.persist.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * 联系人：关注监控的人
 */

@Entity
@Table(name = "contacts")
@Data
public class Contacts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @Column
    private String phone;
}
