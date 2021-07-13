package com.r15tech.businesscardwallet.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.r15tech.businesscardwallet.App
import com.r15tech.businesscardwallet.R
import com.r15tech.businesscardwallet.entity.BusinessCard
import com.r15tech.businesscardwallet.databinding.ActivityAddBusinessCardBinding
import com.r15tech.businesscardwallet.entity.ColorItem
import com.r15tech.businesscardwallet.util.CardColorAdapter
import com.r15tech.businesscardwallet.util.CellColorClickListener
import com.r15tech.businesscardwallet.util.CustomAlertDialog
import com.r15tech.businesscardwallet.util.Mask
import com.r15tech.businesscardwallet.viewModel.BusinessCardViewModel
import com.r15tech.businesscardwallet.viewModel.MainViewModelFactory


class AddBusinessCardActivity : AppCompatActivity(),CellColorClickListener {

    companion object {
        const val OBJ_CARD = "objcard"
        private const val PHONE_MASK = "(##) ##### ####"
    }

    private lateinit var binding: ActivityAddBusinessCardBinding
    private lateinit var adapter: CardColorAdapter
    private lateinit var customAlertDialog: CustomAlertDialog

    private val bCardViewModel: BusinessCardViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    private val backColorsArray = arrayOf(
        "#1EB980",
        "#2962FF",
        "#E65100",
        "#FF0266",
        "#FFB303",
        "#2F4562",
        "#D7D7D7",
        "#FFFFFF",
        "#000000"
    )
    private val textColorsArray = arrayOf(
        "#00C853",
        "#05B5DF",
        "#FF5722",
        "#FF4081",
        "#FFDE03",
        "#FF1744",
        "#D7D7D7",
        "#FFFFFF",
        "#000000"
    )
    private var cardBackColor = "#D7D7D7"
    private var cardTextColor = "#000000"
    private var backColorPosition = 6
    private var textColorPosition = 8

    private var backgroundColorsSelected = true
    private var objListColorsBack = ArrayList<ColorItem>()
    private var objListColorsText = ArrayList<ColorItem>()
    private var businessCardId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBusinessCardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        customAlertDialog = CustomAlertDialog(this)

        setupRecyclerView()

        val bundle: Bundle? = intent.extras
        bundle?.let {
            bundle.apply {

                //Parcelable Data
                 val businessCard: BusinessCard? = getParcelable(OBJ_CARD)
                 if (businessCard != null) {
                     fillFields(businessCard)
                 }
            }
        }
        
        insertListeners()
    }

    private fun setupRecyclerView(){

        for(i in backColorsArray.indices){
            val color = ColorItem(i + 1, backColorsArray[i])
            objListColorsBack.add(color)
        }

        for(i in textColorsArray.indices){
            val color = ColorItem(i + 1, textColorsArray[i])
            objListColorsText.add(color)
        }

        adapter = CardColorAdapter(this)
        binding.recyclerColors.adapter = adapter
        binding.recyclerColors.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerColors.layoutManager = linearLayoutManager
        binding.recyclerColors.itemAnimator = DefaultItemAnimator()
        adapter.setData(objListColorsBack)

    }

    private fun insertListeners(){

        if(businessCardId == -1) {
            binding.txInputName.requestFocus() //abrir o teclado junto com a Activity.
        }
        binding.txInputPhone.addTextChangedListener(Mask.mask(PHONE_MASK, binding.txInputPhone))

        binding.btnConfirm.setOnClickListener {
            saveOrUpdateCard(inputCheckIsValid())
        }

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        binding.toggleButtonColors.addOnButtonCheckedListener { _, checkedId, _ ->

            when (checkedId) {
                R.id.button_back_color -> {
                    if(!backgroundColorsSelected){
                        backgroundColorsSelected = true
                        adapter.setData(objListColorsBack)
                    }
                }
                else -> {
                    if(backgroundColorsSelected){
                        backgroundColorsSelected = false
                        adapter.setData(objListColorsText)

                    }
                }
            }
        }
    }
   

    private fun saveOrUpdateCard(testInput: Boolean){

        hideKeyboard()

        if(testInput){

            try {

                val name = binding.txInputName.text.toString()
                val company = binding.txInputCompany.text.toString()
                val phone = binding.txInputPhone.text.toString()
                val email = binding.txInputEmail.text.toString()

                if (businessCardId == -1) {
                    val businessCard = BusinessCard(
                        name = name,
                        company = company,
                        phone = phone,
                        email = email,
                        backColor = cardBackColor,
                        textColor = cardTextColor
                    )
                    bCardViewModel.insert(businessCard)
                    Toast.makeText(this,R.string.register_success, Toast.LENGTH_SHORT).show()
                } else {
                    val businessCard = BusinessCard(
                        id = businessCardId,
                        name = name,
                        company = company,
                        phone = phone,
                        email = email,
                        backColor = cardBackColor,
                        textColor = cardTextColor
                    )
                    bCardViewModel.updateData(businessCard)
                    Toast.makeText(this,R.string.update_success, Toast.LENGTH_SHORT).show()
                }
                finish()

            }catch (e: Exception){
                val msg = getString(R.string.msg_error_save) + " : " + e.message
                customAlertDialog.alertDialog1(msg)
            }
        }
    }

    //verificar se os campos foram preenchidos
    private fun inputCheckIsValid(): Boolean{

        var isValid = true

        if(binding.txInputName.text.isNullOrBlank()){
            val error = getString(R.string.msg_error_input_name)
            binding.txInputName.error = error
            binding.txInputName.requestFocus()
            isValid = false
        }

        if(isValid){
            if(binding.txInputCompany.text.isNullOrBlank()){
                val error = getString(R.string.msg_error_input_company)
                binding.txInputCompany.error = error
                binding.txInputCompany.requestFocus()
                isValid = false
            }
        }
        if(isValid){
            var error = ""
            if(binding.txInputPhone.text.isNullOrBlank()){
                error = getString(R.string.msg_error_input_phone)
                isValid = false
            }else{
                var phoneNumber = binding.txInputPhone.text.toString()
                phoneNumber = Mask.replaceChars(phoneNumber)
                if(phoneNumber.length < 10){
                    error = getString(R.string.msg_error_invalid_phone)
                    isValid = false
                }
            }
            if(!isValid){
                binding.txInputPhone.error = error
                binding.txInputPhone.requestFocus()
            }
        }
        if(isValid){
            var error = ""
            if(binding.txInputEmail.text.isNullOrBlank()){
                error = getString(R.string.msg_error_input_email)
                isValid = false
            }else{
                val email = binding.txInputEmail.text.toString()
                isValid =  android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                error = getString(R.string.msg_error_invalid_email)
            }
            if(!isValid){
                binding.txInputEmail.error = error
                binding.txInputEmail.requestFocus()
            }
        }
        if(isValid){//teste para evitar salvar cartões com cor de texto e fundo parecidas.
            if(backColorPosition == textColorPosition && backColorPosition != 5){

                isValid = false
                customAlertDialog.alertDialogSimilarColors { saveOrUpdateCard(true) }
            }
        }
        return isValid
    }

    private fun fillFields(businessCard: BusinessCard){

        businessCardId = businessCard.id
        binding.btnConfirm.text = getString(R.string.update)
        binding.txvTitle.text = getString(R.string.edit_card)

        binding.txInputName.setText(businessCard.name)
        binding.txInputCompany.setText(businessCard.company)
        binding.txInputPhone.setText(businessCard.phone)
        binding.txInputEmail.setText(businessCard.email)
        cardBackColor = businessCard.backColor
        cardTextColor = businessCard.textColor
        binding.cardviewPreview.setCardBackgroundColor(Color.parseColor(cardBackColor))
        binding.textPreview.setTextColor(Color.parseColor(cardTextColor))
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun onCellClickListener(position: Int) {

        try {

            if (backgroundColorsSelected) {
                backColorPosition = position
                cardBackColor = backColorsArray[position]
                binding.cardviewPreview.setCardBackgroundColor(Color.parseColor(cardBackColor))
            } else {
                textColorPosition = position
                cardTextColor = textColorsArray[position]
                binding.textPreview.setTextColor(Color.parseColor(cardTextColor))
            }

        }catch (ex: ArrayIndexOutOfBoundsException)   {
            val msg = getString(R.string.msg_error_loading) + " : " + ex.message
            customAlertDialog.alertDialog1(msg)
        }
    }
}
