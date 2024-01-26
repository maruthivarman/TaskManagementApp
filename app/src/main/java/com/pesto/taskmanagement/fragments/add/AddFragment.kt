package com.pesto.taskmanagement.fragments.add

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
import com.pesto.taskmanagement.data.models.ToDoData
import com.pesto.taskmanagement.data.viewmodel.ToDoViewModel
import com.pesto.taskmanagement.fragments.SharedViewModel
import com.pesto.taskmanagement.R
import com.pesto.taskmanagement.databinding.FragmentAddBinding
import java.text.SimpleDateFormat
import java.util.Calendar

class AddFragment : Fragment() {
    private var _binding: FragmentAddBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val mSharedViewModel: SharedViewModel by viewModels()

    lateinit var myCalendar: Calendar

    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener

    var finalDate = 0L
    var finalTime = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        binding.statusSpinner.onItemSelectedListener = mSharedViewModel.listener
        binding.dateEt.setOnClickListener(View.OnClickListener {
            setDate()
        })
        binding.timeEt.setOnClickListener(View.OnClickListener {
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
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_add)
            insertDataToDB()
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDB() {
        val mTitle = binding.titleEt.text.toString()
        val mPriority = binding.statusSpinner.selectedItem.toString()
        val mDescription = binding.descriptionEt.text.toString()
        val mDate = binding.dateEt.text.toString()
        val mTime = binding.timeEt.text.toString()
        val validation = mSharedViewModel.verifyDataFromUser(mTitle, mDescription)
        if(validation) {
            val newData = ToDoData(
                0,
                mTitle,
                mSharedViewModel.parseStatusString(mPriority),
                mDate,
                mTime,
                mDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(
                requireContext(),
                "Successfully Added!",
                Toast.LENGTH_SHORT
            ).show()

            findNavController().navigate(R.id.action_addFragment_to_listFragment)
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
        binding.dateEt.setText(sdf.format(myCalendar.time))
        binding.timeEt.visibility = View.VISIBLE
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
        binding.timeEt.setText(sdf.format(myCalendar.time))

    }
}



