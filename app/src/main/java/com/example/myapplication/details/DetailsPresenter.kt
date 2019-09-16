package com.example.myapplication.details

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myapplication.database.MyDB
import com.example.myapplication.main.MainMVP
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem
import java.util.*

class DetailsPresenter:DetailsMVP.Presenter {

    var detailsView: DetailsMVP.DetailsView?=null
    var context: Context?=null
    var db: MyDB?=null
    constructor(view: DetailsMVP.DetailsView?, context: Context?)
    {
        this.detailsView = view
        this.context = context
        db = MyDB(context!!)

    }
    override fun addSubItem(text: String,p:Double,Parentid:Int) {



        var item = SubItem()
        item.subItemName = text
        item.price = p
        item.subItemDate = Date().time // long
        item.FID = Parentid
        db!!.addSubItem(item)

    }

    override fun observeSubList(id:Int) {


        var allItems = db!!.getSubItems(id)

        detailsView!!.setArr(allItems)
    }

    override fun deleteSubItem(item: SubItem) {

        db!!.deleteSubItem(item)
    }

    override fun editSubItem(item: SubItem) {
        db!!.updateSubItem(item)
    }

    override fun updateItem(item:Item){

        db!!.updateItem(item)
    }

    override fun getSubItems(name: String): LiveData<List<SubItem>> {

        return db!!.getSubItems(name)
    }

}