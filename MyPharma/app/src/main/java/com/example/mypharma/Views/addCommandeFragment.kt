package com.example.mypharma.Views


import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mypharma.BuildConfig
import com.example.mypharma.Controllers.CommandesController
import com.example.mypharma.Models.Commande

import com.example.mypharma.R
import com.example.mypharma.Retrofit.RetrofitService
import com.example.mypharma.baseUrl
import kotlinx.android.synthetic.main.fragment_add_commande.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.support.v4.defaultSharedPreferences
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class addCommandeFragment : Fragment() {
    private val mMediaUri: Uri? = null
    private var filename: String? =null
    private var fileUri: Uri? = null

    private var mediaPath: String? = null

    private var mImageFileLocation = ""
    private var postPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_commande, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (shouldAskPermissions()) {
            askPermissions()
        }
        upload.setOnClickListener { view->
           val alertDialog= AlertDialog.Builder(activity!!)
            alertDialog.setTitle("Choisir une photo")
            val items= arrayOf("A partir de la galerie", "Prendre une photo")
            alertDialog.setItems(items){
                dialog, which ->
                    when (which){
                        0->pickFromGallery()
                        1->captureImage()
                    }
            }
            alertDialog.show()
        }

        envoyer.setOnClickListener { view->
            progress.visibility=View.VISIBLE
            uploadFile()

        }
    }
    private fun uploadFile(){
        if (postPath == null || postPath == "") {
            toast("veuillez choisir une photo")
            return
        }
        else {

            // Map is used to multipart the file using okhttp3.RequestBody
            val map = HashMap<String, RequestBody>()
            val file = File(postPath!!)

            // Parsing any Media type file
            val requestBody = RequestBody.create(MediaType.parse("image/*"), file) 
            val name = RequestBody.create(MediaType.parse("text/plain"), "upload");

            map.put("file\"; filename=\"" + file.name + "\"", requestBody)
            val multipartBody= MultipartBody.Part.createFormData("photo",file.name,requestBody)
            val call = RetrofitService.endpoint.uploadPhoto(multipartBody)
            call.enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val serverResponse = response.body()
                             filename = serverResponse!!
                            val pref= activity!!.getSharedPreferences("fileName",MODE_PRIVATE)
                            val user_id= pref.getInt("nss",0)
                            val sdf = SimpleDateFormat("dd/M/yyyy")
                            val date = sdf.format(Date()).toString()
                            var pharmacie_id= arguments?.getInt("pharmacie_id")
                            var etat= "en attente"
                            var cmd_id=0
                            val path= "/uploads/images/"+filename
                            val call = RetrofitService.endpoint.getNbCommandes()
                            call.enqueue(object : Callback<Int> {
                                override fun onFailure(call: Call<Int>, t: Throwable) {
                                    toast(t.toString())
                                }

                                override fun onResponse(call: Call<Int>?, response: Response<Int>?) {
                                    if (response?.isSuccessful!!) {
                                        cmd_id = response?.body()!!.toInt()+1
                                        val cmd= Commande(cmd_id,path,user_id,pharmacie_id!!,etat,date,0)
                                        val cmdctrl= CommandesController()
                                        cmdctrl.addCommande(view!!,activity!! as Activity,cmd)
                                    } else {
                                    }

                                }
                            })

                        }
                    } else {
                        toast("erreur")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    toast("Response gotten is"+ t.toString())
                }
            })
        }
    }


    private fun captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            val callCameraApplicationIntent = Intent()
            callCameraApplicationIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

            // We give some instruction to the intent to save the image
            var photoFile: File? = null

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile()
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (e: IOException) {
                e.printStackTrace()
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            val outputUri = FileProvider.getUriForFile(
                activity!!,
                "com.example.mypharma.fileprovider",
                photoFile!!)
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)


            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST)
        } else {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE)

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST)
        }


    }
    fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    @Throws(IOException::class)
    internal fun createImageFile(): File {

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmSS").format(Date())
        val imageFileName = "IMAGE_" + timeStamp
        // Here we specify the environment location and the exact path where we want to save the so-created file
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app")

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir()

        // Here we create the file using a prefix, a suffix and a directory
        val image = File(storageDirectory, imageFileName + ".jpg")
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation

        mImageFileLocation = image.absolutePath
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image
    }

    private fun pickFromGallery(){
        val intent : Intent= Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        startActivityForResult(intent, REQUEST_PICK_PHOTO)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode==Activity.RESULT_OK){
                if ((requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO)){
                    val selectedImage = data!!.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val act=activity
                    val cursor = act!!.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    // Set the Image in ImageView for Previewing the Media
                    photo.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()


                    postPath = mediaPath
                   // photo.setImageBitmap(bitmap)

                }
                else if (requestCode == CAMERA_PIC_REQUEST) {
                    if (Build.VERSION.SDK_INT > 21) {

                        Glide.with(this).load(mImageFileLocation).into(photo)
                        postPath = mImageFileLocation

                    } else {
                        Glide.with(this).load(fileUri).into(photo)
                        postPath = fileUri!!.path

                    }

                }

        }
    }
    protected fun  shouldAskPermissions(): Boolean {
    return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
    }
    @TargetApi(23)
    protected fun askPermissions() {
    val permissions = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA"
    )
    val requestCode = 200
    requestPermissions(permissions, requestCode)
}
    companion object {
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_PICK_PHOTO = 2
        private val CAMERA_PIC_REQUEST = 1111

        private val TAG = Main2Activity::class.java.getSimpleName()

        private val CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100

        val MEDIA_TYPE_IMAGE = 1
        val IMAGE_DIRECTORY_NAME = "Android File Upload"

        /**
         * returning image / video
         */
        private fun getOutputMediaFile(type: Int): File? {

            // External sdcard location
            val mediaStorageDir = File(
                Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME)

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {

                    return null
                }
            }

            // Create a media file name
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(Date())
            val mediaFile: File
            if (type == MEDIA_TYPE_IMAGE) {
                mediaFile = File(mediaStorageDir.path + File.separator
                        + "IMG_" + ".jpg")
            } else {
                return null
            }

            return mediaFile
        }
    }




}


