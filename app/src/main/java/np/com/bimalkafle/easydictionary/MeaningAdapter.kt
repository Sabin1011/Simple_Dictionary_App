package np.com.bimalkafle.easydictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import np.com.bimalkafle.easydictionary.databinding.MeaningRecyclerRowBinding
import np.com.bimalkafle.easydictionary.model.Meaning

class MeaningAdapter(private var meaningList: List<Meaning>) :
    RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder>() {

    class MeaningViewHolder(private val binding: MeaningRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(meaning: Meaning) {
            binding.partOfSpeechTextview.text = meaning.partOfSpeech

            binding.definitionsTextview.text = meaning.definitions.mapIndexed { index, definition ->
                "${index + 1}. ${definition.definition}"
            }.joinToString("\n\n")

            if (meaning.synonyms.isNullOrEmpty()) {
                binding.synonymsTitleTextview.visibility = View.GONE
                binding.synonymsTextview.visibility = View.GONE
            } else {
                binding.synonymsTitleTextview.visibility = View.VISIBLE
                binding.synonymsTextview.visibility = View.VISIBLE
                binding.synonymsTextview.text = meaning.synonyms.joinToString(", ")
            }

            if (meaning.antonyms.isNullOrEmpty()) {
                binding.antonymsTitleTextview.visibility = View.GONE
                binding.antonymsTextview.visibility = View.GONE
            } else {
                binding.antonymsTitleTextview.visibility = View.VISIBLE
                binding.antonymsTextview.visibility = View.VISIBLE
                binding.antonymsTextview.text = meaning.antonyms.joinToString(", ")
            }
        }
    }

    fun updateNewData(newMeaningList: List<Meaning>) {
        val diffResult = DiffUtil.calculateDiff(MeaningDiffCallback(meaningList, newMeaningList))
        meaningList = newMeaningList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeaningViewHolder {
        val binding = MeaningRecyclerRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MeaningViewHolder(binding)
    }

    override fun getItemCount(): Int = meaningList.size

    override fun onBindViewHolder(holder: MeaningViewHolder, position: Int) {
        holder.bind(meaningList[position])
    }

    class MeaningDiffCallback(
        private val oldList: List<Meaning>,
        private val newList: List<Meaning>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].partOfSpeech == newList[newItemPosition].partOfSpeech
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
