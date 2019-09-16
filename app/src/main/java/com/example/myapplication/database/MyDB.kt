package com.example.myapplication.database

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem

class MyDB {

    var dao:DaoDB
    var context:Context
    var allItem:LiveData<List<Item>>

    constructor(context: Context)
    {
        this.context = context

        dao =MyDataBase.getInstance(context)!!.getDao()

        allItem = dao.getAllItems()


    }

    fun addItem(item:Item)
    {
        ItemAsync(dao,1).execute(item)
    }

    fun updateItem(item:Item)
    {
        ItemAsync(dao,2).execute(item)
    }

    fun deleteItem(item:Item)
    {
        ItemAsync(dao,3).execute(item)
    }

    fun getItems(name:String):LiveData<List<Item>>
    {
        return dao!!.getItems(name)
        //return GetItemAsync(dao).execute(name).get()
    }
    fun addSubItem(item:SubItem)
    {
        SubItemAsync(dao,1).execute(item)
    }

    fun updateSubItem(item:SubItem)
    {
        SubItemAsync(dao,2).execute(item)
    }

    fun deleteSubItem(item:SubItem)
    {
        SubItemAsync(dao,3).execute(item)
    }

    fun getSubItems(id:Int):LiveData<List<SubItem>>
    {
        return  dao.getAllSubItems(id)
    }

    fun getSubItems(name:String):LiveData<List<SubItem>>
    {
        return dao.getSubItems(name)
    }
    companion object{

        class ItemAsync:AsyncTask<Item,Unit,Unit>
        {
            var dao:DaoDB
            var flag:Int
            constructor(dao:DaoDB,flag:Int)
            {
                this.dao = dao
                this.flag = flag
            }
            override fun doInBackground(vararg p0: Item?) {

                if(flag == 1)
                    dao.addItem(p0[0]!!)
                if(flag == 2)
                    dao.updateItem(p0[0]!!)
                if(flag == 3)
                    dao.deleteItem(p0[0]!!)

            }
        }

        class SubItemAsync:AsyncTask<SubItem,Unit,Unit>
        {
            var dao:DaoDB
            var flag:Int
            constructor(dao:DaoDB,flag:Int)
            {
                this.dao = dao
                this.flag = flag
            }
            override fun doInBackground(vararg p0: SubItem?) {

                if(flag == 1)
                    dao.addSubItem(p0[0]!!)
                if(flag == 2)
                    dao.updateSubItem(p0[0]!!)
                if(flag == 3)
                    dao.deleteSubItem(p0[0]!!)

            }
        }




    }
}
