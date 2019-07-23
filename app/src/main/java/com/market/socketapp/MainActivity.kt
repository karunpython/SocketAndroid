package com.market.socketapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException

class MainActivity : AppCompatActivity() {
    lateinit var message: String
    private var mSocket: Socket? = null

    init {
        try {
            mSocket = IO.socket("http://192.168.43.163:2000")
        } catch (e: URISyntaxException) {
            Log.d("myTag", e.message)
        }
        mSocket!!.connect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (mSocket!!.connected()){
            Log.d("TAGS", "Socket Connected")
        }else{
            Log.d("TAGS", "Socket not connected")
        }
        tv.text = ""
        setListening()

        button.setOnClickListener {
            val message = edit_name.text.toString().trim { it <= ' ' }
            edit_name.setText("")
            if (message.isNotEmpty()) {
                //send message
                val jsonString = "{message: '$message'}"
                try {
                    val jsonData = JSONObject(jsonString)
                    mSocket!!.emit("message", message)
                } catch (e: JSONException) {
                    Log.d("me", "error send message " + e.message)
                }

            }
        }

    }//on create

    private fun setListening() {
        mSocket!!.on("message") { args ->
            try {
              //  val messageJson = JSONObject(args[0].toString())
               // message = messageJson.getString("message")
                message =args[0].toString()
                runOnUiThread { tv.text = message }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
    }

}//e