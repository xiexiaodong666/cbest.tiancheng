package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2019-11-03 21:28
 */
@Data
@JsonInclude(value = Include.NON_NULL)
public class MerchantUpdateEvt extends MerchantEvt {

}
