-- ============================================
-- 助贷CRM 扩展表结构
-- 基于 yshop-crm 现有功能的扩展
-- ============================================

-- ----------------------------
-- 1. 数据源表 (用于ROI分析)
-- ----------------------------
DROP TABLE IF EXISTS `crm_data_source`;
CREATE TABLE `crm_data_source` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `source_code` VARCHAR(50) NOT NULL COMMENT '数据源编码 (如: A+G11.9, ssfq12.3)',
    `source_name` VARCHAR(100) NOT NULL COMMENT '数据源名称',
    `channel` VARCHAR(50) COMMENT '所属渠道',
    `acquire_cost` DECIMAL(10,2) DEFAULT 0.00 COMMENT '获客成本',
    `acquire_time` DATE COMMENT '获客时间',
    `region` VARCHAR(50) COMMENT '地区',
    `remark` VARCHAR(500) COMMENT '备注',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1=启用 0=禁用',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_source_code` (`source_code`),
    KEY `idx_channel` (`channel`),
    KEY `idx_region` (`region`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据源表-ROI分析';


-- ----------------------------
-- 2. 客户状态配置表 (12种状态)
-- ----------------------------
DROP TABLE IF EXISTS `crm_customer_status_config`;
CREATE TABLE `crm_customer_status_config` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `status_code` VARCHAR(50) NOT NULL COMMENT '状态编码',
    `status_name` VARCHAR(50) NOT NULL COMMENT '状态名称',
    `status_type` VARCHAR(20) NOT NULL DEFAULT 'normal' COMMENT '状态类型: normal=普通状态 special=特殊状态',
    `color_type` VARCHAR(20) COMMENT '颜色类型: primary/success/warning/danger/info',
    `css_class` VARCHAR(50) COMMENT 'CSS样式类',
    `sort_order` INT DEFAULT 0 COMMENT '排序号',
    `is_enabled` TINYINT DEFAULT 1 COMMENT '是否启用: 1=启用 0=禁用',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_status_code` (`status_code`),
    KEY `idx_status_type` (`status_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户状态配置表';

-- 插入预置状态
INSERT INTO `crm_customer_status_config` (`status_code`, `status_name`, `status_type`, `color_type`, `sort_order`) VALUES
('new', '新线索', 'normal', 'primary', 1),
('following', '跟进中', 'normal', 'warning', 2),
('not_connected', '未接通', 'special', 'info', 3),
('invalid', '无效客户', 'special', 'danger', 4),
('intention_high', '意向强烈', 'special', 'success', 5),
('intention_normal', '意向一般', 'special', 'warning', 6),
('intention_low', '意向低', 'special', 'info', 7),
('wechat_added', '已加微信', 'normal', 'primary', 8),
('invited', '邀约成功', 'normal', 'warning', 9),
('visited', '已上门', 'normal', 'primary', 10),
('plan_confirmed', '方案确认', 'normal', 'warning', 11),
('loan_processing', '放款中', 'normal', 'info', 12),
('deal', '已成交', 'normal', 'success', 13),
('lost', '流失客户', 'special', 'danger', 14);


-- ----------------------------
-- 3. 客户状态变更日志表
-- ----------------------------
DROP TABLE IF EXISTS `crm_customer_status_log`;
CREATE TABLE `crm_customer_status_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `old_status` VARCHAR(50) COMMENT '原状态',
    `new_status` VARCHAR(50) NOT NULL COMMENT '新状态',
    `operator_id` BIGINT COMMENT '操作人ID',
    `operator_name` VARCHAR(50) COMMENT '操作人姓名',
    `change_reason` VARCHAR(500) COMMENT '变更原因',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_operator_id` (`operator_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户状态变更日志表';


-- ----------------------------
-- 4. 线索分配规则表
-- ----------------------------
DROP TABLE IF EXISTS `crm_assign_rule`;
CREATE TABLE `crm_assign_rule` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `rule_name` VARCHAR(100) NOT NULL COMMENT '规则名称',
    `condition_type` VARCHAR(20) NOT NULL COMMENT '条件类型: city=按城市 dataSource=按数据源 all=全部',
    `condition_value` VARCHAR(200) COMMENT '条件值 (城市编码/数据源ID)',
    `assign_type` VARCHAR(20) NOT NULL COMMENT '分配类型: staff=指定业务员 balance=负载均衡 smart=智能评分',
    `target_staff_id` BIGINT COMMENT '目标业务员ID (assign_type=staff时使用)',
    `weight_conversion` DECIMAL(3,2) DEFAULT 0.40 COMMENT '转化率权重',
    `weight_workload` DECIMAL(3,2) DEFAULT 0.30 COMMENT '工作量权重',
    `weight_region` DECIMAL(3,2) DEFAULT 0.20 COMMENT '地域权重',
    `weight_expertise` DECIMAL(3,2) DEFAULT 0.10 COMMENT '专业度权重',
    `priority` INT DEFAULT 0 COMMENT '优先级 (数字越大优先级越高)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1=启用 0=禁用',
    `remark` VARCHAR(500) COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_condition_type` (`condition_type`),
    KEY `idx_priority` (`priority`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='线索分配规则表';


-- ----------------------------
-- 5. 贷款产品表
-- ----------------------------
DROP TABLE IF EXISTS `crm_loan_product`;
CREATE TABLE `crm_loan_product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `product_name` VARCHAR(100) NOT NULL COMMENT '产品名称',
    `product_code` VARCHAR(50) COMMENT '产品编码',
    `city_code` VARCHAR(20) NOT NULL COMMENT '适用城市编码',
    `city_name` VARCHAR(50) COMMENT '适用城市名称',
    `min_amount` DECIMAL(12,2) COMMENT '最低额度',
    `max_amount` DECIMAL(12,2) COMMENT '最高额度',
    `min_rate` DECIMAL(5,4) COMMENT '最低利率 (如: 0.0450 代表4.5%)',
    `max_rate` DECIMAL(5,4) COMMENT '最高利率',
    `period_min` INT COMMENT '最短期数 (月)',
    `period_max` INT COMMENT '最长期数 (月)',
    `repayment_methods` VARCHAR(200) COMMENT '还款方式 (JSON数组: ["等额本息","先息后本"])',
    `requirements` JSON COMMENT '客户资质要求 (JSON格式)',
    `bank_name` VARCHAR(100) COMMENT '放款银行',
    `product_features` TEXT COMMENT '产品特点',
    `required_documents` TEXT COMMENT '所需材料',
    `processing_time` INT COMMENT '审批时效 (小时)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1=上架 0=下架',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_product_code` (`product_code`),
    KEY `idx_city_code` (`city_code`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贷款产品表';

-- 产品资质要求JSON格式示例:
-- {
--   "min_income": 5000,              -- 最低月收入 (元)
--   "min_credit_score": 600,         -- 最低信用分
--   "required_insurance": false,     -- 是否需要社保
--   "required_house": false,         -- 是否需要房产
--   "required_car": false,           -- 是否需要车产
--   "age_min": 18,                   -- 最小年龄
--   "age_max": 60,                   -- 最大年龄
--   "required_local_house": false,   -- 是否需要本地房产
--   "work_stability_months": 6       -- 工作稳定性 (月)
-- }


-- ----------------------------
-- 6. 放款记录表
-- ----------------------------
DROP TABLE IF EXISTS `crm_loan_record`;
CREATE TABLE `crm_loan_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `customer_id` BIGINT NOT NULL COMMENT '客户ID',
    `customer_name` VARCHAR(50) COMMENT '客户姓名',
    `customer_phone` VARCHAR(20) COMMENT '客户手机号',
    `product_id` BIGINT NOT NULL COMMENT '产品ID',
    `product_name` VARCHAR(100) COMMENT '产品名称',
    `loan_amount` DECIMAL(12,2) NOT NULL COMMENT '放款额度',
    `loan_rate` DECIMAL(5,4) NOT NULL COMMENT '利率',
    `loan_period` INT NOT NULL COMMENT '还款期数 (月)',
    `repayment_method` VARCHAR(50) COMMENT '还款方式',
    `bank_name` VARCHAR(100) COMMENT '放款银行',
    `disbursement_date` DATE COMMENT '放款日期',
    `commission_amount` DECIMAL(12,2) COMMENT '中介服务费',
    `commission_paid` TINYINT DEFAULT 0 COMMENT '服务费是否已支付: 1=是 0=否',
    `channel_staff_id` BIGINT COMMENT '渠道专员ID',
    `channel_staff_name` VARCHAR(50) COMMENT '渠道专员姓名',
    `contract_no` VARCHAR(50) COMMENT '合同编号',
    `remark` VARCHAR(500) COMMENT '备注',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_customer_id` (`customer_id`),
    KEY `idx_product_id` (`product_id`),
    KEY `idx_channel_staff` (`channel_staff_id`),
    KEY `idx_disbursement_date` (`disbursement_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='放款记录表';


-- ----------------------------
-- 7. 为现有客户表添加数据源字段
-- ----------------------------
-- 检查字段是否存在，不存在则添加
SET @column_exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'yshop_crm_customer'
    AND COLUMN_NAME = 'data_source_id'
);

SET @sql_stmt = IF(@column_exists = 0,
    'ALTER TABLE yshop_crm_customer ADD COLUMN `data_source_id` BIGINT COMMENT ''数据源ID'' AFTER `source`',
    'SELECT ''Column data_source_id already exists'' AS result'
);

PREPARE stmt FROM @sql_stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加索引
SET @index_exists = (
    SELECT COUNT(*)
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'yshop_crm_customer'
    AND INDEX_NAME = 'idx_data_source_id'
);

SET @sql_stmt = IF(@index_exists = 0,
    'ALTER TABLE yshop_crm_customer ADD INDEX `idx_data_source_id` (`data_source_id`)',
    'SELECT ''Index idx_data_source_id already exists'' AS result'
);

PREPARE stmt FROM @sql_stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- ----------------------------
-- 8. 为现有客户表添加扩展状态字段
-- ----------------------------
-- 添加扩展状态字段 (使用原有的followStatus，但这里添加一个更详细的字段)
SET @column_exists = (
    SELECT COUNT(*)
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'yshop_crm_customer'
    AND COLUMN_NAME = 'ext_status'
);

SET @sql_stmt = IF(@column_exists = 0,
    'ALTER TABLE yshop_crm_customer ADD COLUMN `ext_status` VARCHAR(50) COMMENT ''扩展状态编码 (关联crm_customer_status_config)'' AFTER `followStatus`',
    'SELECT ''Column ext_status already exists'' AS result'
);

PREPARE stmt FROM @sql_stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;


-- ----------------------------
-- 9. 城市部门关联表 (用于渠道团队管理)
-- ----------------------------
DROP TABLE IF EXISTS `crm_dept_city`;
CREATE TABLE `crm_dept_city` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `dept_id` BIGINT NOT NULL COMMENT '部门ID',
    `city_code` VARCHAR(20) NOT NULL COMMENT '城市编码',
    `city_name` VARCHAR(50) COMMENT '城市名称',
    `team_type` VARCHAR(20) DEFAULT 'sales' COMMENT '团队类型: sales=销售团队 channel=渠道团队',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1=启用 0=禁用',
    `creator` VARCHAR(64) DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` VARCHAR(64) DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` BIT DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_dept_id` (`dept_id`),
    KEY `idx_city_code` (`city_code`),
    KEY `idx_team_type` (`team_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='城市部门关联表';


-- ----------------------------
-- 10. 数据分析统计表 (定期更新)
-- ----------------------------
DROP TABLE IF EXISTS `crm_statistics_summary`;
CREATE TABLE `crm_statistics_summary` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `stat_type` VARCHAR(20) NOT NULL COMMENT '统计类型: daily=日报 weekly=周报 monthly=月报',
    `dimension_type` VARCHAR(20) NOT NULL COMMENT '维度类型: all=全部 city=城市 dataSource=数据源 staff=业务员',
    `dimension_value` VARCHAR(50) COMMENT '维度值',
    `total_customers` INT DEFAULT 0 COMMENT '总客户数',
    `new_customers` INT DEFAULT 0 COMMENT '新增客户数',
    `following_customers` INT DEFAULT 0 COMMENT '跟进中客户数',
    `deal_count` INT DEFAULT 0 COMMENT '成交数量',
    `deal_amount` DECIMAL(12,2) DEFAULT 0.00 COMMENT '成交金额',
    `conversion_rate` DECIMAL(5,2) DEFAULT 0.00 COMMENT '转化率 (%)',
    `acquire_cost` DECIMAL(10,2) DEFAULT 0.00 COMMENT '获客成本',
    `roi` DECIMAL(10,2) DEFAULT 0.00 COMMENT '投资回报率',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_stat` (`stat_date`, `stat_type`, `dimension_type`, `dimension_value`),
    KEY `idx_stat_date` (`stat_date`),
    KEY `idx_dimension_type` (`dimension_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据分析统计表';


-- ----------------------------
-- 初始化数据
-- ----------------------------

-- 插入测试数据源
INSERT INTO `crm_data_source` (`source_code`, `source_name`, `channel`, `acquire_cost`, `region`) VALUES
('A+G11.9', 'A+渠道-11月数据', 'A+', 5000.00, '全国'),
('ssfq12.3', 'SSFQ渠道-12月数据', 'SSFQ', 3000.00, '上海'),
('BD123', 'BD推广-123计划', 'BD', 2000.00, '北京');


-- ============================================
-- 完成
-- ============================================