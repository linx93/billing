package net.phadata.billing.constant


/**
 * 开票状态枚举
 * @author linx
 * @since 2022-07-20 16:28
 *
 */
enum class BillingStatusEnum(val code: Int, val message: String) {
    //开票状态[0：未开票、1：开票中、2：已开票]
    NOT_INVOICED(0, "未开票"),
    INVOICING(1, "开票中"),
    INVOICED(2, "已开票")
}