package com.example.myapplication.details

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.myapplication.R
import com.example.myapplication.main.MainActivity
import com.example.myapplication.main.MainMVP
import com.example.myapplication.main.MainPresenter
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem
import kotlinx.android.synthetic.main.add_item.*
import kotlinx.android.synthetic.main.add_item.view.*
import kotlinx.android.synthetic.main.add_subitem.*
import kotlinx.android.synthetic.main.add_subitem.view.*
import java.lang.Exception

class DetailsAlert:DialogFragment {

    var item: SubItem?=null
    var flag:Int? = null
    var mainItem:Item
    constructor(item: SubItem?, flag:Int,mainItem:Item)
    {
        this.item = item
        this.flag = flag
        this.mainItem = mainItem
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        var view = inflater.inflate(R.layout.add_subitem,container,false)

        var ac: DetailsMVP.DetailsView = (context as SubItemActivity)
        var presenter: DetailsMVP.Presenter = DetailsPresenter(null,context)

        view.SubItemName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(p0!!.length>0)
                {
                    try {
                        ac.hideErrorMessage(SubItemInputLayout)
                    }catch (ex: Exception){}
                }
            }

        })

        view.SubItemPrice.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if(p0!!.length>0)
                {
                    try {
                        ac.hideErrorMessage(PriceInputLayout)
                    }catch (ex: Exception){}
                }
            }

        })

        view.SubCancelButton.setOnClickListener{

            dismiss()
        }
        if(flag == 0)//add item
        {

            view.SubAddButton.setOnClickListener{

                var f = true

                if(view.SubItemName.text.toString() == "")
                {
                    ac.showErrorMessage(getString(R.string.requiredText),SubItemInputLayout)
                    f = false
                }
                if(view.SubItemPrice.text.toString() == "")
                {
                    ac.showErrorMessage(getString(R.string.requiredPrice),PriceInputLayout)
                    f = false
                }

                if(f)
                {
                    presenter.addSubItem(view.SubItemName.text.toString(),view.SubItemPrice.text.toString().toDouble(),mainItem.itemID)

                    mainItem.totalPrice += view.SubItemPrice.text.toString().toDouble()

                    presenter.updateItem(mainItem)

                    dismiss()
                }

            }


        }

        else
        {
            var str = item!!.subItemName
            var p = item!!.price.toString()
            view.SubItemName.setText(item!!.subItemName)
            view.SubItemPrice.setText(item!!.price.toString())

            view.SubAddButton.setOnClickListener{

                var f = true

                if(view.SubItemName.text.toString() == "")
                {
                    ac.showErrorMessage(getString(R.string.requiredText),SubItemInputLayout)
                    f = false
                }
                if(view.SubItemPrice.text.toString() == "")
                {
                    ac.showErrorMessage(getString(R.string.requiredPrice),PriceInputLayout)
                    f = false
                }

                if(f)
                {
                    if(str == view.SubItemName.text.toString() && p == view.SubItemPrice.text.toString())
                        ac.showMessage(getString(R.string.sameItem))
                    else
                    {
                        item!!.subItemName = view.SubItemName.text.toString()
                        item!!.price = view.SubItemPrice.text.toString().toDouble()
                        presenter.editSubItem(item!!)

                        if(p != view.SubItemPrice.text.toString()) // if price changed
                        {
                            mainItem.totalPrice -= p.toDouble()
                            mainItem.totalPrice += view.SubItemPrice.text.toString().toDouble()
                            presenter.updateItem(mainItem)
                        }
                        dismiss()
                    }

                }

            }

        }
        return view
    }
}