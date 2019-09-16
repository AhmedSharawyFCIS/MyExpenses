package com.example.myapplication.details

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.myapplication.R
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem
import kotlinx.android.synthetic.main.custom_search_layout.view.*
import java.text.SimpleDateFormat

class SearchAdapter2:ArrayAdapter<SubItem> {

    var arr:ArrayList<SubItem>
    constructor(context: Context, arr:ArrayList<SubItem>):super(context,0,arr)
    {
        this.arr = ArrayList()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var view = LayoutInflater.from(context).inflate(R.layout.custom_search_layout,parent,false)

        var item =getItem(position)
        if(item != null)
        {
            view.ItemNameSearch.text = item.subItemName
            var sdf = SimpleDateFormat("d MMM yyyy").format(item.subItemDate)
            view.ItemDateSearch.text = sdf
        }

        return view

    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(query: CharSequence?): FilterResults {

                var result = FilterResults()
                var suggestions = ArrayList<SubItem>()

                if (query == null || query.isEmpty())
                    suggestions.addAll(arr)

                else
                {
                    var q = query.toString().toLowerCase().trim()
                    for(i in arr)
                    {

                        if(i.subItemName.toLowerCase().trim().contains(q))
                        {
                            suggestions.add(i)
                        }
                    }
                }

                result.values = suggestions
                result.count = suggestions.size

                return  result
            }

            override fun publishResults(p0: CharSequence?, result: FilterResults?) {

                clear()
                addAll(result!!.values as ArrayList<SubItem>)
                notifyDataSetChanged()
            }

            override fun convertResultToString(resultValue: Any?): CharSequence {

                return (resultValue as SubItem).subItemName
            }


        }


    }

    fun updateArr(arr:List<SubItem>)
    {
        this.arr = ArrayList()
        this.arr.addAll(arr)
        notifyDataSetChanged()
    }
}