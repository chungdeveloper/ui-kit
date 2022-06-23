package com.chungld.uikit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.chungld.uikit.databinding.ActivitySnackBarActivityBinding
import com.chungld.uipack.snackbar.TrpSnackBar
import kotlinx.android.synthetic.main.activity_snack_bar_activity.*


class SnackBarActivity : BaseActivity<ActivitySnackBarActivityBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_snack_bar_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.btnDefault.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content").show()
        }

        mBinding.btnDefaultIcon.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content")
                .setIcon(R.drawable.ic_trp_uipack_holding_time).show()
        }

        mBinding.btnDefaultTitle.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content title").setTitle("Title đây").show()
        }

        mBinding.btnDefaultIconTitle.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content title")
                .setTitle("Title đây")
                .setIcon(R.drawable.ic_flight).show()
        }

        mBinding.btnDefaultAction.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content title")
                .setAction("Blew", View.OnClickListener {
                    Toast.makeText(this, "Blew Clicked", Toast.LENGTH_SHORT).show()
                }).show()
        }

        mBinding.btnDefaultActionIcon.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content title")
                .setAction(R.drawable.ic_trp_uipack_holding_time, View.OnClickListener {
                    Toast.makeText(this, "Blew Clicked", Toast.LENGTH_SHORT).show()
                }).show()
        }

        mBinding.btnDefaultActionIconTitle.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content title")
                .setTitle("Title nè")
                .setAction(R.drawable.ic_trp_uipack_holding_time, View.OnClickListener {
                    Toast.makeText(this, "Blew Clicked", Toast.LENGTH_SHORT).show()
                }).show()
        }

        mBinding.btnDefaultActionIconTitleIcon.setOnClickListener {
            TrpSnackBar.make(this, "UIKit Default content title")
                .setTitle("Title nè")
                .setIcon(R.drawable.ic_flight)
                .setAction(R.drawable.ic_trp_uipack_holding_time, View.OnClickListener {
                    Toast.makeText(this, "Blew Clicked", Toast.LENGTH_SHORT).show()
                }).show()
        }

    }
}
