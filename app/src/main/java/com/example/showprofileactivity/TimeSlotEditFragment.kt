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
        createViewModel()

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
                    val timeslot = JSONObject()
                    timeslot.put("title",  titlebox?.text)
                    timeslot.put("description", description?.text)
                    timeslot.put("location", location?.text)
                    timeslot.put("duration", duration?.text)
                    timeslot.put("date", "${date?.dayOfMonth}-${date?.month?.plus(1)}-${date?.year}")
                    timeslot.put("time","${time?.hour}:${time?.minute}" )
                    putString("timeslot", timeslot.toString())
                    apply()
                }
                requireView().findNavController().navigateUp()

            }
        })

    }





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        datepicker?.updateDate(date!!.split("-").get(2).toInt(),date!!.split("-").get(1).toInt(),date!!.split("-")?.get(0).toInt())

        val timepicker = view?.findViewById<TimePicker>(R.id.time_e)
        val time = vm.time.value
        timepicker?.minute = time!!.split(":").get(1).toInt()
        timepicker?.hour = time!!.split(":").get(0).toInt()


    }

    private fun createViewModel(){
        val sharedPref =requireActivity().getPreferences(Context.MODE_PRIVATE) ?: return
        val myJSON = JSONObject(
            sharedPref.getString("timeslot",
                """{"title":"Default title","description":"Default description","location":"Default location","duration":"Default duration","date":"12-12-2001","time":"12:00"}"""        ))

        vm.setTitle(myJSON.getString("title"))
        vm.setDesc(myJSON.getString("description"))
        vm.setDuration(myJSON.getString("duration"))
        vm.setLocation(myJSON.getString("location"))
        vm.setDate(myJSON.getString("date"))
        vm.setTime(myJSON.getString("time"))
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
            vm.setDate("${date?.dayOfMonth}-${date?.month}-${date?.year}")
            vm.setTime("${time?.hour}:${time?.minute}")
        }

        super.onDestroyView()

    }
}

