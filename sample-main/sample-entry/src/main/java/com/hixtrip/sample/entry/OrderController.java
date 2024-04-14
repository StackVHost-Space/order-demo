package com.hixtrip.sample.entry;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.hixtrip.sample.client.order.dto.CommandOderCreateDTO;
import com.hixtrip.sample.client.order.dto.CommandPayDTO;
import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import com.hixtrip.sample.domain.order.OrderDomainService;
import com.hixtrip.sample.domain.order.interceptor.LoginUserInterceptor;
import com.hixtrip.sample.domain.order.model.Order;
import com.hixtrip.sample.domain.pay.PayDomainService;
import com.hixtrip.sample.domain.pay.model.CommandPay;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * todo 这是你要实现的
 */
@RestController
public class OrderController {

    @Autowired
    private OrderDomainService orderDomainService;

    @Autowired
    private PayDomainService payDomainService;

    /**
     * todo 这是你要实现的接口
     *
     * @param commandOderCreateDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/create")
    public String order(@RequestBody CommandOderCreateDTO commandOderCreateDTO) {

        //获取当前用户登录的信息
        //XXX xx = LoginUserInterceptor.loginUser.get();

        Order order = new Order();
        BeanUtils.copyProperties(commandOderCreateDTO,order);
        order.setUserId("xxx");
        order.setUpdateBy("xxx");
        order.setCreateBy("xxx");
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        try {
            orderDomainService.createOrder(order);
        } catch (Exception e) {
            throw new RuntimeException("创建失败！");
        }
        return "创建成功";
    }

    /**
     * todo 这是模拟创建订单后，支付结果的回调通知
     * 【中、高级要求】需要使用策略模式处理至少三种场景：支付成功、支付失败、重复支付(自行设计回调报文进行重复判定)
     *
     * @param commandPayDTO 入参对象
     * @return 请修改出参对象
     */
    @PostMapping(path = "/command/order/pay/callback",produces = "text/html")
    public String payCallback(@RequestBody CommandPayDTO commandPayDTO) {
        CommandPay commandPay = new CommandPay();
        BeanUtils.copyProperties(commandPayDTO,commandPay);
        //获取支付宝异步回调结果
        String  tradeStatus = payDomainService.payRecord(commandPay);


        //调用SDK验证签名
        boolean signVerified =false;
//        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.getAlipay_public_key(),
//                alipayTemplate.getCharset(), alipayTemplate.getSign_type());

        if (signVerified) {
            System.out.println("签名验证成功...");
            if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {
                //支付成功状态
                //修改订单状态-支付成功，以及保存支付宝交易相关信息
                orderDomainService.orderPaySuccess(commandPay);
            }else {
                //支付失败
                //修改订单状态-支付失败，以及保存支付宝交易相关信息
                orderDomainService.orderPayFail(commandPay);

            }
        } else {
            System.out.println("签名验证失败...");
            return "error";
        }

        return "SUCCESS";
    }

}
