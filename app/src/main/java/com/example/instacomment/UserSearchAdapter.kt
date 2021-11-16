package com.example.instacomment

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class UserSearchAdapter(
    private val mainActivity: FragmentActivity, private val clickListener: View.OnClickListener
) : RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {

    private var userSearchResult: List<UserData>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View =
            LayoutInflater.from(mainActivity).inflate(R.layout.list_item_user_search, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        if (null != userSearchResult && !userSearchResult!!.isEmpty() && position < userSearchResult!!.size) {
            val userSearchResultItem: UserData =
                userSearchResult!![position]
                    ?: return
            holder.layoutUserSearch.setTag(userSearchResultItem)
            holder.layoutUserSearch.setOnClickListener(clickListener)
            holder.userSearchNameText.setText(if (TextUtils.isEmpty(userSearchResultItem.name.toString())) "" else userSearchResultItem.name)
        }
    }

    fun setData(userSearchResult: List<UserData>?) {
        this.userSearchResult = userSearchResult
    }

    override fun getItemCount(): Int {
        if (null != userSearchResult && !userSearchResult!!.isEmpty()) {
            return userSearchResult!!.size
        } else
            return 0
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutUserSearch: ConstraintLayout
        val userSearchNameText: AppCompatTextView
        val userSearchImage: AppCompatImageView

        init {
            userSearchNameText = itemView.findViewById(R.id.txt_username)
            layoutUserSearch = itemView.findViewById(R.id.layout_user_search)
            userSearchImage = itemView.findViewById(R.id.profile_image)
        }
    }

}