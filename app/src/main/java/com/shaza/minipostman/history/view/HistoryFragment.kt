package com.shaza.minipostman.history.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.shaza.minipostman.R
import com.shaza.minipostman.databinding.FragmentHistoryBinding
import com.shaza.minipostman.history.view.adapter.HistoryAdapter
import com.shaza.minipostman.history.view.adapter.HistoryItemListener
import com.shaza.minipostman.history.viewmodel.HistoryViewModel
import com.shaza.minipostman.response.view.ResponseFragment
import com.shaza.minipostman.shared.HttpResponse
import com.shaza.minipostman.shared.OrderClauses
import com.shaza.minipostman.shared.WhereClauses

class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() = HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        adapter = HistoryAdapter(viewModel.requests.value ?: mutableListOf(), historyItemListener)
        binding.historyList.adapter = adapter
        initObservers()
        initClickListener()
        viewModel.getAllRequests(requireContext())
    }

    private fun initObservers() {
        viewModel.requests.observe(viewLifecycleOwner) {
            adapter.list = it
            adapter.notifyDataSetChanged()
        }
    }

    private fun initClickListener() {
        onTypeFilterChange()

        onStatusFilterChange()

        onSortChange()

        onNavigationButtonClicked()
    }

    private fun onTypeFilterChange() {
        binding.httpTypeToggle.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                binding.allRequest.id -> {
                    if (isChecked) {
                        viewModel.updateRequestType(WhereClauses.GetAllRequest)
                    }
                }
                binding.getButton.id -> {
                    if (isChecked) {
                        viewModel.updateRequestType(WhereClauses.GetAllGETRequest)
                    }
                }
                binding.postButton.id -> {
                    if (isChecked) {
                        viewModel.updateRequestType(WhereClauses.GetAllPOSTRequest)
                    }
                }
            }
            viewModel.getAllRequests(requireContext())

        }
    }

    private fun onStatusFilterChange() {
        binding.requestStatusType.addOnButtonCheckedListener { group, checkedId, isChecked ->
            when (checkedId) {
                binding.allRequestStatus.id -> {
                    if (isChecked) {
                        viewModel.updateRequestStatus(WhereClauses.GetAllRequest)
                    }
                }
                binding.succeedStatus.id -> {
                    if (isChecked) {
                        viewModel.updateRequestStatus(WhereClauses.GetAllSuccessRequest)
                    }
                }
                binding.failedStatus.id -> {
                    if (isChecked) {
                        viewModel.updateRequestStatus(WhereClauses.GetAllFailedRequest)
                    }
                }
            }
            viewModel.getAllRequests(requireContext())

        }

    }

    private fun onSortChange() {
        binding.sortByTime.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                viewModel.orderClauses = OrderClauses.OrderByTime
            } else {
                viewModel.orderClauses = OrderClauses.OrderById
            }
            viewModel.getAllRequests(requireContext())
        }
    }

    private fun onNavigationButtonClicked() {
        binding.historyToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private val historyItemListener = object : HistoryItemListener {
        override fun onItemClicked(httpResponse: HttpResponse) {
            val fragment: Fragment = ResponseFragment.newInstance(httpResponse)
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            fragmentManager.beginTransaction().add(R.id.main_layout, fragment)
                .addToBackStack("").commit()
        }

    }
}