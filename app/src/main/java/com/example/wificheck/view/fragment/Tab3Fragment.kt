package com.example.wificheck.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Switch
import com.example.wificheck.presenter.fragment.Tab3PresenterImpl
import com.example.wificheck.R
import kotlinx.android.synthetic.main.tab3_settings_fragment.view.*

class Tab3Fragment : Fragment(), Tab3View {


    lateinit var mPresenter: Tab3PresenterImpl
    lateinit var mView: View

    lateinit var rbNotification: RadioButton
    lateinit var rbAutomatic: RadioButton
    lateinit var rbBoth: RadioButton
    lateinit var sEntering: Switch
    lateinit var sLeaving: Switch
    var tabletView: Boolean = false

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val TABLET_VIEW = "item_id"
    }



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab3_settings_fragment, container, false)

        mView = view
        mPresenter = Tab3PresenterImpl(this, view.context)
        rbNotification = view.rb_notification
        rbAutomatic = view.rb_automatic
        rbBoth = view.rb_both

        sEntering = view.s_entering
        sLeaving = view.s_leaving

        arguments?.let {
            if (it.containsKey(Tab1Fragment.ARG_ITEM_ID)) {
                tabletView = it.getBoolean(TABLET_VIEW)
            }
        }

        sEntering.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setEnteringCheck(isChecked)
        }

        sLeaving.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setLeavingCheck(isChecked)
        }

        mPresenter.getChecked()
        mPresenter.getEnteringCheck()
        mPresenter.getLeavingCheck()

        rbNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setNotification(isChecked)
        }
        rbAutomatic.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setAutomatic(isChecked)
        }
        rbBoth.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setBoth(isChecked)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        rbNotification = mView.findViewById<RadioButton>(R.id.rb_notification)
        rbAutomatic = mView.findViewById<RadioButton>(R.id.rb_automatic)
        rbBoth = mView.findViewById<RadioButton>(R.id.rb_both)

        sEntering = mView.findViewById<Switch>(R.id.s_entering)
        sLeaving = mView.findViewById<Switch>(R.id.s_leaving)
    }

    override fun setNotification() {
        rbNotification.isChecked = true
    }

    override fun setAutomatic() {
        rbAutomatic.isChecked = true
    }

    override fun setBoth() {
        rbBoth.isChecked = true
    }

    override fun setEnteringSwitch() {
        sEntering.isChecked = true
    }

    override fun setLeavingSwitch() {
        sLeaving.isChecked = true
    }
}