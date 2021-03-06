package com.example.myapplication

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), IMVVMObserver {

    private var ip = ""
    private var port = ""
    private val vm: JoystickViewModel by activityViewModels()


    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.add(this)

        // when pressing the connect button - connect to the flight gear
        binding.buttonFirst.setOnClickListener {
            ip = binding.IPText.text.toString()
            port = binding.PortText.text.toString()

            vm.update("Connect", 1.0)
            //findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

        }

        // binding of the ip
        binding.IPText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                vm.update("IP", binding.IPText.text.toString())

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

        // binding of the port
        binding.PortText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                vm.update("Port", binding.PortText.text.toString())

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun update(message: String, value: Double) {
        if(message == "VM_Connect"){
            if(value == 0.0){
                //alert
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Connection Failed")
                builder.setMessage("Please check your IP and port.")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { _, _ ->
                    Toast.makeText(activity,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                }
                builder.show()

            }
            else{
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)

            }
        }
    }
}