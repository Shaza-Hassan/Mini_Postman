package com.shaza.minipostman.home.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.shaza.minipostman.R
import com.shaza.minipostman.databinding.FragmentHomeBinding
import com.shaza.minipostman.history.view.HistoryFragment
import com.shaza.minipostman.home.model.Header
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.home.view.adapter.AddHeaderAdapter
import com.shaza.minipostman.home.view.adapter.OnRemoveHeader
import com.shaza.minipostman.home.viewmodel.HomeViewModel
import com.shaza.minipostman.response.view.ResponseFragment
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.utils.HTTPCallback
import com.shaza.minipostman.utils.hideKeyboardFrom


class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: AddHeaderAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initRecyclerView()
        setOnClickListener()
    }

    private fun initRecyclerView(){
        adapter = AddHeaderAdapter(viewModel.headers,onRemoveHeader)
        binding.addingHeader.headers.adapter = adapter
    }

    private fun setOnClickListener(){
        onAddHeaderButtonClicked()

        onHttpTypeChanged()

        onCreateRequestClicked()

        onUrlEditTextChange()

        onBodyEditTextChange()

        binding.homeToolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.history_menu_item -> {
                    val fragment: Fragment = HistoryFragment()
                    val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
                    fragmentManager.beginTransaction().add(R.id.main_layout, fragment)
                        .addToBackStack("").commit()
                }
            }

            true
        }
    }

    private fun onAddHeaderButtonClicked(){
        binding.addingHeader.addNewHeader.setOnClickListener {
            viewModel.headers.add(Header())
            adapter.notifyDataSetChanged()
        }
    }

    private fun onHttpTypeChanged(){
        binding.httpType.httpTypeToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when(checkedId){
                binding.httpType.getButton.id -> {
                    if (isChecked) {
                        binding.bodyOfPostRequestLayout.visibility = GONE
                        viewModel.requestType = HttpRequestType.GET
                    }
                }
                binding.httpType.postButton.id -> {
                    if (isChecked) {
                        binding.bodyOfPostRequestLayout.visibility = VISIBLE
                        viewModel.requestType = HttpRequestType.POST
                    }
                }
            }
        }
    }

    private val onRemoveHeader = object :OnRemoveHeader{
        override fun onRemoveHeader(index:Int) {
            viewModel.headers.removeAt(index)
            adapter.notifyDataSetChanged()
        }
    }

    private fun onUrlEditTextChange(){
        binding.urlEditText.doOnTextChanged { text, start, before, count ->
            viewModel.url = text.toString()
        }
    }

    private fun onBodyEditTextChange(){
        binding.bodyEditText.doOnTextChanged { text, start, before, count ->
            viewModel.body = text.toString()
        }
    }

    private fun onCreateRequestClicked(){
        binding.createRequest.setOnClickListener {
            hideKeyboardFrom(requireContext(),requireView())
            viewModel.makeRequest(httpCallback)
        }
    }

    private val httpCallback = object : HTTPCallback {
        override fun processRunning() {
            showLoading()
        }
        override fun processFinish(output: HttpResponse?) {
            showData()
            navigateToResultScreen(output)
        }

        override fun processFailed(output: HttpResponse?) {
            showData()
            navigateToResultScreen(output)
        }
    }

    private fun navigateToResultScreen(output:HttpResponse?){
        if (output != null){
            viewModel.addRequestToDB(requireContext(),output)
            val fragment: Fragment = ResponseFragment.newInstance(output)
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().add(R.id.main_layout, fragment)
                .addToBackStack("").commit()
        }
    }

    private fun showLoading() {
        binding.loading.visibility = VISIBLE
        binding.createRequest.visibility = GONE
        binding.homeScroll.visibility = GONE
    }

    private fun showData() {
        binding.loading.visibility = GONE
        binding.createRequest.visibility = VISIBLE
        binding.homeScroll.visibility = VISIBLE
    }
}
