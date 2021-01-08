package welfare.servicesettlement.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import welfare.servicesettlement.dto.BillRespDto;
import welfare.servicesettlement.dto.OrderReqDto;
import welfare.servicesettlement.dto.OrderRespDto;

/**
 * @ProjectName: e-welfare
 * @Package: welfare.servicesettlement.controller
 * @ClassName: OrderController
 * @Author: jian.zhou
 * @Description: 订单控制器
 * @Date: 2021/1/7 20:24
 * @Version: 1.0
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/settlement/order")
@Api(tags = "线下订单管理")
public class OrderController implements IController {

    @GetMapping("selectPage")
    public R<Page<OrderRespDto>> selectByPage(@RequestBody OrderReqDto orderReqDto){
        Page<OrderRespDto> accountPage = null;
        //TODO 查询商户的配置门店表
        //TODO 拼装条件查询
        return success(accountPage);
    }
}
