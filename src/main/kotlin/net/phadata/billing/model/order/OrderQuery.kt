package net.phadata.billing.model.order

import io.swagger.annotations.ApiModelProperty
import net.phadata.billing.model.base.Keyword
import net.phadata.billing.model.base.TimeRange

/**
 * 订单查询条件
 * @author linx
 * @since 2022-06-13 23:53
 *
 */
open class OrderQuery : Keyword() {

    @ApiModelProperty(value = "时间范围条件")
    var timeRange: TimeRange? = null

    @ApiModelProperty(value = "平台code")
    var platformCode: String? = null
}