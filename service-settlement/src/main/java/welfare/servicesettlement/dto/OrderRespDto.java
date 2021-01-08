package welfare.servicesettlement.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: e-welfare
 * @Package: welfare.servicesettlement.dto
 * @ClassName: OrderRespDto
 * @Author: jian.zhou
 * @Description: 订单实体
 * @Date: 2021/1/7 20:42
 * @Version: 1.0
 */
@ApiModel("订单实体")
@Data
public class OrderRespDto implements Serializable {

    @ApiModelProperty("订单编号")
    private String orderId;
    @ApiModelProperty("商品")
    private String goods;
    @ApiModelProperty("门店编码")
    private String storeCode;
    @ApiModelProperty("门店名称")
    private String storeName;
    @ApiModelProperty("账户名称")
    private String accountName;
    @ApiModelProperty("账户编码")
    private String accountCode;
    @ApiModelProperty("卡号")
    private String accountCardId;
    @ApiModelProperty("订单总金额")
    private String orderAmount;
    @ApiModelProperty("订单创建时间")
    private String orderDateTime;
}
