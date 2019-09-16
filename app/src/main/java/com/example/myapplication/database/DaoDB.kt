package com.example.myapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem

@Dao
interface DaoDB {

    @Insert
    fun addItem(item:Item)

    @Update
    fun updateItem(item:Item)

    @Delete
    fun deleteItem(item:Item)

    @Query("select * from Item")
    fun getAllItems():LiveData<List<Item>>

    @Query("select * from Item where itemName like :name")
    fun getItems(name:String):LiveData<List<Item>>

    @Insert
    fun addSubItem(item:SubItem)

    @Update
    fun updateSubItem(item: SubItem)

    @Delete
    fun deleteSubItem(item: SubItem)

    @Query("select * from SubItem where FID = :id")
    fun getAllSubItems(id:Int):LiveData<List<SubItem>>

    @Query("select * from SubItem where subItemName like :name")
    fun getSubItems(name:String):LiveData<List<SubItem>>
}