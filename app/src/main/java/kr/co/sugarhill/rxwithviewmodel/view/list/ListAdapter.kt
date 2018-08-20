package kr.co.sugarhill.rxwithviewmodel.view.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import kr.co.sugarhill.rxwithviewmodel.R
import kr.co.sugarhill.rxwithviewmodel.network.FranchiseBrand

class ListAdapter : RecyclerView.Adapter<FranchiseBrandHolder>() {
    var keyword: String = ""
    var list: List<FranchiseBrand> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FranchiseBrandHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_franchise, parent, false)
        return FranchiseBrandHolder(parent.context, view)
    }

    override fun onBindViewHolder(holder: FranchiseBrandHolder, position: Int) {
        holder.updateUI(list[position], keyword)
    }

    override fun getItemCount(): Int = list.size
}