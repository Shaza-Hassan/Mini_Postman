package com.shaza.minipostman.home.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.shaza.minipostman.R
import com.shaza.minipostman.databinding.AddHeaderItemLayoutBinding
import com.shaza.minipostman.home.model.Header
import java.security.AccessController.getContext

class AddHeaderAdapter(val headers: MutableList<Header>, val onRemoveHeader: OnRemoveHeader) : RecyclerView.Adapter<AddHeaderAdapter.AddHeaderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddHeaderViewHolder {
        val binding = AddHeaderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddHeaderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return headers.size
    }

    override fun onBindViewHolder(holder: AddHeaderViewHolder, position: Int) {
        val header = headers[position]
        holder.bindView(header)
        holder.binding.removeHeader.setOnClickListener {
            onRemoveHeader.onRemoveHeader(position)
        }
    }

    class AddHeaderViewHolder(val binding: AddHeaderItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindView(header: Header){
            itemView.apply {
                binding.headerTitleEditText.doOnTextChanged { text, start, before, count ->
                    header.title = text.toString()
                }

                binding.headerValueEditText.doOnTextChanged { text, start, before, count ->
                    header.value = text.toString()
                }
            }
        }
    }
}

interface OnRemoveHeader{
    fun onRemoveHeader(index:Int)
}