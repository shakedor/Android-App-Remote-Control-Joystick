package com.example.myapplication

import java.io.OutputStream
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class FlightModel : IMVVMObservable{

    override val observers: ArrayList<IMVVMObserver> = ArrayList()

    private var IP = ""
    private var Port = ""
    private lateinit var fg: Socket
    private lateinit var out: OutputStream
    private var connected = 0


    fun setIP(ip: String){
        IP = ip
    }
    fun setPort(port : String){
        Port = port
    }

    // connect the app with the flight gear
    fun connect(){
        // variable that will help to verify if the app connected to the FG in the thread
        var block = 1
        Thread {

            try{
                // create a socket
                fg = Socket(IP, Port.toInt())

                // get the output of the socket
                out = fg.getOutputStream()

                if (fg.isConnected) {

                    connected = 1

                }
            }
            catch (e: Exception){
                try {
                    fg.close()
                }
                catch(closeE: Exception){ }
            }
            block = 0
        }.start()

        // waits for the thread to change the 'connected' variable
        while(block == 1);

        if(connected == 0){
            sendUpdateEvent("Model_Connect", 0.0)

        }
        else{
            sendUpdateEvent("Model_Connect", 1.0)

        }

    }

    // receive the new value of the property and notify
    fun update(property: String, value: Double){

        if (connected == 0){
            sendUpdateEvent("Model_Send", 0.0)

            return
        }
        var sendMessage = "set /controls/"

        // find which property has changed
        when (property) {
            "Aileron" -> {
                sendMessage += "flight/aileron "
            }
            "Elevator" -> {
                sendMessage += "flight/elevator "

            }
            "Rudder" -> {
                sendMessage += "flight/rudder "

            }
            "Throttle" -> {
                sendMessage += "/engines/current-engine/throttle "

            }
        }
        sendMessage += value.toString() + "\r\n"

        thread{
            try {
                out.write(sendMessage.toByteArray())
                out.flush()
            }
            catch (e : Exception){
                sendUpdateEvent("Model_Send", 0.0)
            }

        }

    }

    fun stop(){
        // disconnect
        connected = 0
        try{
            fg.close()
        }
        catch (e: java.lang.Exception){ }
    }

}