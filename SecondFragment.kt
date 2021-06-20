package com.example.myapplication

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSecondBinding
import java.lang.Math.*
import kotlin.math.sqrt


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(),SeekBar.OnSeekBarChangeListener, IMVVMObserver {

    var xCenter =0;
    var yCenter = 0;

    private val vm: JoystickViewModel by activityViewModels()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Hide the action bar
//        val actionBar = supportActionBar
//
//        actionBar!!.hide()
//    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.IPText.text = ("IP: " +vm.getIP())
        binding.PortText.text = ("Port: " + vm.getPort())


        binding.seekBar2.setOnSeekBarChangeListener(this)

        binding.seekBar1.setOnSeekBarChangeListener(this)

        var listener = View.OnTouchListener(function = {view, motionEvent ->

            if(xCenter == 0){
                xCenter = binding.inner.x.toInt() +  binding.inner.drawable.intrinsicWidth/2
                yCenter = binding.inner.y.toInt() + binding.inner.drawable.intrinsicHeight/2
            }


            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                // calculate the next move
                var h = motionEvent.rawY - view.height/2
                var w = motionEvent.rawX - view.width/2
                // 420 to each direction
                var minX = 75
                var maxX = 505
                var minY = 330
                var maxY = 750




                // check if the move is inside the joystick
                if((w < maxX) && (w > minX)){
                    view.x = w
                }
                else{
                    if(w < minX){
                        view.x = minX.toFloat()
                    }
                    else{
                        view.x = maxX.toFloat()
                    }
                }
                if(h < maxY && h > minY){
                    view.y = h
                }
                else{
                    if(h < minY){
                        view.y = minY.toFloat()
                    }
                    else{
                        view.y = maxY.toFloat()
                    }
                }



                // aileron
                var rangeA = maxX - minX
                var middleA = minX + (rangeA/2)
                var valueA = (view.x - middleA) / (rangeA/2)
                vm.update("Aileron", valueA.toDouble())

                // elevator
                var rangeE = maxY - minY
                var middleE = minY + (rangeE/2)
                var valueE = ((view.y - middleE) / (rangeE/2)) * -1
                vm.update("Elevator", valueE.toDouble())


            }

            true

        })

        binding.inner.setOnTouchListener(listener)


        binding.buttonSecond.setOnClickListener {
            vm.update("Disconnect",1.0)
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    /*
    private fun isInside(v: View, e: MotionEvent): Boolean){

    }

     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar != null) {
            if(seekBar == binding.seekBar1){
                val value = ((progress-100)/100).toDouble()
                vm.update("Rudder", value)

            }
            else if (seekBar == binding.seekBar2){
                val value = ((progress)/100).toDouble()
                vm.update("Throttle", value)


            }
        }

    }



    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun update(message: String, value: Double) {
        if(message == "Model_Send"){
            if(value == 0.0){

                //alert

                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Failed to send data")
                builder.setMessage("Please reconnect.")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(activity,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(activity,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }

/*
                builder.setNeutralButton("Maybe") { dialog, which ->
                    Toast.makeText(activity,
                        "Maybe", Toast.LENGTH_SHORT).show()
                }

                 */
                builder.show()
            }
        }
    }
}