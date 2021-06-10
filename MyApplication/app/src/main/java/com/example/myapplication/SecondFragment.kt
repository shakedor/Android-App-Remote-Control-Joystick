package com.example.myapplication

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentSecondBinding
import kotlin.random.Random.Default.nextDouble

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment(),SeekBar.OnSeekBarChangeListener {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.IPText.text = vm.getIP()
        binding.PortText.text = vm.getPort()


        binding.seekBar2.setOnSeekBarChangeListener(this)

        binding.seekBar1.setOnSeekBarChangeListener(this)


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
            if(seekBar.max == 2){
                vm.update("Elevator",-1.0)
                vm.update("Aileron", -1.0)
                vm.update("Rudder", -1.0)
                vm.update("Throttle", -1.0)

            }
            else{
                vm.update("Elevator",1.0)
                vm.update("Aileron", 1.0)
                vm.update("Rudder", 1.0)
                vm.update("Throttle", 1.0)

            }
        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}