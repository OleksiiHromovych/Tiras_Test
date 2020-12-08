package android.hromovych.com.tiras_test.imageSources

import android.content.Context
import android.graphics.BitmapFactory
import android.hromovych.com.tiras_test.R
import android.hromovych.com.tiras_test.extentions.isImage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileNavigateAdapter(val context: Context, var items: List<File>) :
    RecyclerView.Adapter<FileNavigateAdapter.FileNavigateHolder>() {

    var onItemClickAction: ((File) -> Unit)? = null

    inner class FileNavigateHolder(val view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var file: File
        private val textView = view.findViewById<TextView>(R.id.textView)
        private val imageView = view.findViewById<ImageView>(R.id.imageView)

        init {
            view.setOnClickListener(this)
        }

        fun bind(s: File) {
            file = s
            textView.text = file.name
            when {
                file.isImage() -> imageView.setImageBitmap(
                    BitmapFactory.decodeFile(file.absolutePath)
                )
                file.isDirectory -> imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        view.context,
                        R.drawable.ic_folder
                    )
                )
                else -> imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        view.context,
                        R.drawable.ic_file
                    )
                )
            }
        }

        override fun onClick(v: View?) {
            onItemClickAction?.invoke(file)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileNavigateHolder {
        return FileNavigateHolder(
            LayoutInflater.from(context).inflate(R.layout.item_file_navigate, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FileNavigateHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

}