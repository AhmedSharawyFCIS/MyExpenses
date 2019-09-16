package com.example.myapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem

@Database(entities = [(Item::class),(SubItem::class)],version = 3,exportSchema = false)
abstract class MyDataBase:RoomDatabase() {

    abstract fun getDao():DaoDB


    companion object
    {

        private var instance:MyDataBase? = null

        fun getInstance(context:Context):MyDataBase?
        {
            if(instance == null)
            {
                synchronized(MyDataBase::class)
                {
                    instance = Room.databaseBuilder(context,MyDataBase::class.java,"MyDatabase")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return instance
        }
    }
}