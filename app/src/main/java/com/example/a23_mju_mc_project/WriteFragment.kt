import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a23_mju_mc_project.AppDatabase
import com.example.a23_mju_mc_project.Feed
import com.example.a23_mju_mc_project.MyAppDatabase
import com.example.a23_mju_mc_project.databinding.FragmentWriteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class WriteFragment : Fragment() {
    private lateinit var binding: FragmentWriteBinding
    private lateinit var nickname: String
    private lateinit var savedUri: String
    private lateinit var database: MyAppDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWriteBinding.inflate(inflater, container, false)
        database = MyAppDatabase.getDatabase(requireContext().applicationContext)
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

        binding.saveButton.setOnClickListener {
            val feedText = binding.feedEditText.text.toString()
            if (feedText.isNotEmpty()) {
                val currentDateTime = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = currentDateTime.format(formatter)
                val feed = Feed(
                    nickname = nickname,
                    picture = savedUri.toByteArray(),
                    upload_Date = formattedDate,
                    feed_Text = feedText
                )
                binding.uploadDateTextView.text = "Upload Date: $formattedDate"
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
