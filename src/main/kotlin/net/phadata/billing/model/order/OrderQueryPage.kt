package net.phadata.billing.model.order

import io.swagger.annotations.ApiModelProperty
import net.phadata.billing.model.base.Page


/**
 *
 * @author linx
 * @since 2022-07-18 16:06
 *
 */
class OrderQueryPage : OrderQuery() {
    @ApiModelProperty(value = "分页参数", required = true)
    lateinit var page: Page
}