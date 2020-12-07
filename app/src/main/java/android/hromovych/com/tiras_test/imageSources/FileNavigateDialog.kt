package android.hromovych.com.tiras_test.imageSources

import android.hromovych.com.tiras_test.R
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileNavigateDialog : DialogFragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var pathLayout: LinearLayout
    private lateinit var completeBtn: Button

    private lateinit var adapter: FileNavigateAdapter

    private var currentPath: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_file_navigate, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        pathLayout = view.findViewById(R.id.pathLayout)
        completeBtn = view.findViewById(R.id.completeBtn)

        initViews()

        return view
       }

    private fun initViews() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = FileNavigateAdapter(
            listOf(
                Environment.getExternalStorageDirectory(),
                File(System.getenv("SECONDARY_STORAGE")!!)
            )
        )
        adapter.onItemClickAction = { file -> onItemClickListener(file) }
    }

    private fun updateList(items: List<File>) {
        adapter.items = items
        adapter.notifyDataSetChanged()
    }

    private fun onItemClickListener(file: File) {
        updateList(file.listFiles().toList())
        currentPath = file.absolutePath
        updateFolderPath()
    }

    private fun updateFolderPath(){
        pathLayout.removeAllViews()
        currentPath?.split("/")?.forEach { pathPart ->
            val pathPartBtn = Button(context).apply {
                text = pathPart
                setOnClickListener { onItemClickListener(File(currentPath!!.substringBefore(pathPart))) }
                setPadding(1)
            }
            pathLayout.addView(pathPartBtn)
        }
    }

}