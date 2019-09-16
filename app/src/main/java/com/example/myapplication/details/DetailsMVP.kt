package com.example.myapplication.details

import androidx.lifecycle.LiveData
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem
import com.google.android.material.textfield.TextInputLayout

interface DetailsMVP {

    interface DetailsView{

        fun showMessage(message:String)
        fun showErrorMessage(message: String,inputfield: TextInputLayout)
        fun hideErrorMessage(inputfield: TextInputLayout)
        fun setArr(arr: LiveData<List<SubItem>>)
    }

    interface Presenter{

        fun addSubItem(item:String,price:Double,id:Int)
        fun observeSubList(id:Int)
        fun deleteSubItem(item: SubItem)
        fun editSubItem(item: SubItem)

        fun updateItem(item:Item)

        fun getSubItems(name:String):LiveData<List<SubItem>>
    }
}