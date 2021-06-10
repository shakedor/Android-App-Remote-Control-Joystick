package com.example.myapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentFirstBinding
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var ip = "";
    private var port = "";
    private val vm: JoystickViewModel by activityViewModels()


    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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


        binding.buttonFirst.setOnClickListener {
            ip = binding.IPText.text.toString();
            port = binding.PortText.text.toString();

            vm.update("Connect", 1.0);
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.IPText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                vm.update("IP", binding.IPText.text.toString());

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                //binding.textviewFirst.text = s;
                //use the IP!
            }
        })


        binding.PortText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                vm.update("Port", binding.PortText.text.toString());

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                //binding.textviewFirst.text = s;
                //use the IP!
            }
        })



    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}