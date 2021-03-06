package com.soten.memo.ui

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.soten.memo.R
import com.soten.memo.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val navController by lazy { findNavController(R.id.nav_host_fragment_activity_main) }

    private val viewModel by viewModel<MemoSharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initNavigation()
        bindViews()

        requestPermission()
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            REQUEST_NEED_PERMISSIONS,
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initNavigation() {
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.memoListFragment,
                R.id.memoEditFragment,
            )
        )
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun bindViews() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.memoListFragment -> title = getString(R.string.memo_list)
                R.id.memoDetailFragment -> title = getString(R.string.memo_detail)
                R.id.memoEditFragment -> title = getString(R.string.memo_editor)
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            it.setOnClickListener {
                viewModel.setNormalState()
            }
        }
    }

    companion object {
        private const val REQUEST_NEED_PERMISSIONS = 101
    }

}