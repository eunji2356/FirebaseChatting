package com.eun7jii3.chatting.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eun7jii3.chatting.R
import com.eun7jii3.chatting.model.ChatData

class ChattingAdapter(private var context: Context, private var list: ArrayList<ChatData>, var myEmail: String):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var viewHolder:RecyclerView.ViewHolder

    companion object{
        const val userChat = 0
        const val myChat = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)

        when(viewType){
            0 -> {
                viewHolder = UserChatViewHolder(inflater.inflate(R.layout.chatting_item, parent, false))
            }
            1 -> {
                viewHolder = MyChatViewHolder(inflater.inflate(R.layout.chatting_my_item, parent, false))
            }
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        val chatEmail = list[position].user

        return if(chatEmail == myEmail){
            myChat
        } else{
            userChat
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val result = list[position]

        if(userChat == getItemViewType(position)){
            val userViewHolder = holder as UserChatViewHolder

            userViewHolder.user.text = result.user
            userViewHolder.userMessage.text = result.message
        }
        else{
            val myViewHolder = holder as MyChatViewHolder
            myViewHolder.message.text = result.message
        }
    }

    class UserChatViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var user: TextView = itemView.findViewById(R.id.chat_user)
        var userMessage: TextView = itemView.findViewById(R.id.chat_message)
    }

    class MyChatViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var message: TextView = itemView.findViewById(R.id.my_chat_message)
    }
}