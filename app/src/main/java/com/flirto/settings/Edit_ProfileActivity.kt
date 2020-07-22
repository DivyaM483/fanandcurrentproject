package com.flirto.settings

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Edit_ProfileActivity : AppCompatActivity() {
    private lateinit var pager: ViewPager
    private lateinit var professionValueTV: TextView
    private lateinit var fullNamevalueTv: TextView
    private lateinit var genderValueTV: TextView
    private lateinit var ageValueTV: TextView
    private lateinit var sliderDotsLayout: TabLayout
    private lateinit var cameraImage:ImageView
    private  val uid:String="MM7IXQIURuPwNMpOMYfvfccrV3D2"


    val imagesArray: IntArray =
        intArrayOf(R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4)
    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        setupView() //Method which setsup imageslider
        professionValueTV = findViewById(R.id.professionValueTV)
        fullNamevalueTv = findViewById(R.id.FullNameValueTV)
        genderValueTV = findViewById(R.id.GenderValueTV)
        ageValueTV = findViewById(R.id.AgeValueTV)
        cameraImage=findViewById(R.id.ic_camera)
        val professionText: String = professionValueTV.text.toString()
        val fullNameText: String = fullNamevalueTv.text.toString()


        professionValueTV.setOnClickListener(View.OnClickListener {
            openEditDialogbox("Edit Profession", professionValueTV, professionText)
        })
        fullNamevalueTv.setOnClickListener(View.OnClickListener {
            openEditDialogbox("Edit Name", fullNamevalueTv, fullNameText)
        })
        genderValueTV.setOnClickListener {
            val msg: String = "Gender cannot be changed.Please contact us for further assistance"
            openWarningDialogBox(msg)
        }
        ageValueTV.setOnClickListener {
            val msg: String = "Age cannot be changed.Please contact us for further assistance"
            openWarningDialogBox(msg)
        }
        cameraImage.setOnClickListener {
            val intent= Intent(this, AddOrChangeImagesActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setupView() {
        val adapter: PagerAdapter = SliderPagerAdapter(this, imagesArray)
        pager = findViewById(R.id.imagesVP)
        pager.adapter=adapter
        sliderDotsLayout = findViewById(R.id.tabDots)
        sliderDotsLayout.setupWithViewPager(pager, true)
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                println("on page scroll state changed $state")
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                val currentPosition = position + 1
//                pageCountTextView.text = "$currentPosition / ${images.size}"
            }

            override fun onPageSelected(position: Int) {
                println("on page selected $position")
            }

        })

    }

    private fun openEditDialogbox(

        dialogTitle: String,
        textView: TextView?,
        textViewValue: String
    ) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle(dialogTitle)
        val constraintLayout = getEditTextLayout(context, textViewValue)
        builder.setView(constraintLayout)
        val editText = constraintLayout.findViewWithTag<EditText>("textEditTextTag")
        // alert dialog positive button
        builder.setPositiveButton("Submit") { dialog, which ->
            val name = editText.text
            textView?.text = "$name"
        }
        // alert dialog other buttons
//            builder.setNegativeButton("No",null)
        builder.setNeutralButton("Cancel", null)
        builder.setCancelable(false)

        // finally, create the alert dialog and show it
        val dialog = builder.create()

        dialog.show()
        // initially disable the positive button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

        // edit text text change listener
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(
                p0: CharSequence?, p1: Int,
                p2: Int, p3: Int
            ) {
            }

            override fun onTextChanged(
                p0: CharSequence?, p1: Int,
                p2: Int, p3: Int
            ) {
                if (p0.isNullOrBlank()) {
                    editText.error = "Name is required."
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .isEnabled = false
                } else {
//                    editText.error = ""
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .isEnabled = true
                }
            }
        })
    }

    private fun openWarningDialogBox(
        warningMsg: String
    ) {
        val builder = MaterialAlertDialogBuilder(context)
        builder.setTitle("Warning")
        builder.setMessage(warningMsg)
//        val constraintLayout = getTextLayout(context,warningMsg)
//        builder.setView(constraintLayout)
        builder.setPositiveButton("Ok", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun getEditTextLayout(context: Context, professionText: String): ConstraintLayout {
        val constraintLayout = ConstraintLayout(context)
        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        constraintLayout.layoutParams = layoutParams
        constraintLayout.id = View.generateViewId()

        val dialogEditText = EditText(context)

        layoutParams.setMargins(
            32.toDp(context),
            8.toDp(context),
            32.toDp(context),
            8.toDp(context)
        )
        dialogEditText.layoutParams = layoutParams
        dialogEditText.id = View.generateViewId()
        dialogEditText.setText(professionText)
        dialogEditText.tag = "textEditTextTag"
        dialogEditText.inputType = InputType.TYPE_CLASS_TEXT

//        textInputLayout.addView(textInputEditText)

        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintLayout.addView(dialogEditText)
        return constraintLayout
    }

    // extension method to convert pixels to dp
    private fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

    // function which gets the images from firebase
    fun getImagesFromFireBase(){
        val ref: DatabaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid)

    }
}

