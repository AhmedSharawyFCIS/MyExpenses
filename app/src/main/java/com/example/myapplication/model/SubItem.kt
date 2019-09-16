package com.example.myapplication.model

import android.content.ClipData
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Item::class
    ,parentColumns = arrayOf("itemID")
    ,childColumns = arrayOf("FID")
    ,onDelete = ForeignKey.CASCADE)))
class SubItem {

    @PrimaryKey(autoGenerate = true)
    var subItemID:Int = 0

    var subItemName:String = ""

    var subItemDate:Long = 0

    var price:Double = 0.0

    var FID:Int=0
}