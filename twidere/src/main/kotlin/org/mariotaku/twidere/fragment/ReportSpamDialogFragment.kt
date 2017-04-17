/*
 * 				Twidere - Twitter client for Android
 * 
 *  Copyright (C) 2012-2014 Mariotaku Lee <mariotaku.lee@gmail.com>
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mariotaku.twidere.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import org.mariotaku.kpreferences.get
import org.mariotaku.twidere.R
import org.mariotaku.twidere.constant.IntentConstants.EXTRA_USER
import org.mariotaku.twidere.constant.nameFirstKey
import org.mariotaku.twidere.extension.applyTheme
import org.mariotaku.twidere.model.ParcelableUser

class ReportSpamDialogFragment : BaseDialogFragment(), DialogInterface.OnClickListener {

    override fun onClick(dialog: DialogInterface, which: Int) {
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                val user = user ?: return
                twitterWrapper.reportSpamAsync(user.account_key, user.key)
            }
            else -> {
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context)
        val user = user
        if (user != null) {
            val nameFirst = preferences[nameFirstKey]
            val displayName = userColorNameManager.getDisplayName(user, nameFirst)
            builder.setTitle(getString(R.string.report_user, displayName))
            builder.setMessage(getString(R.string.report_user_confirm_message, displayName))
        }
        builder.setPositiveButton(android.R.string.ok, this)
        builder.setNegativeButton(android.R.string.cancel, null)
        val dialog = builder.create()
        dialog.setOnShowListener {
            it as AlertDialog
            it.applyTheme()
        }
        return dialog
    }

    private val user: ParcelableUser?
        get() {
            val args = arguments
            if (!args.containsKey(EXTRA_USER)) return null
            return args.getParcelable<ParcelableUser>(EXTRA_USER)
        }

    companion object {

        val FRAGMENT_TAG = "create_user_block"

        fun show(fm: FragmentManager, user: ParcelableUser): ReportSpamDialogFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_USER, user)
            val f = ReportSpamDialogFragment()
            f.arguments = args
            f.show(fm, FRAGMENT_TAG)
            return f
        }
    }
}
