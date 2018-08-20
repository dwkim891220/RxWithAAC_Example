package kr.co.sugarhill.rxwithviewmodel.view.list

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_franchise.view.*
import kr.co.sugarhill.rxwithviewmodel.network.FranchiseBrand
import kr.co.sugarhill.rxwithviewmodel.util.recyclerview.AndroidExtensionsViewHolder

class FranchiseBrandHolder(
    private val context: Context, view: View
): AndroidExtensionsViewHolder(view) {
    fun updateUI(item: FranchiseBrand, keyword: String?){
        Glide.with(context)
            .load(item.logoPhotoUrl)
            .apply(
                RequestOptions.circleCropTransform()
            )
            .into(itemView.iv_itemFranchise)

        itemView.tv_itemFranchise_brand.text = item.brandName
        itemView.tv_itemFranchise_headQuater.text = item.headquartersName

        highlightKeyword(keyword)
    }

    private fun highlightKeyword(keyword: String?){
        keyword?.let {
            setHighLightTextView(itemView.tv_itemFranchise_brand, it)
            setHighLightTextView(itemView.tv_itemFranchise_headQuater, it)
        }
    }

    private fun setHighLightTextView(tv: TextView, keyword: String) {
        val tvTitleText = tv.text.toString()

        val startIndex = tvTitleText.indexOf(keyword)
        if (startIndex > -1 && (startIndex + keyword.length) > 0) {
            tv.text =
                    SpannableStringBuilder(tvTitleText).apply {
                        setSpan(
                            ForegroundColorSpan(Color.parseColor("#FF0000")),
                            startIndex,
                            startIndex + keyword.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            startIndex,
                            startIndex + keyword.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
        }
    }
}