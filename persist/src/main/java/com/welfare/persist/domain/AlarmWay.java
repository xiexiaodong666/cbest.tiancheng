package com.welfare.persist.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * 监控通知方式
 */

@Entity
@Table(name = "alarm_way")
@Data
public class AlarmWay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long contactId;

    @Column
    private Long ruleId;

    @Column
    private String way;
}
