package net.phadata.billing.model.consumer

import io.swagger.annotations.ApiModelProperty
import net.phadata.billing.model.base.Page


/**
 * 分页客户查询条件
 * @author linx
 * @since 2022-07-18 16:04
 *
 */
class ConsumerQueryPage : ConsumerQuery() {
    @ApiModelProperty(value = "分页参数", required = true)
    lateinit var page: Page
}