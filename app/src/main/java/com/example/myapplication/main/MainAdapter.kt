package com.example.myapplication.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.details.SubItemActivity
import com.example.myapplication.model.Item
import kotlinx.android.synthetic.main.main_ticket.view.*
import java.text.SimpleDateFormat

class MainAdapter:RecyclerView.Adapter<MainAdapter.ViewHolder> {

    var context:Context
    var arr:List<Item>
    var presenter:MainMVP.Presenter
    constructor(context: Context)
    {
        this.context = context
        arr = emptyList()
        presenter = MainPresenter(null,context)
    }

    inner class ViewHolder(item:View):RecyclerView.ViewHolder(item)
    {
        var name = item.MainName
        var date = item.MainDate
        var price = item.TotalPrice

        var editButton = item.EditMain
        var deleteButton = item.DeleteMain

        init {
            itemView.setOnClickListener{

                var intent = Intent((context as Activity),SubItemActivity::class.java)
                intent.putExtra("MainItem",arr[adapterPosition])
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        var view = LayoutInflater.from(context).inflate(R.layout.main_ticket,parent,false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(holder.name.text.toString() != "")
            holder.name.text = ""

        if(holder.price.text.toString() != "")
            holder.price.text = ""

        if(holder.date.text.toString() != "")
            holder.date.text = ""

        var item = arr[position]

        holder.name.text = item.itemName

        var sdf = SimpleDateFormat("d MMM yyyy").format(item.itemDate)
        holder.date.text = sdf
        holder.price.text = item.totalPrice.toString()

        holder.editButton.setOnClickListener{

            var alert = AddAlert(item,1)
            alert.show((context as FragmentActivity).supportFragmentManager,"Edit Item")
            alert.isCancelable = false
        }

        holder.deleteButton.setOnClickListener{

            presenter.deleteItem(item)
        }
    }

    fun updateList(l:List<Item>)
    {
        this.arr = l
        notifyDataSetChanged()
    }
}