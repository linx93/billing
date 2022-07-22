package net.phadata.billing.model.statistics

import io.swagger.annotations.ApiModelProperty
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * 折线图
 * @author linx
 * @since 2022-07-22 10:12
 *
 */
class Polyline {
    @ApiModelProperty("横坐标数据")
    var xAxisData: List<String> = listOf()

    @ApiModelProperty("分类的图标数据")
    var legendData: List<String> = listOf()

    @ApiModelProperty("折线渲染的具体数据")
    var series: List<SeriesData> = listOf()


    fun initDayList(days: Int): ArrayList<CommonVO> {
        val list: ArrayList<CommonVO> = ArrayList(days)
        for (i in -(days - 1)..0) {
            val dataStr = LocalDate.now().plusDays(i.toLong()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            list.add(CommonVO().apply {
                this.value = 0.0
                this.date = dataStr
            })
        }
        LocalDate.now().plusDays(-days.toLong())
        return list
    }

    fun initMonthList(months: Int): ArrayList<CommonVO> {
        val list: ArrayList<CommonVO> = ArrayList(months)
        for (i in -(months - 1)..0) {
            val dataStr = LocalDate.now().plusMonths(i.toLong()).format(DateTimeFormatter.ofPattern("yyyy-MM"))
            list.add(CommonVO().apply {
                this.value = 0.0
                this.date = dataStr
            })
        }
        LocalDate.now().plusMonths(-months.toLong())
        return list
    }

    fun getMonthList(months: Int): List<String> {
        val list = mutableListOf<String>()
        for (i in -(months - 1)..0) {
            val dataStr = LocalDate.now().plusMonths(i.toLong()).format(DateTimeFormatter.ofPattern("yyyy-MM"))
            list.add(dataStr)
        }
        LocalDate.now().plusMonths(-months.toLong())
        return list
    }
}