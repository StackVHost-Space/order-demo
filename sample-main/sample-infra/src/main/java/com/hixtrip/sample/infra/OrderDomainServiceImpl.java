package com.hixtrip.sample.infra;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Aurth：MingYHua
 * @Date：2024-04-12
 */
@Component
public class OrderDomainServiceImpl extends OrderDomainService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private InventoryDomainService inventoryDomainService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建订单
     * 使用token令牌机制实现幂等性
     * @param order
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order) throws Exception {
        //1、验证令牌是否合法【令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        //令牌验证失败，返回异常或者提示

        //令牌验证成功
        //2、创建订单、订单项等信息
        //createOrder();

        //3、验证价格
        //金额对比

        //4、保存订单

        //5、库存锁定,只要有异常，回滚订单数据

        //获取sku库存信息
        Integer inventory = inventoryDomainService.getInventory(order.getSkuId());
        Boolean flag = inventoryDomainService.changeInventory(order.getSkuId(), Long.valueOf(inventory), Long.valueOf(order.getAmount()), Long.valueOf(order.getAmount()));

        //可能出现的问题：扣减库存成功了，但是由于网络原因超时，出现异常，导致订单事务回滚，库存事务不回滚(解决方案：seata)
        //为了保证高并发，不推荐使用seata，因为是加锁，并行化，提升不了效率,可以发消息给库存服务
        if (flag){
            //锁定成功
            //订单创建成功，发送消息给MQ
            rabbitTemplate.convertAndSend("order-xxx-exchange","order.create.order",order.getId());

            //删除购物车里的数据
            redisTemplate.delete("xxxx");
        }else {
          //锁定失败
            throw new Exception("xxx");
        }

    }

    /**
     * 待付款订单支付成功
     * @param commandPay
     */
    @Override
    public void orderPaySuccess(CommandPay commandPay) {
        //修改订单状态-支付成功，以及保存支付宝交易相关信息
    }

    /**
     * 待付款订单支付失败
     * @param commandPay
     */
    @Override
    public void orderPayFail(CommandPay commandPay) {
        //支付失败,可能是超时未支付、支付宝回调失败等等，
        //超时未支付，有延迟队列处理
        //订单创建成功，会锁定库存，同时发送消息到延迟队列，订单到期会释放
        //库存锁定成功，也会发送消息到延迟队列，到期会解锁库存
        //订单到期释放，也要发送消息给库存，作为消息补偿，解锁库存
    }
}
