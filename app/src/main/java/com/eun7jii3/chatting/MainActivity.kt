package com.eun7jii3.chatting

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    companion object{
        lateinit var mAuth: FirebaseAuth

        // 비밀번호 정규식
        val PASSWORD_PATTERN: Pattern = Pattern.compile("^[a-zA-Z0-9!@.#$%^&*?_~]{4,16}$")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //파이어베이스 인증 객체 선언
        mAuth = FirebaseAuth.getInstance()

        // 회원가입 버튼 클릭
        btn_signUp.setOnClickListener {
            val email = id.text.toString()
            val pwd = password.text.toString()

            if(isValidEmail() && isValidPwd()){
                signUp(email, pwd)
            }
        }

        //로그인 버튼 클릭
        btn_login.setOnClickListener {
            val email = id.text.toString()
            val pwd = password.text.toString()

            if(isValidEmail() && isValidPwd()){
                login(email, pwd)
            }
        }
    }

    /**
     * 이메일 유효성 검사
     */
    private fun isValidEmail(): Boolean{
        val email = id.text.toString()

        return if(email.isEmpty()){
            false

        } else Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * 비밀번호 유효성 검사
    */
    private fun isValidPwd(): Boolean{
        val pwd = password.text.toString()

        return if(pwd.isEmpty()){
            false
        } else PASSWORD_PATTERN.matcher(pwd).matches()
    }

    /**
     * 회원가입 메소드
     */
    private fun signUp(email: String, pwd: String){
        mAuth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "회원가입 성공.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    Log.e(
                        "Firebase-SignUp",
                        "createUserWithEmail:failure",
                        task.exception
                    )

                    Toast.makeText(
                        this,
                        "회원가입 실패.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /**
     * 로그인 메소드
     */
    private fun login(email: String, pwd: String){
        mAuth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "로그인 성공",
                        Toast.LENGTH_SHORT
                    ).show()

                    moveNextPage(email)
                }
                else {
                    Log.e(
                        "Firebase-Login",
                        "signInWithEmail:failure",
                        task.exception
                    )

                    Toast.makeText(
                        this,
                        "로그인 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    /**
     * Chatting 방으로 이동
     */
    private fun moveNextPage(email:String){
        val intent = Intent(this, ChattingActivity::class.java)
        intent.putExtra("email", email)
        startActivityForResult(intent, Activity.RESULT_OK)
    }
}