package com.kato.scopedstorageapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kato.scopedstorageapp.databinding.ActivityFileBinding
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class FileActivity : AppCompatActivity() {
    companion object {
        private const val CREATE_FILE_REQUEST_CODE = 1
    }

    lateinit var bindingFile: ActivityFileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingFile=ActivityFileBinding.inflate(layoutInflater)
        val view=bindingFile.root
        setContentView(view)

        bindingFile.btnSave.setOnClickListener {
            createFile()
        }
    }

    private fun createFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TITLE, "${bindingFile.editFileName.text}.txt")
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE)
    }


    private fun writeFileContent(uri: Uri?) {
        try {
            val file = uri?.let { this.contentResolver.openFileDescriptor(it, "w") }

            file?.let {
                val fileOutputStream = FileOutputStream(
                    it.fileDescriptor
                )
                val textContent = bindingFile.editContent.text.toString()

                fileOutputStream.write(textContent.toByteArray())

                fileOutputStream.close()
                it.close()
            }

        } catch (e: FileNotFoundException) {
            //print logs
        } catch (e: IOException) {
            //print logs
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Write the file content
        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                writeFileContent(data.data)

            }

        }
    }
}