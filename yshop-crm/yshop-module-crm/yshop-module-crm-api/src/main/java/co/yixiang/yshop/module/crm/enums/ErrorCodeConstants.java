package co.yixiang.yshop.module.crm.enums;

import co.yixiang.yshop.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode CUSTOMER_NOT_EXISTS = new ErrorCode(200000, "客户不存在");
    ErrorCode CUSTOMER_CONTACTS_NOT_EXISTS = new ErrorCode(200001, "联系人不存在");
    ErrorCode RECORD_NOT_EXISTS = new ErrorCode(200002, "跟进记录不存在");
    ErrorCode BUSINESS_NOT_EXISTS = new ErrorCode(200003, "商机不存在");
    ErrorCode CLUES_NOT_EXISTS = new ErrorCode(200004, "线索不存在");
    ErrorCode CONTRACT_NOT_EXISTS = new ErrorCode(200005, "合同不存在");
    ErrorCode FLOW_NOT_EXISTS = new ErrorCode(200006, "审批流程不存在");
    ErrorCode CONTRACT_RECEIVABLES_NOT_EXISTS = new ErrorCode(200007, "回款不存在");
    ErrorCode INVOICE_NOT_EXISTS = new ErrorCode(200008, "发票不存在");

    // 助贷CRM扩展错误码
    ErrorCode DATA_SOURCE_NOT_EXISTS = new ErrorCode(200100, "数据源不存在");
    ErrorCode DATA_SOURCE_CODE_EXISTS = new ErrorCode(200101, "数据源编码已存在");
    ErrorCode LOAN_PRODUCT_NOT_EXISTS = new ErrorCode(200102, "贷款产品不存在");
    ErrorCode LOAN_RECORD_NOT_EXISTS = new ErrorCode(200103, "放款记录不存在");
    ErrorCode ASSIGN_RULE_NOT_EXISTS = new ErrorCode(200104, "分配规则不存在");
    ErrorCode CUSTOMER_STATUS_CONFIG_NOT_EXISTS = new ErrorCode(200105, "客户状态配置不存在");
    ErrorCode CUSTOMER_STATUS_CODE_EXISTS = new ErrorCode(200106, "状态编码已存在");
}
