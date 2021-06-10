package com.example.myapplication

import android.content.ClipData
import android.content.ContentValues
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import java.util.*

class JoystickViewModel : ViewModel(),IMVVMObserver {
    private var IP = ""
    private var Port = ""
    private val model = FlightModel()

    init {
        model.add(this);
    }



    override fun update(property: String, value: Double) {
        if(property == "Aileron" || property == "Elevator" ||property == "Rudder" ||property == "Throttle"){
            model.update(property,value)
        }
        if(property == "Model_Connect"){
            if(value == 0.0){
                // failed to connect!
            }
        }else if(property == "Connect"){
            sendData();
        }
        else if(property == "Disconnect"){
            model.stop();
        }

    }



    fun update(property: String, text: String) {

        if(property == "IP"){
            IP = text
        }
        else if(property == "Port"){
            Port = text
        }

    }



    fun getIP(): String {
        return IP
    }

    fun getPort(): String{
        return Port
    }

    private fun sendData(){

        model.setIP(IP);
        model.setPort(Port);
        model.connect();

    }

}