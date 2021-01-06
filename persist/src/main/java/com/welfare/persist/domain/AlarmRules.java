package com.welfare.persist.domain;


import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 告警规则配置，用于生成alarm-setting.xml文件格式的配置文件
 */

@Entity
@Table(name = "alarm_rules")
@Data
public class AlarmRules {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String ruleName;

    @Column
    private String metricsName;

    @Column
    private String op;

    @Column
    private String threshold;

    @Column
    private Long period;

    @Column
    private Long SilencePeriod;

    @Column
    private String message;

    @Column
    private String includeNames;

    @Column
    private String excludeNames;

    @Column
    private String includeNamesRegex;

    @Column
    private String excludeNamesRegex;


}
