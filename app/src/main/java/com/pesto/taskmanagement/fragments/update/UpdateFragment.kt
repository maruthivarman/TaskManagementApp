package com.pesto.taskmanagement.fragments.update

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pesto.taskmanagement.data.models.ToDoData
import com.pesto.taskmanagement.data.viewmodel.ToDoViewModel
import com.pesto.taskmanagement.fragments.SharedViewModel
import com.pesto.taskmanagement.R
import com.pesto.taskmanagement.databinding.FragmentUpdateBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class UpdateFragment : Fragment() {
    private var _binding: FragmentUpdateBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val mSharedViewModel: SharedViewModel by viewModels()
    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val args by navArgs<UpdateFragmentArgs>()

    private lateinit var myCalendar: Calendar
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    private lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    private var finalDate = 0L
    private var finalTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.args = args
        binding.currentStatusSpinner.onItemSelectedListener = mSharedViewModel.listener
        binding.currentDateEt.setOnClickListener(View.OnClickListener {
            setDate()
        })
        binding.currentTimeEt.setOnClickListener(View.OnClickListener {
            setTime()
        })
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_save -> updateItem()
            R.id.menu_delete -> confirmItemRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmItemRemoval() {
        val alertDialog = AlertDialog.Builder(requireContext()).apply {
            setPositiveButton("YES") { _, _ ->
                mToDoViewModel.deleteItem(args.currentItem)
                Toast.makeText(
                    requireContext(),
                    "Successfully Removed: '${args.currentItem.title}'",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            }
            setNegativeButton("NO") { _, _ -> /*DO NOTHING*/ }
            setTitle("Delete '${args.currentItem.title}'")
            setMessage("Are you sure you want to remove: '${args.currentItem.title}'?")
            create()
        }
        alertDialog.show()
    }

    private fun updateItem() {
        val title = binding.currentTitleEt.text.toString()
        val desc = binding.currentDescriptionEt.text.toString()
        val date = binding.currentDateEt.text.toString()
        val time = binding.currentTimeEt.text.toString()
        val status = mSharedViewModel.parseStatusString(binding.currentStatusSpinner.selectedItem.toString())
        if(mSharedViewModel.verifyDataFromUser(title, desc)) {
            val updatedItem = ToDoData(
                id = args.currentItem.id,
                title = title,
                status = status,
                date = date,
                time = time,
                description = desc
            )
            mToDoViewModel.updateData(updatedItem)
            Toast.makeText(
                requireContext(),
                "Successfully Updated!",
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Please fill out all the fields.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setDate() {
        myCalendar = Calendar.getInstance()

        dateSetListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, month)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(), dateSetListener, myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val myformat = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(myformat)
        finalDate = myCalendar.time.time
        binding.currentDateEt.setText(sdf.format(myCalendar.time))
        binding.currentTimeEt.visibility = View.VISIBLE
    }

    private fun setTime() {
        myCalendar = Calendar.getInstance()

        timeSetListener =
            TimePickerDialog.OnTimeSetListener() { _: TimePicker, hourOfDay: Int, min: Int ->
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myCalendar.set(Calendar.MINUTE, min)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE), false
        )
        timePickerDialog.show()
    }

    private fun updateTime() {
        val myformat = "h:mm a"
        val sdf = SimpleDateFormat(myformat)
        finalTime = myCalendar.time.time
        binding.currentTitleEt.setText(sdf.format(myCalendar.time))
    }
}