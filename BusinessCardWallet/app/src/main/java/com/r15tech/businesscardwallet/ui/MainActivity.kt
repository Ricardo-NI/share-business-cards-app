package com.r15tech.businesscardwallet.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.r15tech.businesscardwallet.App
import com.r15tech.businesscardwallet.R
import com.r15tech.businesscardwallet.databinding.ActivityMainBinding
import com.r15tech.businesscardwallet.entity.BusinessCard
import com.r15tech.businesscardwallet.ui.AddBusinessCardActivity.Companion.OBJ_CARD
import com.r15tech.businesscardwallet.util.BusinessCardAdapter
import com.r15tech.businesscardwallet.util.CellCardClickListener
import com.r15tech.businesscardwallet.util.CustomAlertDialog
import com.r15tech.businesscardwallet.util.Image
import com.r15tech.businesscardwallet.viewModel.BusinessCardViewModel
import com.r15tech.businesscardwallet.viewModel.MainViewModelFactory


class MainActivity : AppCompatActivity(), CellCardClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var customAlertDialog: CustomAlertDialog
    private val bCardViewModel: BusinessCardViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }
    private val adapter by lazy{ BusinessCardAdapter(this,this)  }

    private var bcList = emptyList<BusinessCard>()
    private var listIsFiltered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        customAlertDialog = CustomAlertDialog(this)

        setupRecyclerView()
        getAllBusinessCard()
        insertListeners()

    }

    private fun setupRecyclerView(){

        binding.recyclerCards.adapter = adapter
        binding.recyclerCards.setHasFixedSize(true)
        binding.recyclerCards.layoutManager = LinearLayoutManager(this)
    }

    private fun getAllBusinessCard(){
        bCardViewModel.getAll().observe(this,{
            bcList = it
            adapter.submitList(it)
        })
    }


    private fun insertListeners(){

        adapter.lintenerShare = {
            Image.share(this@MainActivity, it)
        }

        binding.fabAddCard.setOnClickListener {
            testAndRemoveFilterFromList(false)
            val intent = Intent(this, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }

        binding.btnInfo.setOnClickListener {
            val msg = getString(R.string.msg_info_share)
            customAlertDialog.alertDialogInfo(msg)
        }

        binding.btnSearch.setOnClickListener {
            if(binding.cLayoutSearch.visibility == View.GONE){
                testCardsList()
            }else{
                binding.cLayoutSearch.visibility = View.GONE
                binding.btnSearch.setIconResource(R.drawable.ic_search)
                testAndRemoveFilterFromList(true)
            }
        }

        binding.btnShowAll.setOnClickListener {
            testAndRemoveFilterFromList(false)
        }

        binding.txInputLayoutSearch.setEndIconOnClickListener {
            testInputAndFilter()
        }

        binding.txInputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                testInputAndFilter()
                true
            } else {
                false
            }
        }
    }

    private fun testInputAndFilter(){

        val name = binding.txInputSearch.text.toString()
        if(name.isNotBlank()){

            val filteredBcList : MutableList<BusinessCard> = mutableListOf()

            for(bCard in bcList){
                if(bCard.name.contains(name)){
                    filteredBcList.add(bCard)
                }
            }
            if(filteredBcList.size > 0) {
                hideKeyboard()
                binding.btnShowAll.visibility = View.VISIBLE
                adapter.submitList(filteredBcList)
                listIsFiltered = true
            }else{
                val msg = getString(R.string.msg_info_search)
                customAlertDialog.alertDialogInfo(msg)
            }

        }else{
            binding.txInputSearch.error = getString(R.string.msg_error_search)
            binding.txInputSearch.requestFocus()
        }
    }


    private fun testCardsList(){

        if (adapter.itemCount == 0) {

            customAlertDialog.alertDialogInfo(getString(R.string.search_card_msg))
        } else {

            binding.btnSearch.setIconResource(R.drawable.ic_search_off)
            binding.cLayoutSearch.visibility = View.VISIBLE
            binding.txInputSearch.text?.clear()

        }
    }

    private fun hideKeyboard() {

            val view = this.currentFocus
            if (view != null) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

    }

    private fun delete(businessCard: BusinessCard) {
        val builder = AlertDialog.Builder(this)
        builder.setPositiveButton(R.string.delete) { _, _ ->

            bCardViewModel.deleteData(businessCard)
            testAndRemoveFilterFromList(false)
        }
        builder.setNegativeButton(R.string.cancel) { _, _ -> }

        builder.setTitle(getString(R.string.delete_card))
        val msg = getString(R.string.confirm_delete) + " " + businessCard.name + "?"
        builder.setMessage(msg)
        builder.create().show()
    }

    private fun testAndRemoveFilterFromList(adjustComponents: Boolean){
        if(listIsFiltered || adjustComponents) {
            binding.txInputSearch.text?.clear()
            binding.cLayoutSearch.visibility = View.GONE
            binding.btnSearch.setIconResource(R.drawable.ic_search)
            adapter.submitList(bcList)
            listIsFiltered = false
        }
    }

    override fun onEditCellClickListener(businessCard: BusinessCard, toDelete: Boolean) {

        if(toDelete){
            delete(businessCard)
        }else{
            testAndRemoveFilterFromList(false)
            val intent = Intent(this, AddBusinessCardActivity::class.java)
            intent.putExtra(OBJ_CARD,businessCard)
            startActivity(intent)
        }
    }
}