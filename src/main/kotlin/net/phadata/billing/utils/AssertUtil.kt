package net.phadata.billing.utils

import net.phadata.billing.exception.ServiceException
import org.apache.commons.lang3.StringUtils


/**
 * 断言封装
 * @author linx
 * @since 2022-06-13 23:02
 *
 */
object AssertUtil {
    fun notNull(any: Any?, msg: String) {
        any ?: throw ServiceException(msg)
    }

    fun isNull(any: Any?, msg: String) {
        any ?: throw ServiceException(msg)
    }

    fun isTrue(boolean: Boolean, msg: String) {
        if (!boolean) {
            throw ServiceException(msg)
        }

    }

    fun notBlank(str: String?, msg: String) {
        if (StringUtils.isBlank(str)) {
            throw ServiceException(msg)
        }
    }

    fun notEmpty(array: Array<Any?>?, msg: String) {
        if (array == null || array.isEmpty()) {
            throw ServiceException(msg)
        }
    }

    fun notEmpty(collection: Collection<Any?>?, msg: String) {
        if (collection == null || collection.isEmpty()) {
            throw ServiceException(msg)
        }
    }

    fun isEmpty(collection: Collection<Any?>?, msg: String) {
        if (collection != null && !collection.isEmpty()) {
            throw ServiceException(msg)
        }
    }
}