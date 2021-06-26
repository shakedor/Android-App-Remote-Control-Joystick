package com.example.myapplication

import androidx.lifecycle.ViewModel
import java.util.*

class JoystickViewModel : ViewModel(),IMVVMObserver, IMVVMObservable {

    override val observers: ArrayList<IMVVMObserver> = ArrayList()

    private var IP = ""
    private var Port = ""
    private val model = FlightModel()

    init {
        model.add(this)
    }

    // notify the model when one of the property changed
    override fun update(property: String, value: Double) {
        if(property == "Aileron" || property == "Elevator" ||property == "Rudder" ||property == "Throttle"){
            model.update(property,value)
        }
        if(property == "Model_Connect"){
            // failed to connect!
            sendUpdateEvent("VM_Connect", value)
        }else if(property == "Connect"){
            sendData()
        }
        else if(property == "Disconnect"){
            model.stop()
        }
        else if(property == "Model_Send"){
            if(value == 0.0){
                // failed to send data to server
                sendUpdateEvent("VM_Send", 0.0)

            }
        }

    }

    // update the text boxes with the given ip and port
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

    // send the data of the ip and port to the model
    private fun sendData(){
        model.setIP(IP)
        model.setPort(Port)
        model.connect()

    }

}