package com.example.showprofileactivity

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.activity.OnBackPressedCallback
import androidx.core.view.get
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI


class TimeSlotEditFragment : Fragment(R.layout.time_slot_edit_fragment) {
    val vm by activityViewModels<TimeSlotViewModel>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.time_slot_edit_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
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
                }

                requireView().findNavController().navigateUp()

            }
        })

    }

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


        val date = view.findViewById<DatePicker>(R.id.date_e)
/*        vm.date.observe(this.viewLifecycleOwner){
            date.init(vm.date.value!!.split("-")[2].toInt(),vm.date.value!!.split("-")[1].toInt(),vm.date.value!!.split("-")[0].toInt().)
        }


        val time = view.findViewById<TimePicker>(R.id.time_e)
*/

        }



}