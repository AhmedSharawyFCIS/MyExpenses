package com.example.myapplication.main

import android.app.Activity
import android.content.Context
import android.inputmethodservice.Keyboard
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Item
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),MainMVP.MainView {

    var adapter:MainAdapter?=null
    var Arr:List<Item> = emptyList()
    var presenter:MainMVP.Presenter?=null
    var searchAdapter:SearchAdapter?=null
    var addItemMenu:MenuItem?=null
    var searchFlag = false
    var  ar:List<Item> = emptyList()
    var a:LiveData<List<Item>>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchAdapter = SearchAdapter(this, ArrayList())
        adapter = MainAdapter(this)
        presenter = MainPresenter(this,this)
        recyclerView.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)
        recyclerView.adapter = adapter

        presenter!!.observeMainList()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.additem_menu,menu)

        addItemMenu = menu!!.findItem(R.id.AddItemMenu)

        var menuitem = menu!!.findItem(R.id.SearchItemMenu) as MenuItem

        var autoCompleteSerach = menuitem.actionView.findViewById(R.id.Search) as AutoCompleteTextView

        autoCompleteSerach.setAdapter(searchAdapter)

        autoCompleteSerach.setOnItemClickListener(object:AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var item = p0!!.getItemAtPosition(position) as Item
                ar = emptyList()
                ar = listOf(item)
                adapter!!.updateList(ar)
                searchFlag = true
                a = null
                NoItem.visibility = View.GONE
                var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


                imm.hideSoftInputFromWindow(autoCompleteSerach.applicationWindowToken,0)

                autoCompleteSerach.clearFocus()
            }

        })
        autoCompleteSerach.setOnEditorActionListener(object :TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, actionid: Int, p2: KeyEvent?): Boolean {

                if(actionid == EditorInfo.IME_ACTION_SEARCH)
                {

                    a =  presenter!!.getItems("%${autoCompleteSerach.text.toString()}%")
                    a!!.observe(this@MainActivity, Observer<List<Item>>{

                        adapter!!.updateList(it)
                        if(it.isEmpty())
                            NoItem.visibility = View.VISIBLE
                        else
                            NoItem.visibility = View.GONE
                    })

                    searchFlag = true
                    var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                    imm.hideSoftInputFromWindow(autoCompleteSerach.applicationWindowToken,0)
                    autoCompleteSerach.clearFocus()
                }
                return true
            }

        })
        menuitem.setOnActionExpandListener(object :MenuItem.OnActionExpandListener{
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {

                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {

                var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
                //imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0)
                imm.hideSoftInputFromWindow(autoCompleteSerach.applicationWindowToken,0)

                if(!searchFlag)
                {
                    invalidateOptionsMenu()
                    addItemMenu!!.isVisible = true
                    addItemMenu!!.isEnabled = true
                }

                return true
            }


        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.AddItemMenu)
        {
            var alert = AddAlert(null,0)
            alert!!.show(supportFragmentManager,"Add Item")
            alert!!.isCancelable = false
        }

        if(item.itemId == R.id.SearchItemMenu)
        {
            addItemMenu!!.isVisible = false
            var autoCompleteSerach = item.actionView.findViewById(R.id.Search) as AutoCompleteTextView

            autoCompleteSerach.requestFocus()
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)

        }


        return true
    }
    override fun showMessage(message: String) {

        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    override fun showErrorMessage(message: String, inputfield: TextInputLayout) {

        inputfield.error = message
    }

    override fun hideErrorMessage(inputfield: TextInputLayout) {
        inputfield.error = null
    }

    override fun setArr(arr: LiveData<List<Item>>) {

        arr.observe(this,Observer<List<Item>>{

            if(it.isNotEmpty())
                NoItem.visibility = View.GONE
            else
                NoItem.visibility = View.VISIBLE
            Arr = it

            if(!searchFlag)
            {
                if(a != null)
                {
                    a!!.removeObservers(this)
                    a = null
                }
                adapter!!.updateList(it)
            }
            else if(searchFlag&&a==null)
            {
                var f = false
                for(i in it)
                {
                    if(i.itemID == ar[0].itemID)
                    {
                        ar = emptyList()
                        ar = listOf(i)
                        adapter!!.updateList(ar)
                        f = true
                        break
                    }
                }

                if(!f)
                {
                    NoItem.visibility = View.VISIBLE
                    adapter!!.updateList(emptyList())
                    ar = emptyList()
                }
            }
            searchAdapter!!.updateArr(it )
        })
    }

    override fun onBackPressed() {

        if(searchFlag)
        {
            invalidateOptionsMenu()
            addItemMenu!!.isVisible = true
            searchFlag = false

            if(Arr.isEmpty())
            NoItem.visibility = View.VISIBLE
            else
            NoItem.visibility = View.GONE
            adapter!!.updateList(Arr)
        }
        else
            finish()
    }
}
