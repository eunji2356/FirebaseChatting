package com.eun7jii3.chatting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.eun7jii3.chatting.adapter.ChattingAdapter
import com.eun7jii3.chatting.model.ChatData
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chatting.*
import kotlin.collections.ArrayList

class ChattingActivity : AppCompatActivity() {

    companion object{
        lateinit var firebaseDatabase: FirebaseDatabase
        lateinit var databaseReference: DatabaseReference

        var userEmail: String = ""
        var chatList: ArrayList<ChatData> = arrayListOf()
    }
    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        //realTime Database 가져오기 위한 초기 세팅
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference

        userEmail = intent.getStringExtra("email")!!

        //전송 버튼 클릭
        btn_send.setOnClickListener {
            val message = edit_message.text.toString()
            if(message.isNotEmpty()){
                val chatData = ChatData(userEmail, message)
                // 기본 database 하위 message 라는 child 에 chatData 를 list 로 만들기
                databaseReference.child("message").push().setValue(chatData)
                    .addOnSuccessListener {
                        chatList.add(chatData)
                        changeChatData()
                    }
                    .addOnCanceledListener {
                        Log.e("@@@@ OnCanceled", "메세지 전송에 실패하였습니다.")
                    }
                edit_message.setText("")
            }
        }

        getChatDataInit()
    }

    /**
     * RecyclerView Data 추가 및 갱신
     */
    fun changeChatData(){
        val adapter = ChattingAdapter(context, chatList, userEmail)

        recycler.layoutManager = LinearLayoutManager(context)
        recycler.scrollToPosition(adapter.itemCount -1)
        recycler.adapter = adapter

        adapter.notifyDataSetChanged()
    }

    /**
     * Firebase Realtime Database 채팅 데이터 받아오기
     *
     */
    private fun getChatDataInit(){
        chatList = arrayListOf()

        databaseReference.child("message").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e("@@@@ Single Event Error", error.toString())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(snapshot in dataSnapshot.children){
                    val chatData = snapshot.getValue(ChatData::class.java)
                    chatList.add(ChatData(chatData!!.user, chatData.message))

                    changeChatData()
                }
            }
        })
    }
}