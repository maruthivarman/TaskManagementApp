package com.pesto.taskmanagement.fragments

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.pesto.taskmanagement.data.models.Status
import com.pesto.taskmanagement.data.models.ToDoData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pesto.taskmanagement.R
import com.pesto.taskmanagement.fragments.list.ListFragmentDirections

class BindingAdapters {

	companion object {
		@BindingAdapter("android:navigateToAddFragment")
		@JvmStatic
		fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
			view.setOnClickListener {
				if (navigate) {
					view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
				}
			}
		}

		@BindingAdapter("android:sendDataToUpdateFragment")
		@JvmStatic
		fun navigateToAddFragment(view: ConstraintLayout, currentItem: ToDoData) {
			view.setOnClickListener {
				val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
				view.findNavController().navigate(action)
			}
		}

		@BindingAdapter("android:emptyDatabase")
		@JvmStatic
		fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
			when (emptyDatabase.value) {
				true -> view.visibility = View.VISIBLE
				false -> view.visibility = View.INVISIBLE
			}
		}

		@BindingAdapter("android:parseStatusToInt")
		@JvmStatic
		fun parseStatusToInt(spinner: Spinner, status: Status) {
			when (status) {
				Status.ToDo -> spinner.setSelection(0)
				Status.InProgress -> spinner.setSelection(1)
				Status.Done -> spinner.setSelection(2)
			}
		}

		@BindingAdapter("android:parseStatusColor")
		@JvmStatic
		fun parseStatusColor(cardView: CardView, status: Status) {
			when (status) {
				Status.ToDo -> cardView.setCardBackgroundColor(
					cardView.context.getColor(
						R.color.red
					)
				)
				Status.InProgress -> cardView.setCardBackgroundColor(
					cardView.context.getColor(
						R.color.yellow
					)
				)
				Status.Done -> cardView.setCardBackgroundColor(
					cardView.context.getColor(
						R.color.green
					)
				)
			}
		}
	}
}