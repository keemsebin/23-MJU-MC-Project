import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a23_mju_mc_project.Feed
import com.example.a23_mju_mc_project.MyAppDatabase
import com.example.a23_mju_mc_project.R
import com.example.a23_mju_mc_project.databinding.FragmentWriteBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class WriteFragment : Fragment() {
    private lateinit var binding: FragmentWriteBinding
    private lateinit var nickname: String
    private lateinit var savedUri: String
    private lateinit var database: MyAppDatabase
    @RequiresApi(Build.VERSION_CODES.O)
    val currentTime = LocalTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val timeFormat = DateTimeFormatter.ofPattern("HH:mm")
    @RequiresApi(Build.VERSION_CODES.O)
    val dateFormat = DateTimeFormatter.ofPattern("M/d")

    @RequiresApi(Build.VERSION_CODES.O)
    val formattedTime = currentTime.format(timeFormat)
    @RequiresApi(Build.VERSION_CODES.O)
    val formattedDate = currentDate.format(dateFormat)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWriteBinding.inflate(inflater, container, false)
        database = MyAppDatabase.getDatabase(requireContext().applicationContext)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbarLayout)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.navigationbar)
        toolbar.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            val users = database.userDao().getAllUsers()
            nickname = users[0].nickname
            savedUri = arguments?.getString("photoFilePath").orEmpty()

            requireActivity().runOnUiThread {
                if (savedUri.isNotEmpty()) {
                    val photoUri = Uri.parse(savedUri)
                    binding.imageView.setImageURI(photoUri)
                }
            }
        }

        binding.writeDate.text = formattedDate
        binding.writeTime.text = formattedTime

        binding.saveBtn.setOnClickListener {
            val feedText = binding.writeComment.text.toString()
            if (feedText.isNotEmpty()) {
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = currentDateTime.format(formatter)
                val feed = Feed(
                    nickname = nickname,
                    picture = savedUri,
                    upload_Date = formattedDate,
                    upload_Time = formattedTime,
                    feed_Text = feedText
                )

                saveFeed(feed)
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(requireContext(), "Feed text is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveFeed(feed: Feed) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val db = MyAppDatabase.getDatabase(requireContext().applicationContext)
                db.feedDao().insertFeed(feed)

                Log.d("WriteFragment", "Feed saved: $feed")
                printAllFeeds() //Feed table 데이터 확인 가능
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "Feed saved successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Additional operations
                }
            } catch (e: Exception) {
                Log.e("WriteFragment", "Error saving feed: ${e.message}")
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        "An error occurred while saving the feed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun printAllFeeds() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = MyAppDatabase.getDatabase(requireContext().applicationContext)
            val feedList = db.feedDao().getAllFeeds()
            if (feedList != null) {
                for (feed in feedList) {
                    Log.d("WriteFragment", "Feed: $feed")
                }
            }
        }
    }

}
