import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.story.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ItemStoryBinding

class StoryAdapter: PagingDataAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val review = getItem(position)
        review?.let { holder.bind(it) }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(review) }
    }

    class MyViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            binding.tvTitleStory.text = story.name
            binding.tvDeskripsi.text = story.description?.let {
                if (it.length > 20) "${it.substring(0, 20)}..." else it
            }
            Glide.with(binding.root)
                .load(story.photoUrl)
                .into(binding.ivPhoto)
        }

    }
    interface OnItemClickCallback {
        fun onItemClicked(item: ListStoryItem?)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}