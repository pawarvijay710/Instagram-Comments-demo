package com.example.instacomment

import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_comment.view.*


class CommentAdapter(private val mainActivity: MainActivity, private val clickListener: View.OnClickListener) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    var learningCommentList: List<CommentData> = ArrayList()
    var learningCommentDynamicList: List<CommentDynamicData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view: View = LayoutInflater.from(mainActivity)
            .inflate(R.layout.list_item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        if (!learningCommentList.isEmpty() && position < learningCommentList.size) {
            var commentData = learningCommentList.get(position);

            if (!TextUtils.isEmpty(commentData.userPicture)) {
                //miscUtils.loadImage(mainActivity, holder.commentUserImage, commentData.getUserPicture(), R.drawable.placeholder_user_image);
            }

            holder.commentTime.text = if (TextUtils.isEmpty(commentData.createdTime))
                ""
            else
                commentData.createdTime


            holder.commentText.setMovementMethod(LinkMovementMethod())
            holder.commentText.setText(commentData.getFieldComment())

            holder.recyclerCommentReplies.setTag(commentData);
            holder.commentReply.setTag(commentData);
            holder.viewReplyText.setTag(commentData);
            holder.commentReply.setTag(R.string.parent_comment_position_key, position)

            if (null != commentData.commentReplyData && !commentData.commentReplyData!!.isEmpty()) {
                holder.recyclerCommentReplies.setVisibility(View.VISIBLE)
                if (commentData.commentReplyData!!.size <= commentData.currentCommentNumber) {
                    holder.viewReplyText.setVisibility(View.GONE)
                    holder.viewReplyText.setOnClickListener(null)
                } else {
                    holder.viewReplyText.setOnClickListener(clickListener)
                    holder.viewReplyText.setVisibility(View.VISIBLE)
                    val loadCommentCount: Int =
                        commentData.commentReplyData!!.size - commentData.currentCommentNumber
                    val replyText: String =
                        mainActivity.getString(R.string.string_comment_view_replies) + "(" + loadCommentCount + ")"
                    holder.viewReplyText.setText(replyText)
                }
                if (null == holder.adapter) {
                    holder.adapter = CommentRepliesAdapter (mainActivity, clickListener)
                }

                if (null == holder.layoutManager) {
                    holder.layoutManager = LinearLayoutManager (mainActivity)
                }
                holder.recyclerCommentReplies.setLayoutManager(holder.layoutManager)
                holder.recyclerCommentReplies.setAdapter(holder.adapter)
                holder.adapter!!.setData(
                    commentData.commentReplyData,
                    null,
                    commentData.currentCommentNumber
                )
            } else {
                holder.recyclerCommentReplies.setVisibility(View.GONE)
            }

            if (!learningCommentDynamicList.isEmpty() && position < learningCommentDynamicList.size) {
                val commentDynamicData = learningCommentDynamicList[position]
                    ?: return
                holder.commentLikeUnlikeImage.tag = commentDynamicData
                if (null != holder.adapter && null != commentDynamicData.commentReplyDynamicDataList && !commentDynamicData.commentReplyDynamicDataList!!.isEmpty()) {
                    holder.adapter!!.setData(
                        null,
                        commentDynamicData.commentReplyDynamicDataList,
                        -1
                    )
                }
               // holder.commentLikeUnlikeImage.setImageResource(if (null != commentDynamicData.favoriteSatus && commentDynamicData.favoriteSatus) R.drawable.ic_like_black else R.drawable.ic_unlike_black)
                if (null != commentDynamicData.favoriteCount && commentDynamicData.favoriteCount != 0) {
                    val countStr: String =
                        commentDynamicData.favoriteCount.toString() + " " + mainActivity.getString(R.string.string_comment_like)

                    holder.commentLikeCount.text =
                        if (TextUtils.isEmpty(java.lang.String.valueOf(commentDynamicData.favoriteCount))) "" else countStr
                    holder.commentLikeCount.visibility = View.VISIBLE
                } else {
                    holder.commentLikeCount.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (null != learningCommentList && !learningCommentList!!.isEmpty()) {
            return learningCommentList!!.size
        }
        return 0
    }

    fun setData(
        learningCommentList: List<CommentData>,
        learningCommentDynamicList: List<CommentDynamicData>?
    ) {
        if (!learningCommentList.isEmpty()) {
            this.learningCommentList = learningCommentList
        }
        /*if (!learningCommentDynamicList.isEmpty()) {
            this.learningCommentDynamicList = learningCommentDynamicList
        }*/
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentUserImage = itemView.image_user_comment
        val commentText = itemView.text_user_comment
        val commentLikeUnlikeImage = itemView.image_like_unlike_comment
        val recyclerCommentReplies = itemView.recycler_comment_replies
        val commentTime = itemView.text_comment_time
        val commentLikeCount = itemView.text_comment_like
        val commentReply = itemView.text_comment_reply
        val viewReplyText = itemView.text_view_more_comments
        var adapter: CommentRepliesAdapter? = null
        var layoutManager: LinearLayoutManager? = null

        init {
            recyclerCommentReplies.setVisibility(View.GONE)
            commentReply.setVisibility(View.GONE)
        }
    }
}

