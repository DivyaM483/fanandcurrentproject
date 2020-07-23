package com.flirto.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {
    private lateinit var editProfileRL:RelativeLayout
    private lateinit var favoritesRL:RelativeLayout
    private lateinit var inviteFrndsRL:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editProfileRL =findViewById(R.id.relativeLayout)
        editProfileRL.setOnClickListener {
            val intent= Intent(this, Edit_ProfileActivity::class.java)
            startActivity(intent)
        }

        favoritesRL=findViewById(R.id.relativeLayout2)
        favoritesRL.setOnClickListener {
            val intent= Intent(this, Favorites_activity::class.java)
            startActivity(intent)
        }

        inviteFrndsRL =findViewById(R.id.relativeLayout4)
        inviteFrndsRL.setOnClickListener {
            val message:String="Hey..it's me.Am using this wonderful app where you can find new people around you and would like you to join this..Please follow the below link"
            val intent=Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,message)
             intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Invite friends through:"))

        }

    }



}