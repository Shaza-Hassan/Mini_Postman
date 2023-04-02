package com.shaza.minipostman.history.view.adapter

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.shaza.minipostman.R
import com.shaza.minipostman.databinding.HistoryRequestItemLayoutBinding
import com.shaza.minipostman.shared.HttpRequestType
import com.shaza.minipostman.shared.HttpResponse

class HistoryAdapter(var list: MutableList<HttpResponse>, val historyItemListener: HistoryItemListener) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryRequestItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val httpResponse = list[position]
        holder.bindingView(httpResponse)
        holder.binding.historyCardItem.setOnClickListener {
            historyItemListener.onItemClicked(httpResponse)
        }
    }

    class HistoryViewHolder(val binding:HistoryRequestItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindingView(httpResponse: HttpResponse){
            itemView.apply {
                binding.requestType.text = httpResponse.httpRequestType.name
                binding.requestType.backgroundTintList = when(httpResponse.httpRequestType){
                    HttpRequestType.GET -> context.resources.getColorStateList(R.color.get_response_color)
                    HttpRequestType.POST -> context.resources.getColorStateList(R.color.post_response_color)
                }

                setSpannableText(httpResponse.url,"URL: ",binding.requestUrl)

                setSpannableText("${httpResponse.elapsedTime} ms", "Time: ", binding.requestTime)

                if (httpResponse.response != null){
                    binding.requestStatus.text = context.getString(R.string.succeed)
                    binding.requestStatus.backgroundTintList = context.resources.getColorStateList(R.color.green)
                }else if (httpResponse.error != null){
                    binding.requestStatus.text = context.getString(R.string.failed)
                    binding.requestStatus.backgroundTintList = context.resources.getColorStateList(R.color.red)
                }
            }

        }

        private fun setSpannableText(normalText:String,boldText:String,textView: TextView){
            val s = SpannableStringBuilder()
                .bold { append(boldText) }
                .append(normalText)

            textView.text = s
        }
    }
}

interface HistoryItemListener{
    fun onItemClicked(httpResponse: HttpResponse)
}