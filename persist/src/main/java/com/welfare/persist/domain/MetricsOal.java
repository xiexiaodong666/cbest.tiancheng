package com.welfare.persist.domain;


import lombok.Data;

import javax.persistence.*;

/**
 * skywalking系统初始化的oal查询或者后期自定义的oal查询
 * 在config/oal/目录下的文件里。
 */

@Entity
@Table(name = "metrics_oal")
@Data
public class MetricsOal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;
}
