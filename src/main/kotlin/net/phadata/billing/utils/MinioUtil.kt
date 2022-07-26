package net.phadata.billing.utils

import io.minio.*
import io.minio.http.Method
import io.minio.messages.Bucket
import io.minio.messages.DeleteError
import io.minio.messages.DeleteObject
import io.minio.messages.Item
import net.phadata.billing.configuration.MinioClientConfig
import net.phadata.billing.exception.ServiceException
import net.phadata.billing.model.common.ObjectItem
import org.apache.commons.compress.utils.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import kotlin.Result


/**
 * 文件上传工具
 * @author linx
 * @since 2022-07-20 17:05
 *
 */
@Component
class MinioUtil {
    @Autowired
    private lateinit var minioClient: MinioClient

    @Autowired
    private lateinit var minioClientConfig: MinioClientConfig

    /**
     * description: 判断bucket是否存在，不存在则创建
     */
    fun existBucket(name: String?) {
        try {
            val exists: Boolean = minioClient.bucketExists(BucketExistsArgs.builder().bucket(name).build())
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    fun makeBucket(bucketName: String?): Boolean {
        try {
            minioClient.makeBucket(
                MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName 存储bucket名称
     * @return Boolean
     */
    fun removeBucket(bucketName: String?): Boolean {
        try {
            minioClient.removeBucket(
                RemoveBucketArgs.builder()
                    .bucket(bucketName)
                    .build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * description: 上传文件
     *
     * @param multipartFile
     * @return java.lang.String
     */
    fun upload(multipartFile: Array<MultipartFile>): List<String> {
        val names: MutableList<String> = ArrayList(multipartFile.size)
        for (file in multipartFile) {
            var fileName = file.originalFilename
            val split = fileName!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            fileName = if (split.size > 1) {
                split[0] + "_" + System.currentTimeMillis() + "." + split[1]
            } else {
                fileName + System.currentTimeMillis()
            }
            var inputStream: InputStream? = null
            try {
                inputStream = file.inputStream
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(minioClientConfig.bucketName)
                        .`object`(fileName)
                        .stream(inputStream, inputStream.available().toLong(), -1)
                        .contentType(file.contentType)
                        .build()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw ServiceException("文件上传出错")
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            names.add(fileName)
        }
        return names
    }

    /**
     * description: 上传文件
     *
     * @param multipartFile
     * @return url list
     */
    fun uploadMultipartFile(multipartFile: Array<MultipartFile?>): List<String> {
        val names: MutableList<String> = ArrayList(multipartFile.size)
        for (file in multipartFile) {
            var fileName = file!!.originalFilename
            val split = fileName!!.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            fileName = if (split.size > 1) {
                split[0] + "_" + System.currentTimeMillis() + "." + split[1]
            } else {
                fileName + System.currentTimeMillis()
            }
            var `in`: InputStream? = null
            try {
                `in` = file.inputStream
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(minioClientConfig.bucketName)
                        .`object`(fileName)
                        .stream(`in`, `in`.available().toLong(), -1)
                        .contentType(file.contentType)
                        .build()
                )
            } catch (e: Exception) {
                e.printStackTrace()
                throw ServiceException("文件上传出错")
            } finally {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            //处理直接返回完整的url
            val url = "${minioClientConfig.endpoint}${minioClientConfig.bucketName}/${fileName}"
            names.add(url)
        }
        return names
    }

    /**
     * description: 下载文件
     *
     * @param fileName
     * @return: org.springframework.http.ResponseEntity<byte [ ]>
    </byte> */
    fun download(fileName: String?): ResponseEntity<ByteArray?>? {
        var responseEntity: ResponseEntity<ByteArray?>? = null
        var `in`: InputStream? = null
        var out: ByteArrayOutputStream? = null
        try {
            `in` = minioClient.getObject(
                GetObjectArgs.builder().bucket(minioClientConfig.bucketName).`object`(fileName).build()
            )
            out = ByteArrayOutputStream()
            IOUtils.copy(`in`, out)
            //封装返回值
            val bytes = out.toByteArray()
            val headers = HttpHeaders()
            try {
                headers.add("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            headers.contentLength = bytes.size.toLong()
            headers.contentType = MediaType.APPLICATION_OCTET_STREAM
            headers.accessControlExposeHeaders = Arrays.asList("*")
            responseEntity = ResponseEntity(bytes, headers, HttpStatus.OK)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (`in` != null) {
                    try {
                        `in`.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                out?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return responseEntity
    }

    /**
     * 查看文件对象
     *
     * @return 存储bucket内文件对象信息
     */
    fun listObjects(): List<ObjectItem>? {
        val results: MutableIterable<io.minio.Result<Item>>? = minioClient.listObjects(
            ListObjectsArgs.builder().bucket(minioClientConfig.bucketName).build()
        )
        val objectItems: MutableList<ObjectItem> = ArrayList<ObjectItem>()
        try {
            if (results != null) {
                for (result in results) {
                    val item: Item = result.get()
                    val objectItem = ObjectItem()
                    objectItem.objectName = item.objectName()
                    objectItem.size = item.size()
                    objectItems.add(objectItem)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return objectItems
    }

    /**
     * 批量删除文件对象
     *
     * @param objects 对象名称集合
     */
    fun removeObjects(objects: List<String?>): MutableIterable<io.minio.Result<DeleteError>>? {
        val dos: List<DeleteObject> = objects.map { e: String? ->
            DeleteObject(e)
        }
        return minioClient.removeObjects(
            RemoveObjectsArgs.builder().bucket(minioClientConfig.bucketName).objects(dos).build()
        )
    }

    /**
     * 获取文件链接
     *
     * @param fileName
     * @return
     */
    fun readMinioCommonFile(fileName: String?): String? {
        val reqParams: Map<String, String> = HashMap()
        //        reqParams.put("response-content-type", "application/json");
        var url: String? = null
        try {
            url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(minioClientConfig.bucketName)
                    .`object`(fileName)
                    .expiry(2, TimeUnit.HOURS)
                    .extraQueryParams(reqParams)
                    .build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return url
    }

    /**
     * 获取bucket列表
     *
     * @return
     */
    fun listBucket(): List<String> {
        val list: MutableList<String> = ArrayList()
        var bucketList: List<Bucket?>? = null
        try {
            bucketList = minioClient.listBuckets()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (bucketList != null) {
            for (bucket in bucketList) {
                if (bucket != null) {
                    list.add(bucket.name())
                }
            }
        }
        return list
    }

    /**
     * @param in
     * @param fileName
     * @param contentType
     * @return
     */
    fun upload(`in`: InputStream?, fileName: String, contentType: String?): String {
        try {
            minioClient.putObject(
                `in`?.available()?.toLong()?.let {
                    PutObjectArgs.builder()
                        .bucket(minioClientConfig.bucketName)
                        .`object`(fileName)
                        .stream(`in`, it, -1)
                        .contentType(contentType)
                        .build()
                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (`in` != null) {
                try {
                    `in`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return "${minioClientConfig.endpoint}${minioClientConfig.bucketName}/$fileName"
    }

    fun getObject(fileName: String?): InputStream? {
        var `in`: InputStream? = null
        try {
            //  ByteArrayOutputStream out = null;
            `in` = minioClient.getObject(
                GetObjectArgs.builder().bucket(minioClientConfig.bucketName).`object`(fileName).build()
            )
            // out = new ByteArrayOutputStream();
            // IOUtils.copy(in, out);
            //封装返回值
            // byte[] bytes = out.toByteArray();
            return `in`
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}