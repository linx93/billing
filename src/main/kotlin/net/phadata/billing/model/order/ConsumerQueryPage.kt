package net.phadata.billing.model.order

import io.swagger.annotations.ApiModelProperty
import net.phadata.billing.model.base.Page


/**
 * 客户查询条件
 * @author linx
 * @since 2022-06-13 23:53
 *
 */
class ConsumerQueryPage : ConsumerQuery() {
    @ApiModelProperty(value = "分页参数", required = true)
    lateinit var page: Page
}