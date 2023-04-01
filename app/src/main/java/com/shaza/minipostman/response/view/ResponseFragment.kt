package com.shaza.minipostman.response.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.SpannableStringBuilder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.bold
import androidx.lifecycle.ViewModelProvider
import com.shaza.minipostman.R
import com.shaza.minipostman.databinding.FragmentResponseBinding
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.response.viewmodel.ResponseViewModel
import com.shaza.minipostman.shared.HttpResponse

class ResponseFragment : Fragment() {

    companion object {
        const val RESPONSE_KEY = "RESPONSE_KEY"
        fun newInstance() = ResponseFragment()

        fun newInstance(httpResponse:HttpResponse) : ResponseFragment{
            val fragment = ResponseFragment()
            val bundle = Bundle()
            bundle.putParcelable(RESPONSE_KEY,httpResponse)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: ResponseViewModel
    private lateinit var binding: FragmentResponseBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResponseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ResponseViewModel::class.java]
        viewModel.extractArgument(this.arguments)
        handleShowRequestResult()
        initListener()
    }

    private fun initListener(){
        binding.responseToolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun handleShowRequestResult(){
        requestBasicInfo()
        setSpannableText(viewModel.response?.url ?: "","URL: ",binding.requestUrl)
        handleShowRequestQueryParams()
        handleShowRequestHeaders()
        handleShowRequestBody()
        handleShowResponse()
        handleShowResponseHeaders()
    }

    private fun handleShowResponseHeaders() {
        if (viewModel.response?.responseHeaders != null) {
            showView(binding.responseHeader)
            setSpannableText(
                viewModel.response?.responseHeaders.toString() ?: "",
                "Response headers: ",
                binding.responseHeader
            )
        } else {
            hideView(binding.requestHeaders)
        }
    }

    private fun handleShowResponse() {
        if (viewModel.response?.response != null) {
            showView(binding.responseOrError)
            setSpannableText(
                viewModel.response?.response ?: "",
                "Response: ",
                binding.responseOrError
            )
        } else if (viewModel.response?.error != null) {
            showView(binding.responseOrError)
            binding.responseOrError.setTextColor(requireContext().getColor(R.color.red))
            setSpannableText(viewModel.response?.error ?: "", "Error: ", binding.responseOrError)

        } else {
            hideView(binding.responseOrError)
        }
    }

    private fun handleShowRequestBody() {
        if (viewModel.response?.bodyRequest != null) {
            showView(binding.bodyRequest)
            setSpannableText(
                viewModel.response?.bodyRequest ?: "",
                "Body request: ",
                binding.bodyRequest
            )
        } else {
            hideView(binding.bodyRequest)
        }
    }

    private fun handleShowRequestHeaders() {
        if (viewModel.response?.requestHeaders != null && viewModel.response?.requestHeaders!!.isNotEmpty()) {
            showView(binding.requestHeaders)
            setSpannableText(
                viewModel.response?.requestHeaders.toString() ?: "",
                "Request header: ",
                binding.requestHeaders
            )
        } else {
            hideView(binding.requestHeaders)
        }
    }

    private fun handleShowRequestQueryParams() {
        if (viewModel.response?.queryParams != null) {
            showView(binding.requestQueryParams)
            setSpannableText(
                viewModel.response?.queryParams ?: "",
                "Query params: ",
                binding.requestQueryParams
            )
        } else {
            hideView(binding.requestQueryParams)
        }
    }

    private fun requestBasicInfo() {
        setRequestTypeData()
        setSpannableText(viewModel.response?.statusCode.toString(), "Status code: ", binding.requestResponseBasicInfo.requestStatusCode)
        setSpannableText(viewModel.response?.elapsedTime.toString(), "Time: ", binding.requestResponseBasicInfo.requestTime)
    }

    private fun setRequestTypeData() {
        binding.requestResponseBasicInfo.requestType.text =
            viewModel.response?.httpRequestType?.name
        binding.requestResponseBasicInfo.requestType.backgroundTintList =
            when (viewModel.response?.httpRequestType) {
                HttpRequestType.GET -> requireContext().resources.getColorStateList(R.color.get_response_color)
                HttpRequestType.POST -> requireContext().resources.getColorStateList(R.color.post_response_color)
                null -> requireContext().resources.getColorStateList(R.color.black)
            }
    }

    private fun setSpannableText(normalText:String,boldText:String,textView: TextView){
        val s = SpannableStringBuilder()
            .bold { append(boldText) }
            .append(normalText)

        textView.text = s
    }

    private fun showView(view: View){
        view.visibility = VISIBLE
    }

    private fun hideView(view: View){
        view.visibility = GONE
    }
}