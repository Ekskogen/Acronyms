package com.example.acronymslist.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.acronymslist.R
import com.example.acronymslist.data.models.LongForm

class AcronymsAdapter(
    private var acronyms: ArrayList<LongForm>,
) : RecyclerView.Adapter<AcronymsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_acronym, parent, false))

    override fun onBindViewHolder(holder: AcronymsAdapter.ViewHolder, position: Int) {
        val c = acronyms[position]
        with(holder) {
            name.text = c.lf
            freq.text = itemView.context?.getString(R.string.item_freq, c.freq)
            year.text = itemView.context?.getString(R.string.item_year, c.since)
        }
    }

    override fun getItemCount(): Int {
        return acronyms.size
    }

    fun setData(listAcronyms: List<LongForm>) {
        this.acronyms.clear()
        this.acronyms.addAll(listAcronyms)
        notifyDataSetChanged()
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.nameTV)
        val freq = view.findViewById<TextView>(R.id.freqTV)
        val year = view.findViewById<TextView>(R.id.yearTV)
    }

}