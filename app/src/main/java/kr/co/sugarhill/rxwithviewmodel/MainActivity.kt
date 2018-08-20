package kr.co.sugarhill.rxwithviewmodel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.sugarhill.rxwithviewmodel.view.list.ListFragment
import kr.co.sugarhill.rxwithviewmodel.view.list.ListViewModel
import org.koin.android.architecture.ext.viewModel

class MainActivity : AppCompatActivity() {

    private val model: ListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fl_main_container,
                ListFragment()
            )
            .commit()

        et_main_keyword.addTextChangedListener( object : TextWatcher{
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.run {
                    model.setKeyword(this.trim().toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })
    }
}
