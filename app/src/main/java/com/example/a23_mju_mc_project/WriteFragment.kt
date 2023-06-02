import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.MainActivity
import com.example.a23_mju_mc_project.R
import com.example.a23_mju_mc_project.databinding.FragmentWriteBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File

class WriteFragment : Fragment() {
    private lateinit var binding: FragmentWriteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWriteBinding.inflate(inflater, container, false)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbarLayout)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.navigationbar)
        toolbar.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달된 데이터 받기
        val photoFilePath = arguments?.getString("photoFilePath")

        // photoFilePath를 사용하여 필요한 작업 수행
        if (photoFilePath != null) {
            // 이미지 파일 경로를 사용하여 이미지를 로드하거나 다른 작업을 수행할 수 있습니다.
            val imageFile = File(photoFilePath)
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

            // 예시: 이미지를 ImageView에 설정
            binding.imageView.setImageBitmap(bitmap)
        }
    }
    }

