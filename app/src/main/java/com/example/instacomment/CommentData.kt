package com.example.instacomment

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

class CommentData {
    var userId: String? = null
    var userPicture: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var commentId: Int? = null
    var parentId: Int? = null
    private var fieldComment: SpannableStringBuilder? = null
    var commentCount: String? = null
    var commentReplyData: List<CommentReplyData>? = null
    var currentCommentNumber = 3

    var createdTime: String? = null
        set(createdTime) {
            val convertedTimeStr: String? =
                MiscUtils.getSharedInstance()?.getEpocDifference(createdTime)
            field = if (!TextUtils.isEmpty(convertedTimeStr)) {
                convertedTimeStr
            } else {
                ""
            }
        }

    fun getFieldComment(): SpannableStringBuilder? {
        return fieldComment
    }

    fun setFieldComment(fieldComment: String, userInfos: List<CommetUserInfo>?) {
        if (TextUtils.isEmpty(fieldComment)) {
            return
        }
        val ssb = SpannableStringBuilder("")
        addClickableText(ssb, ssb.length, "$firstName $lastName", userId)
        ssb.append(" ")
        processCommentForUserTags(ssb, fieldComment, userInfos)
        this.fieldComment = ssb
    }

    private fun addClickableText(
        ssb: SpannableStringBuilder,
        startPos: Int,
        clickableText: String,
        tagId: String?
    ) {
        ssb.append(clickableText)
        ssb.setSpan(tagId?.let { Tag.UserTag(it) }, startPos, ssb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.setSpan(
            ForegroundColorSpan(Color.BLACK),
            startPos,
            ssb.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.setSpan(
            StyleSpan(Typeface.BOLD),
            startPos,
            ssb.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun processCommentForUserTags(
        ssb: SpannableStringBuilder, fieldComment: String,
        userInfos: List<CommetUserInfo>?
    ) {
        if (!TextUtils.isEmpty(fieldComment)) {
            val items = fieldComment.split("\\[\\[~".toRegex()).toTypedArray()
            for (i in items.indices) {
                val splitStr = items[i].replace("]]".toRegex(), "")
                val arr = splitStr.split(" ".toRegex(), 2).toTypedArray()
                try {
                    arr[0].toDouble()
                    val userName = getUserName(arr[0], userInfos)
                    if (TextUtils.isEmpty(userName)) {
                        ssb.append(arr[0])
                    } else {
                        if (userName != null) {
                            addClickableText(ssb, ssb.length, userName, arr[0])
                        }
                    }
                } catch (e: Exception) {
                    ssb.append(arr[0] + " ")
                }
                if (arr.size > 1) {
                    ssb.append(arr[1])
                }
            }
        }
    }

    private fun getUserName(userId: String, userInfos: List<CommetUserInfo>?): String? {
        if (TextUtils.isEmpty(userId)) {
            return ""
        }
        if (null == userInfos || userInfos.isEmpty()) {
            return ""
        }
        for (userInfo in userInfos) {
            if (userId == java.lang.String.valueOf(userInfo.uid)) {
                return userInfo.name
            }
        }
        return ""
    }
}