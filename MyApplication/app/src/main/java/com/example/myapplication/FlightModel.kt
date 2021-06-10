package com.example.myapplication

import android.content.ContentValues
import android.util.Log
import android.widget.TextView
import java.io.OutputStream
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class FlightModel : IMVVMObservable{

    override val observers: ArrayList<IMVVMObserver> = ArrayList()

    private var IP = ""
    private var Port = ""
    private lateinit var fg: Socket;
    private lateinit var out: OutputStream;
    private var connected = 0;


    fun setIP(ip: String){
        IP = ip;
    }
    fun setPort(port : String){
        Port = port;
    }



    fun connect(){
        Thread {


            // put try and catch!!
            // if did not work - send event to viewmodel.
            fg = Socket(IP, Port.toInt())


            out = fg.getOutputStream()

            if (!fg.isConnected) {

                Log.i(ContentValues.TAG, "not connected");
                sendUpdateEvent("Model_Connect", 0.0);

            } else {
                Log.i(ContentValues.TAG, "connected");
                connected = 1;
            }
        }.start();

    }

    fun update(property: String, value: Double){

        if (connected == 0){
            return;
        }
        var sendMessage = "set /controls/";

        if(property == "Aileron"){
            sendMessage += "flight/aileron " ;
        }
        else if(property == "Elevator"){
            sendMessage += "flight/elevator " ;

        }
        else if(property == "Rudder"){
            sendMessage += "flight/rudder " ;

        }
        else if(property == "Throttle"){
            sendMessage += "/engines/current-engine/throttle " ;

        }
        sendMessage += value.toString() + "\r\n";

        thread{
            // add try and catch!!
            out.write(sendMessage.toByteArray());
            out.flush();
        }

    }

    fun stop(){
        // disconnect
        fg.close();
    }

}