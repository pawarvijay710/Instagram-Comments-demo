package com.example.instacomment

import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_comment.view.*

class CommentRepliesAdapter(
    private val mainActivity: MainActivity,
    private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<CommentRepliesAdapter.CommentViewHolder?>() {
    private val miscUtils: MiscUtils?
    private var commentReplyList: List<CommentReplyData>? = null
    private var commentReplyDynamicDataList: List<CommentReplyDynamicData>? = null
    private var currentCommentNumber = 3

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view: View =
            LayoutInflater.from(mainActivity).inflate(R.layout.list_item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: CommentViewHolder, position: Int) {
        if (null != commentReplyList && !commentReplyList!!.isEmpty() && position < commentReplyList!!.size) {
            val commentData = commentReplyList!![position]
            if (!TextUtils.isEmpty(commentData.userPicture)) {
               /* miscUtils.loadImage(
                    mainActivity,
                    holder.commentUserImage,
                    commentData.userPicture,
                    R.drawable.placeholder_user_image
                )*/
            }
            holder.commentTime.setText(if (TextUtils.isEmpty(commentData.createdTime)) "" else commentData.createdTime)
            holder.commentText.setMovementMethod(LinkMovementMethod())
            holder.commentText.setText(commentData.getFieldComment())
        }
        if (null != commentReplyDynamicDataList && !commentReplyDynamicDataList!!.isEmpty() && position < commentReplyDynamicDataList!!.size) {
            val commentReplyDynamicData = commentReplyDynamicDataList!![position]
                ?: return
            holder.commentLikeUnlikeImage.setTag(commentReplyDynamicData)
            //holder.commentLikeUnlikeImage.setImageResource(if (null != commentReplyDynamicData.favoriteSatus && commentReplyDynamicData.favoriteSatus) R.drawable.ic_like_black else R.drawable.ic_unlike_black)
            if (null != commentReplyDynamicData.favoriteCount && commentReplyDynamicData.favoriteCount != 0) {
                val countStr =
                    commentReplyDynamicData.favoriteCount.toString() + " " + mainActivity.getString(R.string.string_comment_like)

                holder.commentLikeCount.setText(if (TextUtils.isEmpty(commentReplyDynamicData.favoriteCount.toString())) "" else countStr)
                holder.commentLikeCount.setVisibility(View.VISIBLE)
            } else {
                holder.commentLikeCount.setVisibility(View.GONE)
            }
        }
    }

    fun setData(
        commentReplyList: List<CommentReplyData>?,
        commentReplyDynamicDataList: List<CommentReplyDynamicData>?, currentCommentNumber: Int
    ) {
        if (null != commentReplyList && !commentReplyList.isEmpty()) {
            this.commentReplyList = commentReplyList
        }
        if (null != commentReplyDynamicDataList && !commentReplyDynamicDataList.isEmpty()) {
            this.commentReplyDynamicDataList = commentReplyDynamicDataList
        }
        if (-1 != currentCommentNumber) {
            this.currentCommentNumber = currentCommentNumber
        }
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentUserImage = itemView.image_user_comment
        val commentText = itemView.text_user_comment
        val commentLikeUnlikeImage = itemView.image_like_unlike_comment
        val recyclerCommentReplies = itemView.recycler_comment_replies
        val commentTime = itemView.text_comment_time
        val commentLikeCount = itemView.text_comment_like
        val commentReply = itemView.text_comment_reply
        val viewReplyText = itemView.text_view_more_comments

        init {
            commentLikeCount.setText(mainActivity.getString(R.string.string_comment_like))
            commentReply.setText(mainActivity.getString(R.string.string_comment_reply))
            viewReplyText.setText(mainActivity.getString(R.string.string_comment_view_replies))
            commentLikeUnlikeImage.setOnClickListener(clickListener)
            recyclerCommentReplies.setVisibility(View.GONE)
            commentReply.setVisibility(View.GONE)
        }
    }

    init {
        miscUtils = MiscUtils.getSharedInstance()
    }

    override fun getItemCount(): Int {
        return if (null != commentReplyList && !commentReplyList!!.isEmpty()) {
            if (currentCommentNumber < commentReplyList!!.size) {
                currentCommentNumber
            } else commentReplyList!!.size
        } else 0
    }
}