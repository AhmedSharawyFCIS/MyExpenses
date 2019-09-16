package com.example.myapplication.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Item")
class Item :Serializable{

    @PrimaryKey(autoGenerate = true)
    var itemID:Int = 0

    var itemName:String = ""

    var itemDate:Long = 0

    var totalPrice:Double = 0.0
}