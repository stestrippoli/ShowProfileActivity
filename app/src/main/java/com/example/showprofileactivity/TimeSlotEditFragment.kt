package com.example.showprofileactivity

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import org.json.JSONArray
import org.json.JSONObject


class TimeSlotEditFragment : Fragment(R.layout.time_slot_edit_fragment) {
    val vm by activityViewModels<TimeSlotViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.time_slot_edit_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        populateBoxes()
        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
             //Update shared preference for permanent data
                val titlebox = view?.findViewById<EditText>(R.id.title_e)
                val description = view?.findViewById<EditText>(R.id.description_e)
                val location = view?.findViewById<EditText>(R.id.location_e)
                val date = view?.findViewById<DatePicker>(R.id.date_e)
                val time = view?.findViewById<TimePicker>(R.id.time_e)
                val duration = view?.findViewById<EditText>(R.id.duration_e)

                val sharedPref = activity!!.getPreferences(Context.MODE_PRIVATE)
                with (sharedPref.edit()) {
                    val list = JSONArray(sharedPref.getString("list", "[]"))
                    val timeslot = JSONObject()
                    timeslot.put("title",  titlebox?.text)
                    timeslot.put("description", description?.text)
                    timeslot.put("location", location?.text)
                    timeslot.put("duration", duration?.text)
                    timeslot.put("date", "${format(date!!.dayOfMonth)}-${format(date.month+1)}-${format(date.year)}")
                    timeslot.put("time","${format(time!!.hour)}:${format(time.minute)}" )

                    list.put(vm.id.value!!, timeslot)
                    putString("list", list.toString())
                    apply()
                }
                requireView().findNavController().navigateUp()

            }
        })
    }





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateBoxes()
        val title = view.findViewById<EditText>(R.id.title_e)
        vm.title.observe(this.viewLifecycleOwner){
            title.setText(it)
        }
        val description = view.findViewById<EditText>(R.id.description_e)
        vm.description.observe(this.viewLifecycleOwner){
            description.setText(it)
        }

        val location = view.findViewById<EditText>(R.id.location_e)
        vm.location.observe(this.viewLifecycleOwner){
            location.setText(it)
        }
        val duration = view.findViewById<EditText>(R.id.duration_e)
        vm.duration.observe(this.viewLifecycleOwner){
            duration.setText(it)
        }
        val datepicker = view?.findViewById<DatePicker>(R.id.date_e)
        val date = vm.date.value
        datepicker?.updateDate(date!!.split("-")[2].toInt(),
            date.split("-")[1].toInt()-1, date.split("-")[0].toInt())

        val timepicker = view?.findViewById<TimePicker>(R.id.time_e)
        val time = vm.time.value
        timepicker?.minute = time!!.split(":")[1].toInt()
        timepicker?.hour = time.split(":")[0].toInt()


    }



    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDestroyView() {
        val titlebox = view?.findViewById<EditText>(R.id.title_e)
        val description = view?.findViewById<EditText>(R.id.description_e)
        val location = view?.findViewById<EditText>(R.id.location_e)
        val date = view?.findViewById<DatePicker>(R.id.date_e)
        val time = view?.findViewById<TimePicker>(R.id.time_e)
        val duration = view?.findViewById<EditText>(R.id.duration_e)

        if (titlebox != null&&description!=null&&location!=null&&date!=null&&time!=null&&duration!=null) {
            vm.setTitle(titlebox.text)
            vm.setDesc(description.text)
            vm.setDuration(duration.text)
            vm.setLocation(location.text)
            vm.setDate("${format(date.dayOfMonth)}-${format(date.month+1)}-${format(date.year)}")
            vm.setTime("${format(time.hour)}:${format(time.minute)}")
        }

        super.onDestroyView()

    }

    private fun populateBoxes() {
        val myJSON = JSONObject(this.arguments?.getString("item"))
        vm.setId(myJSON.getString("id").toInt())
        vm.setTitle(myJSON.getString("title"))
        vm.setDesc(myJSON.getString("description"))
        vm.setDuration(myJSON.getString("duration"))
        vm.setLocation(myJSON.getString("location"))
        vm.setDate(myJSON.getString("date"))
        vm.setTime(myJSON.getString("time"))

    }

    private fun format(ex: Int ): CharSequence{
        //funzione per non perdere gli zeri
        if(ex < 10){
            return "0$ex"
        }
        else
            return "$ex"

    }
}

