package com.mlukov.articles.presentation.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

import dagger.android.AndroidInjection

import com.mlukov.articles.R

class BaseActivity : AppCompatActivity() {


    //region show screen
    fun showScreen(content : Fragment,
                   contentTag : String,
                   addToBackStack : Boolean,
                   transitionContent : Boolean) {

        val ft = getSupportFragmentManager().beginTransaction()

        // Content area slide animation
        if (transitionContent) {

            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        }

        ft.replace( R.id.placeholder_content, content, contentTag )

        if (addToBackStack) {
            ft.addToBackStack(contentTag + System.identityHashCode(content))
        }

        ft.commitAllowingStateLoss()
    }
    //endregion

    //region AppCompatActivity implementation
    override fun onCreate(savedInstanceState : Bundle?) {

        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        when(id) {

            android.R.id.home -> {

                if ( getSupportFragmentManager().getBackStackEntryCount() > 0) {

                    getSupportFragmentManager().popBackStack()

                    return true
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
    //endregion
}