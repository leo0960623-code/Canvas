package tw.edu.pu.csim.s1140337.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tw.edu.pu.csim.s1140337.canvas.ui.theme.CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                // 不使用 Scaffold，避免觸發 content padding 未使用的檢查，並保持真正的全螢幕
                CanvasScreen()
            }
        }
    }
}

@Composable
fun CanvasScreen(modifier: Modifier = Modifier) {
    // 改用 mutableFloatStateOf 消除警告並提升效能
    var canvasWidth by remember { mutableFloatStateOf(0f) }
    var canvasHeight by remember { mutableFloatStateOf(0f) }

    // 載入小小兵圖檔
    val minionsImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.minions)

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // 取得全螢幕畫布的即時寬高
            canvasWidth = size.width
            canvasHeight = size.height

            // 繪製洋紅色的圓，半徑 100，圓心 (300, 800)
            drawCircle(
                color = Color.Magenta,
                radius = 100f,
                center = Offset(300f, 800f)
            )

            // 繪製藍色空心矩形，左上角 (300, 200)，寬高 200
            drawRect(
                color = Color.Blue,
                topLeft = Offset(300f, 200f),
                size = Size(200f, 200f),
                style = Stroke(width = 5f)
            )

            // 繪製紅色的線，從 (200, 800) 到右下角，粗細 10
            drawLine(
                color = Color.Red,
                start = Offset(200f, 800f),
                end = Offset(size.width, size.height),
                strokeWidth = 10f
            )

            // 計算小小兵置中位置並繪製
            val minionsWidth = minionsImage.width
            val minionsHeight = minionsImage.height
            val offsetX = (size.width - minionsWidth) / 2f
            val offsetY = (size.height - minionsHeight) / 2f

            translate(offsetX, offsetY) {
                drawImage(minionsImage)
            }
        }

        // 頂部顯示畫布尺寸文字（加上 40.dp 避免文字被頂部狀態列遮擋）
        Text(
            text = "畫布寬度: $canvasWidth, 長度: $canvasHeight",
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.padding(start = 8.dp, top = 40.dp)
        )
    }
}