package com.welfare.service.remote.entity.request;

import com.welfare.common.enums.ConsumeTypeEnum;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.welfare.service.dto.RefundRequest;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/11 4:38 PM
 */
@Data
public class WoLifeRefundWriteOffDataRequest {

  /**
   * 订单id
   */
  @NotBlank
  private String oid;


  /**
   * rows
   */
  @NotNull
  private List<WoLifeRefundWriteOffRowsRequest> rows;

  public static WoLifeRefundWriteOffDataRequest of(RefundRequest refundRequest, String orderChannel){
    WoLifeRefundWriteOffDataRequest woLifeRefundWriteOffDataRequest = new WoLifeRefundWriteOffDataRequest();
    woLifeRefundWriteOffDataRequest.setOid(refundRequest.getOriginalTransNo());
    List<String> saleUnIds = refundRequest.getSaleUnIds();
    if(ConsumeTypeEnum.ONLINE_MALL.getCode().equals(orderChannel)) {
      if(CollectionUtils.isNotEmpty(saleUnIds)) {
        List<WoLifeRefundWriteOffRowsRequest> refundWriteOffRowsRequestList = new ArrayList<>(saleUnIds.size());

        for (String saleUnId:
            saleUnIds ) {
          WoLifeRefundWriteOffRowsRequest refundWriteOffRowsRequest = new WoLifeRefundWriteOffRowsRequest();
          refundWriteOffRowsRequest.setSaleUnId(saleUnId);
          refundWriteOffRowsRequestList.add(refundWriteOffRowsRequest);
        }
        woLifeRefundWriteOffDataRequest.setRows(refundWriteOffRowsRequestList);
      }

    } else {
      woLifeRefundWriteOffDataRequest.setRows(Collections.singletonList(WoLifeRefundWriteOffRowsRequest.of(refundRequest)));
    }

    return woLifeRefundWriteOffDataRequest;
  }
}
