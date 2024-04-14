package com.hixtrip.sample.infra;

import com.hixtrip.sample.domain.inventory.InventoryDomainService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

public class InventoryDomainServiceImpl extends InventoryDomainService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询sku库存
     *
     * 从redis中获取数据
     * @param skuId
     * @return
     */
    @Override
    public Integer getInventory(String skuId) {
        //简单的实现查询
        String key = "SkuHasStock"+skuId;
        String skuount = (String) redisTemplate.opsForValue().get(key);
        Integer count = Integer.valueOf(skuount);
        return count;
    }

    /**
     * 修改库存
     *
     *
     * @param skuId
     * @param sellableQuantity    可售库存
     * @param withholdingQuantity 预占库存
     * @param occupiedQuantity    占用库存
     * @return
     */
    @Override
    public Boolean changeInventory(String skuId, Long sellableQuantity, Long withholdingQuantity, Long occupiedQuantity) {
       // 1、保存库存工作单详情信息
        //wareOrderTaskService.save(wareOrderTask);


        //2、按照下单的收货地址，找到一个就近仓库，锁定库存
        //3、找到每个商品在哪个仓库都有库存

        //4、如果每一个商品都锁定成功,将当前商品锁定了几件的工作单记录发给MQ
        //4.1 保存库存工作单详情信息, wareOrderTaskDetailService.save(taskDetailEntity);
        //4.2 告诉MQ库存锁定成功
        String key = "SkuHasStock"+skuId;
        String skuount = (String) redisTemplate.opsForValue().get(key);
        Integer count = Integer.valueOf(skuount);
        if (count>withholdingQuantity){
            redisTemplate.opsForValue().set(key,count-withholdingQuantity);
            //rabbitTemplate.convertAndSend("exchange","stock.locked",lockedTo);
            return true;
        }else {
            //5、锁定失败。前面保存的工作单信息都回滚了。发送出去的消息，即使要解锁库存，由于在数据库查不到指定的id，所有就不用解锁
            //库存不足
            return false;
        }

    }
}

