package net.phadata.billing.model.base

import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import io.swagger.annotations.ApiModelProperty

/**
 * 分页数据
 *
 * @author linx
 * @since 2022-03-22 13:41
 */
class PageInfo<T : Any> {
    @ApiModelProperty(value = "页码")
    var current: Int = 0

    @ApiModelProperty(value = "页数")
    var size: Int = 0

    @ApiModelProperty(value = "总数")
    var total: Int = 0

    @ApiModelProperty(value = "数据")
    var records: List<T>? = null
    fun buildPageInfo(page: Page<T>): PageInfo<T> {
        return PageInfo<T>().apply {
            current = page.current.toInt()
            size = page.size.toInt()
            total = page.total.toInt()
            records = page.records
        }
    }
}