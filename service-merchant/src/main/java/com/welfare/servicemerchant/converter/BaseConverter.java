package com.welfare.servicemerchant.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @author hao.yin
 */
public interface BaseConverter<D, E> {

  /**
   * DTO转Entity
   */
  E toE(D dto);

  /**
   * Entity转DTO
   */
  D toD(E entity);

  /**
   * DTO集合转Entity集合
   */
  List<E> toE(List<D> dtoList);

  /**
   * Entity集合转DTO集合
   */
  List<D> toD(List<E> entityList);

  /**
   * Entity集合转DTO集合
   */
  Page<D> toD(Page<E> entityList);
}
