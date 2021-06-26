package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSecondBinding


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(),SeekBar.OnSeekBarChangeListener, IMVVMObserver {

    private val vm: JoystickViewModel by activityViewModels()

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.IPText.text = ("IP: " +vm.getIP())
        binding.PortText.text = ("Port: " + vm.getPort())


        binding.seekBar2.setOnSeekBarChangeListener(this)

        binding.seekBar1.setOnSeekBarChangeListener(this)

        val listener = View.OnTouchListener(function = { view, motionEvent ->


            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                // calculate the next move
                val h = motionEvent.rawY - view.height/2
                val w = motionEvent.rawX - view.width/2
                // 420 to each direction
                val minX = 75
                val maxX = 505
                val minY = 330
                val maxY = 750


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
                val rangeA = maxX - minX
                val middleA = minX + (rangeA/2)
                val valueA = (view.x - middleA) / (rangeA/2)
                vm.update("Aileron", valueA.toDouble())

                // elevator
                val rangeE = maxY - minY
                val middleE = minY + (rangeE/2)
                val valueE = ((view.y - middleE) / (rangeE/2)) * -1
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (seekBar != null) {
            if(seekBar == binding.seekBar1){
                val temp = (progress-100).toDouble()
                val value = (temp/100)
                vm.update("Rudder", value)

            }
            else if (seekBar == binding.seekBar2){
                val temp = (progress).toDouble()
                val value = (temp/100)
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

                builder.setPositiveButton(android.R.string.yes) { _, _ ->
                    Toast.makeText(activity,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }

                builder.setNegativeButton(android.R.string.no) { _, _ ->
                    Toast.makeText(activity,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }

                builder.show()
            }
        }
    }
}