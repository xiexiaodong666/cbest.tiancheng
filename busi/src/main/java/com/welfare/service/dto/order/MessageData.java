package com.welfare.service.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 协议对象
 * {
 *     HEADER: { ......, ITEM7},
 *     ITEM2_LIST: [
 *          {......, ITEM3, ITEM4, ITEM5, ITEM6}
 *     ]
 *     ITEM8_LIST: [
 *          {......}
 *     ]
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String origin; // FJ/WSC
    // ITEM4, ITEM7 为 key-value
    private HEADER /*JSONObject*/ header;
    private ITEM1 /*JSONObject*/ item1;
    private List<ITEM2/*JSONObject*/> item2List;
    private List<ITEM8/*JSONObject*/> item8List;
}
