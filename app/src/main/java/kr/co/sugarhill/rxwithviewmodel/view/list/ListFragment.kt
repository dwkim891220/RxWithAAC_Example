package kr.co.sugarhill.rxwithviewmodel.view.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import kr.co.sugarhill.rxwithviewmodel.R
import kr.co.sugarhill.rxwithviewmodel.util.ext.visibilityBoolean
import org.koin.android.architecture.ext.viewModel

class ListFragment : Fragment() {
    val TAG = javaClass.simpleName

    private val model: ListViewModel by viewModel()
    private lateinit var listAdapter : ListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i(TAG, "myModel : $model")

        model.searchKeyword.observe(this, android.arch.lifecycle.Observer { keywordModel ->
            keywordModel?.run {
                model.getFranchiseBrands(this)
            }
        })

        model.searchResult.observe(this, android.arch.lifecycle.Observer { result ->
            result?.run {
                if(this.list.isEmpty()){
                    showList(false)
                }else{
                    showList(true)
                    listAdapter.list = this.list
                }
            }
        })

        listAdapter = ListAdapter()
        rv_fList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = listAdapter
        }
    }

    private fun showList(show : Boolean){
        rv_fList.visibilityBoolean(show)
        rl_fList.visibilityBoolean(!show)
    }
}