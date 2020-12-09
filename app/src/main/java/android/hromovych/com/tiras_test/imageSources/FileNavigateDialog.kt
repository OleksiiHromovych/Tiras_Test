package android.hromovych.com.tiras_test.imageSources

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.hromovych.com.tiras_test.R
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageVolume
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileNavigateDialog(val completeBtnAction: (String, Boolean) -> Unit) : DialogFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var completeBtn: Button
    private lateinit var backBtn: Button

    private lateinit var adapter: FileNavigateAdapter
    private lateinit var currentFile: File

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_file_navigate, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        completeBtn = view.findViewById(R.id.completeBtn)
        backBtn = view.findViewById(R.id.backButton)

        currentFile = Environment.getExternalStorageDirectory().absoluteFile
        initViews()

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.Theme_Dialog)
    }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FileNavigateAdapter(
            requireContext(),
            getStartFileList()
        )
        adapter.onItemClickAction = { file -> onItemClickListener(file) }
        recyclerView.adapter = adapter

        backBtn.setOnClickListener {
            currentFile.parentFile?.let { onItemClickListener(it) }
        }
        completeBtn.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Search photo in nested folders?")
                setCancelable(false)
                setPositiveButton("Yes") { _: DialogInterface, _: Int ->
                    completeBtnAction(currentFile.absolutePath, true)
                    dismiss()
                }
                setNegativeButton("No") { _, _ ->
                    completeBtnAction(currentFile.absolutePath, false)
                    dismiss()
                }
            }
                .create()
                .show()
        }
    }


    private fun getStartFileList() = mutableListOf(
        Environment.getRootDirectory().absoluteFile,
    ).apply {
        getExternalCardDirectory(true)?.let { add(it) }
        getExternalCardDirectory(false)?.let { add(it) }

    }.toList()


    private fun updateList(items: List<File>) {
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    private fun onItemClickListener(file: File) {
        if (file.isDirectory && file.listFiles() != null) {
            updateList(file.listFiles().toList())
            currentFile = file
            backBtn.text = file.name
        } else {
            updateList(
                getStartFileList()
            )
            backBtn.text = ""
        }
    }

    // For get sdcard or storage file.
    private fun getExternalCardDirectory(is_removable: Boolean): File? {
        val storageManager = requireContext().getSystemService(Context.STORAGE_SERVICE)
        try {
            val storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList = storageManager.javaClass.getMethod("getVolumeList")
            val getPath = storageVolumeClazz.getMethod("getPath")
            val isRemovable = storageVolumeClazz.getMethod("isRemovable")
            val result = getVolumeList.invoke(storageManager) as Array<StorageVolume>
            result.forEach {
                if (isRemovable.invoke(it) as Boolean == is_removable) {
                    return File(getPath.invoke(it) as String)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }
}