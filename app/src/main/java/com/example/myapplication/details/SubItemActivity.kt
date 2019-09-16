package com.example.myapplication.details

import android.content.Context
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
import com.example.myapplication.main.*
import com.example.myapplication.model.Item
import com.example.myapplication.model.SubItem
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sub_item.*

class SubItemActivity : AppCompatActivity() , DetailsMVP.DetailsView{

    var adapter: DetailsAdapter?=null
    var Arr:List<SubItem> = emptyList()
    var presenter: DetailsMVP.Presenter?=null
    var b:Item?=null

    var searchAdapter: SearchAdapter2?=null
    var addItemMenu:MenuItem?=null
    var searchFlag = false
    var  ar:List<SubItem> = emptyList()
    var a:LiveData<List<SubItem>>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_item)

         b = intent.extras!!.getSerializable("MainItem") as Item
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.title = b!!.itemName

        searchAdapter = SearchAdapter2(this, ArrayList())
        adapter = DetailsAdapter(this,b!!)
        presenter = DetailsPresenter(this,this)
        recyclerView2.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        recyclerView2.adapter = adapter

        presenter!!.observeSubList(b!!.itemID)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {


        menuInflater.inflate(R.menu.additem_menu,menu)
        addItemMenu = menu!!.findItem(R.id.AddItemMenu)

        var menuitem = menu!!.findItem(R.id.SearchItemMenu) as MenuItem

        var autoCompleteSerach = menuitem.actionView.findViewById(R.id.Search) as AutoCompleteTextView

        autoCompleteSerach.setAdapter(searchAdapter)

        autoCompleteSerach.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                var subitem = p0!!.getItemAtPosition(position) as SubItem
                ar = emptyList()
                ar = listOf(subitem)
                adapter!!.updateList(ar)
                searchFlag = true
                a = null
                NoItem2.visibility = View.GONE
                var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


                imm.hideSoftInputFromWindow(autoCompleteSerach.applicationWindowToken,0)

                autoCompleteSerach.clearFocus()
            }

        })
        autoCompleteSerach.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, actionid: Int, p2: KeyEvent?): Boolean {

                if(actionid == EditorInfo.IME_ACTION_SEARCH)
                {
                    a =  presenter!!.getSubItems("%${autoCompleteSerach.text.toString()}%")
                    a!!.observe(this@SubItemActivity, Observer<List<SubItem>>{

                        adapter!!.updateList(it)
                        if(it.isEmpty())
                            NoItem2.visibility = View.VISIBLE
                        else
                            NoItem2.visibility = View.GONE
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
            var alert = DetailsAlert(null,0,b!! )
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

    override fun setArr(arr: LiveData<List<SubItem>>) {
        arr.observe(this, Observer<List<SubItem>>{

            if(it.isNotEmpty())
                NoItem2.visibility = View.GONE
            else
                NoItem2.visibility = View.VISIBLE
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
                    if(i.subItemID == ar[0].subItemID)
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
                    NoItem2.visibility = View.VISIBLE
                    adapter!!.updateList(emptyList())//when remove single item search
                    ar = emptyList()
                }
            }
            searchAdapter!!.updateArr(it)
        })
    }

    override fun onBackPressed() {

        if(searchFlag)
        {
            invalidateOptionsMenu()
            addItemMenu!!.isVisible = true
            searchFlag = false

            if(Arr.isEmpty())
                NoItem2.visibility = View.VISIBLE
            else
                NoItem2.visibility = View.GONE
            adapter!!.updateList(Arr)
        }
        else
            finish()
    }
}
