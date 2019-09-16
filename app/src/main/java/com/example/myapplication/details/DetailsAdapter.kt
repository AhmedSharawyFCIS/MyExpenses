package com.example.myapplication.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.main.AddAlert
import com.example.myapplication.main.MainAdapter
import com.example.myapplication.main.MainMVP
import com.example.myapplication.main.MainPresenter
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem
import kotlinx.android.synthetic.main.main_ticket.view.*
import java.text.SimpleDateFormat

class DetailsAdapter:RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    var context: Context
    var arr:List<SubItem>
    var presenter: DetailsMVP.Presenter
    var mainItem:Item
    constructor(context: Context,mainItem:Item)
    {
        this.context = context
        arr = emptyList()
        presenter = DetailsPresenter(null,context)

        this.mainItem = mainItem
    }

    class ViewHolder(item: View):RecyclerView.ViewHolder(item)
    {
        var name = item.MainName
        var date = item.MainDate
        var price = item.TotalPrice

        var editButton = item.EditMain
        var deleteButton = item.DeleteMain
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsAdapter.ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.main_ticket,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: DetailsAdapter.ViewHolder, position: Int) {

        if(holder.name.text.toString() != "")
            holder.name.text = ""

        if(holder.price.text.toString() != "")
            holder.price.text = ""

        if(holder.date.text.toString() != "")
            holder.date.text = ""

        var item = arr[position]

        holder.name.text = item.subItemName

        var sdf = SimpleDateFormat("d MMM yyyy").format(item.subItemDate)
        holder.date.text = sdf
        holder.price.text = item.price.toString()

        holder.editButton.setOnClickListener{

            var alert = DetailsAlert(item,1,mainItem)
            alert.show((context as FragmentActivity).supportFragmentManager,"Edit Item")
            alert.isCancelable = false
        }

        holder.deleteButton.setOnClickListener{

            mainItem.totalPrice -= item.price
            presenter.deleteSubItem(item)

            presenter.updateItem(mainItem)
        }
    }

    fun updateList(l:List<SubItem>)
    {
        this.arr = l
        notifyDataSetChanged()
    }
}