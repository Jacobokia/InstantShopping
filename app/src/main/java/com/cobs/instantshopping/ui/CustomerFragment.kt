package com.cobs.instantshopping.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cobs.instantshopping.databinding.FragmentCustomerBinding

class CustomerFragment : Fragment() {


    private var _binding: FragmentCustomerBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCustomerBinding.inflate(inflater, container, false)
        val view = binding.root

        return view

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}