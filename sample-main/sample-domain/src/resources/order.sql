# 给出订单主表、订单详情表设计

DROP TABLE IF EXISTS `myh_order`;
CREATE TABLE `myh_order` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                             `member_id` bigint(20) DEFAULT NULL COMMENT '会员id',
                             `order_sn` char(64) DEFAULT NULL COMMENT '订单号',
                             `coupon_id` bigint(20) DEFAULT NULL COMMENT '使用的优惠券',
                             `create_time` datetime DEFAULT NULL COMMENT 'create_time',
                             `member_username` varchar(200) DEFAULT NULL COMMENT '用户名',
                             `total_amount` decimal(18,4) DEFAULT NULL COMMENT '订单总额',
                             `pay_amount` decimal(18,4) DEFAULT NULL COMMENT '应付总额',
                             `freight_amount` decimal(18,4) DEFAULT NULL COMMENT '运费金额',
                             `promotion_amount` decimal(18,4) DEFAULT NULL COMMENT '促销优化金额（促销价、满减、阶梯价）',
                             `integration_amount` decimal(18,4) DEFAULT NULL COMMENT '积分抵扣金额',
                             `coupon_amount` decimal(18,4) DEFAULT NULL COMMENT '优惠券抵扣金额',
                             `discount_amount` decimal(18,4) DEFAULT NULL COMMENT '后台调整订单使用的折扣金额',
                             `pay_type` tinyint(4) DEFAULT NULL COMMENT '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】',
                             `source_type` tinyint(4) DEFAULT NULL COMMENT '订单来源[0->PC订单；1->app订单]',
                             `status` tinyint(4) DEFAULT NULL COMMENT '订单状态【0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单】',
                             `delivery_company` varchar(64) DEFAULT NULL COMMENT '物流公司(配送方式)',
                             `delivery_sn` varchar(64) DEFAULT NULL COMMENT '物流单号',
                             `auto_confirm_day` int(11) DEFAULT NULL COMMENT '自动确认时间（天）',
                             `integration` int(11) DEFAULT NULL COMMENT '可以获得的积分',
                             `growth` int(11) DEFAULT NULL COMMENT '可以获得的成长值',
                             `bill_type` tinyint(4) DEFAULT NULL COMMENT '发票类型[0->不开发票；1->电子发票；2->纸质发票]',
                             `bill_header` varchar(255) DEFAULT NULL COMMENT '发票抬头',
                             `bill_content` varchar(255) DEFAULT NULL COMMENT '发票内容',
                             `bill_receiver_phone` varchar(32) DEFAULT NULL COMMENT '收票人电话',
                             `bill_receiver_email` varchar(64) DEFAULT NULL COMMENT '收票人邮箱',
                             `receiver_name` varchar(100) DEFAULT NULL COMMENT '收货人姓名',
                             `receiver_phone` varchar(32) DEFAULT NULL COMMENT '收货人电话',
                             `receiver_post_code` varchar(32) DEFAULT NULL COMMENT '收货人邮编',
                             `receiver_province` varchar(32) DEFAULT NULL COMMENT '省份/直辖市',
                             `receiver_city` varchar(32) DEFAULT NULL COMMENT '城市',
                             `receiver_region` varchar(32) DEFAULT NULL COMMENT '区',
                             `receiver_detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
                             `note` varchar(500) DEFAULT NULL COMMENT '订单备注',
                             `confirm_status` tinyint(4) DEFAULT NULL COMMENT '确认收货状态[0->未确认；1->已确认]',
                             `delete_status` tinyint(4) DEFAULT NULL COMMENT '删除状态【0->未删除；1->已删除】',
                             `use_integration` int(11) DEFAULT NULL COMMENT '下单时使用的积分',
                             `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
                             `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
                             `receive_time` datetime DEFAULT NULL COMMENT '确认收货时间',
                             `comment_time` datetime DEFAULT NULL COMMENT '评价时间',
                             `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `order_sn` (`order_sn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单';

DROP TABLE IF EXISTS `myh_order_item`;
CREATE TABLE `myh_order_item` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `order_id` bigint(20) DEFAULT NULL COMMENT 'order_id',
                                  `order_sn` char(64) DEFAULT NULL COMMENT 'order_sn',
                                  `spu_id` bigint(20) DEFAULT NULL COMMENT 'spu_id',
                                  `spu_name` varchar(255) DEFAULT NULL COMMENT 'spu_name',
                                  `spu_pic` varchar(500) DEFAULT NULL COMMENT 'spu_pic',
                                  `spu_brand` varchar(200) DEFAULT NULL COMMENT '品牌',
                                  `category_id` bigint(20) DEFAULT NULL COMMENT '商品分类id',
                                  `sku_id` bigint(20) DEFAULT NULL COMMENT '商品sku编号',
                                  `sku_name` varchar(255) DEFAULT NULL COMMENT '商品sku名字',
                                  `sku_pic` varchar(500) DEFAULT NULL COMMENT '商品sku图片',
                                  `sku_price` decimal(18,4) DEFAULT NULL COMMENT '商品sku价格',
                                  `sku_quantity` int(11) DEFAULT NULL COMMENT '商品购买的数量',
                                  `sku_attrs_vals` varchar(500) DEFAULT NULL COMMENT '商品销售属性组合（JSON）',
                                  `promotion_amount` decimal(18,4) DEFAULT NULL COMMENT '商品促销分解金额',
                                  `coupon_amount` decimal(18,4) DEFAULT NULL COMMENT '优惠券优惠分解金额',
                                  `integration_amount` decimal(18,4) DEFAULT NULL COMMENT '积分优惠分解金额',
                                  `real_amount` decimal(18,4) DEFAULT NULL COMMENT '该商品经过优惠后的分解金额',
                                  `gift_integration` int(11) DEFAULT NULL COMMENT '赠送积分',
                                  `gift_growth` int(11) DEFAULT NULL COMMENT '赠送成长值',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项信息';
