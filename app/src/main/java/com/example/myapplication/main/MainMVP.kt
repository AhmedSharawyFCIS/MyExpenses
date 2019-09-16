package com.example.myapplication.main

import android.widget.EditText
import androidx.lifecycle.LiveData
import com.example.myapplication.model.Item
import com.google.android.material.textfield.TextInputLayout

interface MainMVP {

    interface MainView{

        fun showMessage(message:String)
        fun showErrorMessage(message: String,inputfield:TextInputLayout)
        fun hideErrorMessage(inputfield:TextInputLayout)
        fun setArr(arr:LiveData<List<Item>>)
    }

    interface Presenter{

        fun addItem(item:String)
        fun observeMainList()
        fun deleteItem(item:Item)
        fun editItem(item:Item)

        fun getItems(name:String):LiveData<List<Item>>
    }
}