package com.flirto.settings


import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.flirto.settings.R.*


class AddOrChangeImagesActivity : AppCompatActivity() {
    private lateinit var imagesArray: IntArray

    //    private var myBitmap: Bitmap? = null
    private lateinit var myBitmap: Bitmap
    private lateinit var myBitmapDrawable: BitmapDrawable
    private var picUri: Uri? = null


    private lateinit var permissionsToRequest: ArrayList<String>
    private val permissionsRejected: ArrayList<String> = ArrayList()
    private val permissions: ArrayList<String> = ArrayList()

    private val allPermissionsRequired = 107

    private lateinit var cancelAddImageView2: ImageView
    private lateinit var cancelAddImageView3: ImageView
    private lateinit var imgView2:ImageView
    var selectPhoto:Int=1
    private lateinit var selectedImageView: ImageView


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.add_change_images)
        cancelAddImageView2=findViewById(id.cancel_Add_ImageView2)
        cancelAddImageView3=findViewById(id.cancel_Add_ImageView3)
        imgView2=findViewById(id.imgView2)
        onCreateLoadPhotosIntoImageViews()


        cancelAddImageView2.setOnClickListener({
            if (cancelAddImageView2.getDrawable().getConstantState() == getResources().getDrawable(
                    drawable.ic_add_circle,null).getConstantState()) {

                Log.i("AddOrChangesActivity", "Add Image Clicked")
                openImageChooser(imgView2)
                Log.i("AddOrChangesActivity", "Image Selected")
            }
            else{
                Log.i("AddOrChangesActivity", "Cancel image cliked")
                cancelAddImageView2.setImageResource(drawable.ic_add_circle)

                cancelAddImageView3.setImageResource(0)
                imgView2.setImageBitmap(null)
//imgView2.background=null

            }

        })


//        {
//
//
//            permissions.add(CAMERA)
//            permissionsToRequest = findUnAskedPermissions(permissions)
//            //get the permissions we have asked for before but are not granted..
//            //we will store this in a global list to access later.
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//           //     Log.i("AddOrChangesActivity","Permissions size"+permissionsToRequest.size)
//                if (permissionsToRequest!!.size > 0)
//                    requestPermissions(permissionsToRequest.toTypedArray(),allPermissionsRequired)
//            }
//        }
    }
    private fun openImageChooser(imageView: ImageView) {
        var intent:Intent= Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,selectPhoto)
        selectedImageView=imageView
        cancelAddImageView2.setImageResource(drawable.ic_cancel)
        cancelAddImageView3.setImageResource(drawable.ic_add_circle)
        Log.i("AddOrChangesActivity","Image Selected2")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.i("OnActivityResult","Clicked1")
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==selectPhoto && resultCode==Activity.RESULT_OK && data!=null)
        {
            picUri= data.data
            myBitmap=MediaStore.Images.Media.getBitmap(contentResolver,picUri)
            myBitmapDrawable=BitmapDrawable(myBitmap)
            selectedImageView.setBackgroundDrawable(myBitmapDrawable)

        }
    }

    private  fun findUnAskedPermissions(wanted: ArrayList<String>):ArrayList<String>{
        val result = ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }
    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) === PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }
    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }
    @TargetApi(Build.VERSION_CODES.M)
    override  fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            allPermissionsRequired-> {
                for (perms in permissionsToRequest!!) {
                    if (hasPermission(perms)) {
                    } else {
                        permissionsRejected.add(perms)
                    }
                }
                if (permissionsRejected.size > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected[0])) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                DialogInterface.OnClickListener { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                        //Log.d("API123", "permisionrejected " + permissionsRejected.size());
                                        requestPermissions(
                                            permissionsRejected.toTypedArray(),
                                            allPermissionsRequired
                                        )
                                    }
                                })
                            return
                        }
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(
        message: String,
        okListener: DialogInterface.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
    private fun onCreateLoadPhotosIntoImageViews() {
        imagesArray = intArrayOf(
            drawable.img_1
        )    //imagesarray it can be dynamic

        val count =
            imagesArray.size                                                //size of imagearray
        var imageviewlayout: ImageView
        if (count == 1)                                                         //if user has uploaded only one image
        {
            imageviewlayout = findViewById(id.defaultImgView1)
            imageviewlayout.setImageResource(imagesArray[0])

            imageviewlayout =
                findViewById(id.cancel_Add_ImageView2)           //just empty container for second image
            imageviewlayout.setImageResource(drawable.ic_add_circle)          //assigning add icon to empty container allowing user to add next image
        } else                                                                     //to assign images with respective Image view containers,if user has uploaded more than one image
        {
            for (item in imagesArray.withIndex()) {
                var resID: Int
                if (item.index == 0) {
                    imageviewlayout = findViewById(id.defaultImgView1)
                    imageviewlayout.setImageResource(item.value)

                    resID = resources.getIdentifier(
                        "cancel_Add_ImageView" + (item.index + 2),
                        "id",
                        packageName
                    )
                    imageviewlayout = findViewById(resID)
                    imageviewlayout.setImageResource(drawable.ic_add_circle)
                }
                //this is to assign image and cancel icon to respective image view and also set add icon to next ImageView
                else if (item.index != 0 && item.index < 5) //here less than taken because we dont need to set add button for next image view for the last index
                {
                    resID = resources.getIdentifier("imgView" + (item.index + 1), "id", packageName)
                    imageviewlayout = findViewById(resID)
                    imageviewlayout.setImageResource(item.value)

                    resID = resources.getIdentifier(
                        "cancel_Add_ImageView" + (item.index + 1),
                        "id",
                        packageName
                    )

                    imageviewlayout = findViewById(resID)
                    imageviewlayout.setImageResource(drawable.ic_cancel)

                    resID = resources.getIdentifier(
                        "cancel_Add_ImageView" + (item.index + 2),
                        "id",
                        packageName
                    )
                    imageviewlayout = findViewById(resID)
                    imageviewlayout.setImageResource(drawable.ic_add_circle)
                } else                                                         //for the last index adding only Image and cancel button
                {
                    var resId =
                        resources.getIdentifier("imgView" + (item.index + 1), "id", packageName)
                    imageviewlayout = findViewById(resId)
                    imageviewlayout.setImageResource(item.value)

                    resId = resources.getIdentifier(
                        "cancel_Add_ImageView" + (item.index + 1),
                        "id",
                        packageName
                    )
                    imageviewlayout = findViewById(resId)
                    imageviewlayout.setImageResource(drawable.ic_cancel)
                }

            }
        }

    }





}



