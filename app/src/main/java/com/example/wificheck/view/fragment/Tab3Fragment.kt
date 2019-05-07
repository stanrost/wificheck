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
import com.example.wificheck.presenter.fragment.Tab3Presenter
import kotlinx.android.synthetic.main.tab3_settings_fragment.*
import kotlinx.android.synthetic.main.tab3_settings_fragment.view.*

class Tab3Fragment : Fragment(), Tab3View {

    lateinit var mPresenter: Tab3Presenter
    lateinit var mView: View
    lateinit var rbNotification: RadioButton
    lateinit var rbAutomatic: RadioButton
    lateinit var rbBoth: RadioButton
    lateinit var sEntering: Switch
    lateinit var sLeaving: Switch
    lateinit var sVibrate: Switch
    lateinit var sLight: Switch

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab3_settings_fragment, container, false)
        mView = view
        mPresenter = Tab3PresenterImpl(this, view.context)
        rbNotification = view.rb_notification
        rbAutomatic = view.rb_automatic
        rbBoth = view.rb_both

        sEntering = view.s_entering
        sLeaving = view.s_leaving

        sVibrate = view.s_vibrate
        sLight = view.s_light

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O) {
            view.ll_notification.visibility = View.GONE
            view.v_notification.visibility = View.GONE
        }

        sEntering.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setEnteringCheck(isChecked)
        }

        sLeaving.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setLeavingCheck(isChecked)
        }

        sVibrate.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setVibrateCheck(isChecked)
        }

        sLight.setOnCheckedChangeListener { buttonView, isChecked ->
            mPresenter.setLightCheck(isChecked)
        }

        mPresenter.getChecked()
        mPresenter.getEnteringCheck()
        mPresenter.getLeavingCheck()
        mPresenter.getVibrateCheck()
        mPresenter.getLightCheck()

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

    override fun setVibrateSwitch() {
        sVibrate.isChecked = true
    }

    override fun setLigthSwitch() {
        sLight.isChecked = true
    }
}