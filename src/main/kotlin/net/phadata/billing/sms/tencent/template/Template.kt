package net.phadata.billing.sms.tencent.template


/**
 * 模版
 * @author linx
 * @since 2022-08-02 16:33
 *
 */
class Template {

    /**
     * 开具发票通知
     */
    var invoiceNotice: InvoiceNotice? = null


    override fun toString(): String {
        return "Template(invoiceNotice=$invoiceNotice)"
    }


}