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

            try{
                fg = Socket(IP, Port.toInt())

                out = fg.getOutputStream()

                if (fg.isConnected) {

                    connected = 1;

                }
            }
            catch (e: Exception){

                try {
                    fg.close();
                }
                catch(closeE: Exception){ }

            }

        }.start();

        if(connected == 0){
            sendUpdateEvent("Model_Connect", 0.0);

        }
        else{
            sendUpdateEvent("Model_Connect", 1.0);

        }

    }


    fun update(property: String, value: Double){

        if (connected == 0){
            sendUpdateEvent("Model_Send", 0.0);

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
            try {
                out.write(sendMessage.toByteArray());
                out.flush();
            }
            catch (e : Exception){
                sendUpdateEvent("Model_Send", 0.0);
            }

        }

    }

    fun stop(){
        // disconnect
        connected = 0;
       try{
           fg.close()
       }
       catch (e: java.lang.Exception){ }
    }

}