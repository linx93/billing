package net.phadata.billing.controller

import com.alibaba.excel.EasyExcel
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.phadata.billing.model.excel.DownloadConsumer
import net.phadata.billing.model.excel.DownloadOrder
import net.phadata.billing.model.consumer.ConsumerQuery
import net.phadata.billing.model.order.OrderQuery
import net.phadata.billing.service.OrderRecordsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URLEncoder
import java.time.Instant
import javax.servlet.http.HttpServletResponse


/**
 * excel处理
 * @author linx
 * @since 2022-06-14 00:31
 */
@Api(tags = ["excel处理"], description = "excel处理")
@RestController
@RequestMapping("/api/v1/excel")
class ExcelController {
    @Autowired
    lateinit var orderRecordsService: OrderRecordsService

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * 1. 创建excel对应的实体对象
     * 2. 设置返回的 参数
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream
     */
    @ApiOperation(value = "导出订单excel", notes = "导出订单excel")
    @PostMapping("download-order")
    fun downloadOrder(@RequestBody orderQuery: OrderQuery, response: HttpServletResponse) {
        val data: List<DownloadOrder> = orderRecordsService.listByOrderQuery(orderQuery)
        setHttpServletResponse("order_", response)
        EasyExcel.write(response.outputStream, DownloadOrder::class.java).sheet("订单").doWrite(data)
    }

    @ApiOperation(value = "导出客户excel", notes = "导出客户excel")
    @GetMapping("download-consumer")
    fun downloadConsumer(consumerQuery: ConsumerQuery, response: HttpServletResponse) {
        val data: List<DownloadConsumer> = orderRecordsService.listByConsumerQuery(consumerQuery)
        setHttpServletResponse("consumer_", response)
        EasyExcel.write(response.outputStream, DownloadConsumer::class.java).sheet("客户").doWrite(data)
    }

    private fun setHttpServletResponse(name: String, response: HttpServletResponse) {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        response.characterEncoding = "utf-8"
        // 这里URLEncoder.encode可以防止中文乱码 当然和easy excel没有关系
        val fileName: String = URLEncoder.encode(name + Instant.now().epochSecond, "UTF-8").replace("\\+", "%20")
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''$fileName.xlsx")
    }
}