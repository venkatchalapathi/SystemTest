package com.venkat.systemtest.Views

import Items
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.venkat.systemtest.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    lateinit var firstBtn: Button
    lateinit var secondBtn: Button
    lateinit var thirdBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstBtn = findViewById(R.id.first)
        secondBtn = findViewById(R.id.second)
        thirdBtn = findViewById(R.id.third)
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.frameLayout, FirstFragment())
            .addToBackStack("first")
            .commit();
        firstBtn.setOnClickListener {

            if (isFragmentInBackstack(supportFragmentManager, "detail")) {
                Log.d(TAG, "onCreate: second Clicked")
                supportFragmentManager.popBackStackImmediate("detail", 2)
            } else {
                if (isFragmentInBackstack(supportFragmentManager, "first")) {
                    supportFragmentManager.popBackStackImmediate("first", 0)
                } else {
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, FirstFragment())
                        .addToBackStack("first")
                        .commit();
                }
            }
        }
        secondBtn.setOnClickListener {
            //getFragmentManager().popBackStack()
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(R.id.frameLayout, SecondFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
            Log.d(TAG, "onCreate: second Clicked")
        }
        thirdBtn.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.add(R.id.frameLayout, ThirdFragment())
            fragmentTransaction.commit()
        }

    }

    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commitAllowingStateLoss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun openDetailFragment(repo: Items) {
//        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
        val b: Bundle = Bundle()
        b.putString("data", Gson().toJson(repo))
        val fragment = DetailFragment()
        fragment.arguments = b
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .addToBackStack("detail")
            .commit();

    }

    fun isFragmentInBackstack(fragmentManager: FragmentManager, fragmentTagName: String): Boolean {
        for (entry in 0 until fragmentManager.backStackEntryCount) {
            if (fragmentTagName == fragmentManager.getBackStackEntryAt(entry).name) {
                return true
            }
        }
        return false
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}