package com.example.myapplication.main

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myapplication.database.DaoDB
import com.example.myapplication.database.MyDB
import com.example.myapplication.model.Item
import java.util.*

class MainPresenter:MainMVP.Presenter {

    var mainView:MainMVP.MainView?=null
    var context:Context?=null
    var db:MyDB?=null
    constructor(view: MainMVP.MainView?,context: Context?)
    {
        this.mainView = view
        this.context = context
        db = MyDB(context!!)

    }
    override fun addItem(text: String) {



        var item = Item()
        item.itemName = text
        item.totalPrice = 0.0
        item.itemDate = Date().time
        db!!.addItem(item)

    }

    override fun observeMainList() {


        var allItems = db!!.allItem

        mainView!!.setArr(allItems)
    }

    override fun deleteItem(item:Item) {

        db!!.deleteItem(item)
    }

    override fun editItem(item: Item) {
        db!!.updateItem(item)
    }

    override fun getItems(name: String): LiveData<List<Item>> {
        return db!!.getItems(name)
    }

}