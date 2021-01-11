package com.welfare.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Method;

/**
 * hao.yin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldMethodDTO {

    String dictType;
    Method gMethod;
    Method sMethod;
    Class type;



}