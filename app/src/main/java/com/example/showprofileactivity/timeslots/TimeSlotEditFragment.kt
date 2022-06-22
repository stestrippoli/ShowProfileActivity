package com.example.showprofileactivity.timeslots

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.showprofileactivity.R
import com.example.showprofileactivity.User
import com.example.showprofileactivity.offers.placeholder.Offer
import com.example.showprofileactivity.services.ServiceViewModel
import com.google.firebase.firestore.FirebaseFirestore


class TimeSlotEditFragment : Fragment(R.layout.fragment_time_slot_edit) {
    val vm by activityViewModels<TimeSlotViewModel>()
    val vms by activityViewModels<ServiceViewModel>()
    var edited = 0
    private val db: FirebaseFirestore
    init {
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_time_slot_edit, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                println("strano")
                val titlebox = view?.findViewById<EditText>(R.id.title_e)?.text.toString()
                val description = view?.findViewById<EditText>(R.id.description_e)?.text.toString()
                val location = view?.findViewById<EditText>(R.id.location_e)?.text.toString()
                val date = view?.findViewById<DatePicker>(R.id.date_e)
                val time = view?.findViewById<TimePicker>(R.id.time_e)
                val duration = view?.findViewById<EditText>(R.id.duration_e)?.text.toString()
                val skill = view?.findViewById<Spinner>(R.id.skill_spinner)?.selectedItem.toString()
                if(titlebox == "" || description == "" || location == "" || duration == "" )
                    Toast.makeText(requireActivity(), "Some field are empty, try again", Toast.LENGTH_SHORT).show()
                else{
                val o = Offer(
                        vm.id.value as String,
                        titlebox,
                        description,
                        location,
                        duration.toLong(),
                        vm.creator.value.toString(),
                        skill,
                        vm.email.value.toString(),
                        "${format(date!!.dayOfMonth)}-${format(date.month + 1)}-${format(date.year)}",
                        "${format(time!!.hour)}:${format(time.minute)}",
                        false,
                        "",
                        "",
                        false,
                        false,
                        false,
                        "",
                        ""
                    )
                    db.collection("offers")
                        .document(o.id)
                        .set(o)
                        .addOnSuccessListener {
                            Log.d("Firebase", "Offer successfully modified.")
                            Toast.makeText(
                                requireActivity(),
                                "Offer successfully modified.",
                                Toast.LENGTH_SHORT
                            ).show()

                            findNavController().navigateUp()
                        }
                        .addOnFailureListener {
                            Log.d(
                                "Firebase",
                                "Failed to modify user profile."
                            )
                        }
                }
            }
        })
    }





    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

                val title = view.findViewById<EditText>(R.id.title_e)
                vm.title.observe(this.viewLifecycleOwner) {
                    title.setText(it)
                }
                val description = view.findViewById<EditText>(R.id.description_e)
                vm.description.observe(this.viewLifecycleOwner) {
                    description.setText(it)
                }
                spinnerInitialization()

                val location = view.findViewById<EditText>(R.id.location_e)
                vm.location.observe(this.viewLifecycleOwner) {
                    location.setText(it)
                }
                val duration = view.findViewById<EditText>(R.id.duration_e)
                vm.duration.observe(this.viewLifecycleOwner) {
                    duration.setText(it)
                }
                val datepicker = view?.findViewById<DatePicker>(R.id.date_e)
                vm.date.observe(this.viewLifecycleOwner) {
                    val date = vm.date.value
                    datepicker?.updateDate(
                        date!!.split("-")[2].toInt(),
                        date.split("-")[1].toInt() - 1, date.split("-")[0].toInt()
                    )
                }
                val timepicker = view.findViewById<TimePicker>(R.id.time_e)
                vm.time.observe(this.viewLifecycleOwner) {
                    val time = vm.time.value
                    timepicker?.minute = time!!.split(":")[1].toInt()
                    timepicker?.hour = time.split(":")[0].toInt()
                }



    }

    fun spinnerInitialization(){
        val spinner: Spinner = requireView().findViewById<Spinner>(R.id.skill_spinner)
        val services = vms.services.value!!
        val sar: MutableList<String> = ArrayList()
        for (s in services) {
            sar.add(s.name)
        }

        val spinnerArrayAdapter = ArrayAdapter<String>(
            this.requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            sar
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter
        val spinnerPosition: Int = sar.indexOf(vm.skill.value)
        spinner.setSelection(spinnerPosition)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                vm.setSkill(selectedItem)
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }




    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDestroyView() {
        updatevm()
        super.onDestroyView()

    }


    @RequiresApi(Build.VERSION_CODES.M)
    private fun updatevm(){
        val titlebox = requireView().findViewById<EditText>(R.id.title_e)
        val description = requireView().findViewById<EditText>(R.id.description_e)
        val location = requireView().findViewById<EditText>(R.id.location_e)
        val date = requireView().findViewById<DatePicker>(R.id.date_e)
        val time = requireView().findViewById<TimePicker>(R.id.time_e)
        val duration = requireView().findViewById<EditText>(R.id.duration_e)

        vm.setTitle(titlebox.text)
        vm.setDesc(description.text)
        vm.setDuration(duration.text)
        vm.setLocation(location.text)
        vm.setDate("${format(date.dayOfMonth)}-${format(date.month+1)}-${format(date.year)}")
        vm.setTime("${format(time.hour)}:${format(time.minute)}")
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
