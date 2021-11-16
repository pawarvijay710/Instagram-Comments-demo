package com.example.instacomment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_comment.*


class CommentFragment : Fragment(), View.OnClickListener {
    lateinit var userSearchAdapter: UserSearchAdapter
    lateinit var commentAdapter: CommentAdapter

    private var learningCommentList: MutableList<CommentData> = ArrayList()
    private var userInfos: MutableList<CommetUserInfo> = ArrayList()
    private var userSearchResults: MutableList<UserData> = ArrayList()

    private var ssb: StringBuilder = StringBuilder()
    private var taggedUserMap: Map<String, String>? = null

    var isLastItem = false
    private var pageNumber = 0
    private val totalItemCount = 12
    private val currentItemCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val commentUser1 = CommetUserInfo()
        commentUser1.name = "Alex Storm"
        commentUser1.uid = 1

        val commentUser2 = CommetUserInfo()
        commentUser2.name = "Tony Stark"
        commentUser2.uid = 2

        val commentUser3 = CommetUserInfo()
        commentUser2.name = "John Wick"
        commentUser2.uid = 3

        userInfos.add(commentUser1)
        userInfos.add(commentUser2)
        userInfos.add(commentUser3)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setUpAdapter()

        if (!learningCommentList.isEmpty()) {
            setData(learningCommentList)
        } else {
            callApiToGetComments()
        }
    }

    private fun setListeners() {
        edt_comment.doAfterTextChanged {
            val commentStr = it.toString()

            if (TextUtils.isEmpty(commentStr)) {
                if (!userInfos.isEmpty()) {
                    userInfos.clear()
                }

                if (ssb.toString().isNotEmpty()) {
                    ssb = StringBuilder()
                }

                if (null != taggedUserMap && taggedUserMap!!.isEmpty()) {
                    taggedUserMap = HashMap()
                }
            }

            if (commentStr.contains("@")) {
                val searchSplit = commentStr.split("@",ignoreCase = true, limit = 2)
                val searchStr = searchSplit [1]

                if (!TextUtils.isEmpty(searchStr) && searchStr.length > 2) {
                    recycler_user_search.setVisibility(View.VISIBLE);
                    callApiToSearchUser(searchStr)
                }
            } else {
                recycler_user_search.setVisibility(View.GONE)
            }
        }
    }

    override fun onClick(v: View?) {
       when(v?.id){
           R.id.text_view_more_comments ->{
               if (null != v!!.tag && v.tag is CommentData) {
                   val commentData = v.tag as CommentData
                   commentData.currentCommentNumber = commentData.currentCommentNumber + 3
                   commentAdapter.notifyDataSetChanged()
               }
           }
       }
    }

    private fun setUpAdapter() {
        userSearchAdapter = UserSearchAdapter(requireActivity(), this)
        recycler_user_search.setAdapter(userSearchAdapter)

        commentAdapter = CommentAdapter(requireActivity() as MainActivity, this)
        recycler_comment.setAdapter(commentAdapter)
        var layoutManager = recycler_comment.layoutManager as LinearLayoutManager

        recycler_comment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == commentAdapter.getItemCount() - 1 && newState == 0) {
                    if (!isLastItem) {
                        if (currentItemCount >= totalItemCount) {
                            isLastItem = true
                            return
                        }
                        pageNumber++
                        comments_pagination_loader.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            comments_pagination_loader.visibility = View.GONE
                            callApiToGetComments()
                        }, 5000)
                    }
                }
            }
        })
    }

    private fun callApiToGetComments(){
        if(pageNumber>0){
            val commentData11 = CommentData()
            commentData11.firstName = "Alex"
            commentData11.lastName = "Storm"
            commentData11.userId = 1.toString()
            commentData11.commentId = 1101
            commentData11.setFieldComment("Hi THhis is very nice picture.. Hi THhis is very nice picture.. Hi THhis is very nice picture.. ", null)

            val commentData12 = CommentData()
            commentData12.firstName = "Alex"
            commentData12.lastName = "Storm"
            commentData12.userId = 1.toString()
            commentData12.commentId = 501
            commentData12.setFieldComment("Hi THhis is very nice picture", null)

            learningCommentList.add(commentData11)
            learningCommentList.add(commentData12)
            setData(learningCommentList)
            return
        }

        val commentData1 = CommentData()
        commentData1.firstName = "Alex"
        commentData1.lastName = "Storm"
        commentData1.userId = 1.toString()
        commentData1.commentId = 101
        commentData1.setFieldComment("Hi THhis is very nice picture", null)
        val commentReplyData1 = CommentReplyData()
        commentReplyData1.commentId = 102
        commentReplyData1.parentId = 101
        commentReplyData1.userId = 2.toString()
        commentReplyData1.firstName = "Tony"
        commentReplyData1.lastName = "Stark"
        commentReplyData1.setFieldComment("Thanks!!", null)
        val commentReplyData2 = CommentReplyData()
        commentReplyData2.commentId = 103
        commentReplyData2.parentId = 101
        commentReplyData2.userId = 1.toString()
        commentReplyData2.firstName = "Alex"
        commentReplyData2.lastName = "Storm"
        commentReplyData2.setFieldComment("Can you teach me?", null)
        val commentReplayData1 = ArrayList<CommentReplyData>()
        commentReplayData1.add(commentReplyData1)
        commentReplayData1.add(commentReplyData2)
        commentData1.commentReplyData = commentReplayData1

        val commentData2 = CommentData()
        commentData2.firstName = "Tonny"
        commentData2.lastName = "Stark"
        commentData2.userId = 2.toString()
        commentData2.commentId = 201
        commentData2.setFieldComment("Thank you very much all!!", null)

        val commentData3 = CommentData()
        commentData3.firstName = "John"
        commentData3.lastName = "Wick"
        commentData3.userId = 3.toString()
        commentData3.commentId = 301
        commentData3.setFieldComment("Awesome", null)
        val commentReplyData3 = CommentReplyData()
        commentReplyData3.commentId = 302
        commentReplyData3.parentId = 301
        commentReplyData3.userId = 2.toString()
        commentReplyData3.firstName = "Tony"
        commentReplyData3.lastName = "Stark"
        commentReplyData3.setFieldComment("Baba Yaga", null)
        val commentReplyData4 = CommentReplyData()
        commentReplyData4.commentId = 303
        commentReplyData4.parentId = 301
        commentReplyData4.userId = 2.toString()
        commentReplyData4.firstName = "Tony"
        commentReplyData4.lastName = "Stark"
        commentReplyData4.setFieldComment("Man of focus, commitment and sheer will", null)
        val commentReplyData5 = CommentReplyData()
        commentReplyData5.commentId = 304
        commentReplyData5.parentId = 301
        commentReplyData5.userId = 3.toString()
        commentReplyData5.firstName = "John"
        commentReplyData5.lastName = "Wick"
        commentReplyData5.setFieldComment("I got pencil", null)
        val commentReplyData6 = CommentReplyData()
        commentReplyData6.commentId = 305
        commentReplyData6.parentId = 301
        commentReplyData6.userId = 2.toString()
        commentReplyData6.firstName = "Tony"
        commentReplyData6.lastName = "Stark"
        commentReplyData6.setFieldComment("Just kidding bro!!", null)
        val commentReplayData2 = ArrayList<CommentReplyData>()
        commentReplayData2.add(commentReplyData3)
        commentReplayData2.add(commentReplyData4)
        commentReplayData2.add(commentReplyData5)
        commentReplayData2.add(commentReplyData6)
        commentData3.commentReplyData = commentReplayData2

        val commentData4 = CommentData()
        commentData4.firstName = "Alex"
        commentData4.lastName = "Storm"
        commentData4.userId = 1.toString()
        commentData4.commentId = 401
        commentData4.setFieldComment("Hi THhis is very nice picture", null)

        val commentData5 = CommentData()
        commentData5.firstName = "Alex"
        commentData5.lastName = "Storm"
        commentData5.userId = 1.toString()
        commentData5.commentId = 501
        commentData5.setFieldComment("Hi THhis is very nice picture", null)

        val commentData6 = CommentData()
        commentData6.firstName = "Alex"
        commentData6.lastName = "Storm"
        commentData6.userId = 1.toString()
        commentData6.commentId = 601
        commentData6.setFieldComment("Hi THhis is very nice picture.. Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. ", null)

        val commentData7 = CommentData()
        commentData7.firstName = "Alex"
        commentData7.lastName = "Storm"
        commentData7.userId = 1.toString()
        commentData7.commentId = 701
        commentData7.setFieldComment("Hi THhis is very nice picture", null)

        val commentData8 = CommentData()
        commentData8.firstName = "Alex"
        commentData8.lastName = "Storm"
        commentData8.userId = 1.toString()
        commentData8.commentId = 801
        commentData8.setFieldComment("Hi THhis is very nice picture", null)

        val commentData9 = CommentData()
        commentData9.firstName = "Alex"
        commentData9.lastName = "Storm"
        commentData9.userId = 1.toString()
        commentData9.commentId = 901
        commentData9.setFieldComment("Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. Hi THhis is very nice picture!!. ", null)

        val commentData10 = CommentData()
        commentData10.firstName = "Alex"
        commentData10.lastName = "Storm"
        commentData10.userId = 1.toString()
        commentData10.commentId = 1001
        commentData10.setFieldComment("Hi THhis is very nice picture", null)


        learningCommentList.add(commentData1)
        learningCommentList.add(commentData2)
        learningCommentList.add(commentData3)
        learningCommentList.add(commentData4)
        learningCommentList.add(commentData5)
        learningCommentList.add(commentData6)
        learningCommentList.add(commentData7)
        learningCommentList.add(commentData8)
        learningCommentList.add(commentData9)
        learningCommentList.add(commentData10)

        setData(learningCommentList)

    }

    private fun callApiToSearchUser(searchStr: String) {

    }

    private fun setData(learningCommentList: List<CommentData>?) {
        if (null != learningCommentList && !learningCommentList.isEmpty()) {
            commentAdapter.setData(learningCommentList, null)
        }

        commentAdapter.notifyDataSetChanged()
    }
}
