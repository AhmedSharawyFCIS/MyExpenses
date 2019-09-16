package com.example.myapplication.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.model.Item
import kotlinx.android.synthetic.main.add_item.*
import kotlinx.android.synthetic.main.add_item.view.*
import java.lang.Exception

class AddAlert: DialogFragment {

    var item:Item?=null
    var flag:Int? = null
    constructor(item: Item?,flag:Int)
    {
        this.item = item
        this.flag = flag
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.add_item,container,false)

        var ac:MainMVP.MainView = (context as MainActivity)
        var presenter:MainMVP.Presenter = MainPresenter(null,context)

        view.ItemName.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(p0!!.length>0)
                {
                    try {
                        ac.hideErrorMessage(ItemInputLayout)
                    }catch (ex:Exception){}
                }
            }

        })

        view.CancelButton.setOnClickListener{

            dismiss()
        }
        if(flag == 0)//add item
        {

            view.AddButton.setOnClickListener{

                if(view.ItemName.text.toString() != "")
                {

                    presenter.addItem(view.ItemName.text.toString())
                    dismiss()
                }
                else
                    ac.showErrorMessage(getString(R.string.requiredText),ItemInputLayout)
            }


        }

        else
        {
            var str = item!!.itemName
            view.ItemName.setText(item!!.itemName)

           view.AddButton.setOnClickListener{
               if(view.ItemName.text.toString() != "")
               {
                   if(str == view.ItemName.text.toString())
                       ac.showMessage(getString(R.string.sameText))
                   else
                   {
                       item!!.itemName = view.ItemName.text.toString()
                       presenter.editItem(item!!)
                       dismiss()
                   }

               }
               else
                   ac.showErrorMessage(getString(R.string.requiredText),ItemInputLayout)
           }

        }
        return view
    }
}