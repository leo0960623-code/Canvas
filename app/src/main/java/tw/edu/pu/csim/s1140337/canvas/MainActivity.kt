package tw.edu.pu.csim.s1140337.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.delay
import tw.edu.pu.csim.s1140337.canvas.ui.theme.CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                CanvasScreen()
            }
        }
    }
}

@Composable
fun CanvasScreen(modifier: Modifier = Modifier) {
    var CanvasWidth by remember { mutableStateOf(0f) }
    var CamvaHeight by remember { mutableStateOf(0f) }

    var minionsX by remember { mutableStateOf(0f) }
    var minionsY by remember { mutableStateOf(0f) }

    // 遊戲狀態開關：true 為暫停/結束，false 為進行中
    var gameOver by remember { mutableStateOf(false) }

    // 載入圖片資源 (小小兵)
    val minionsImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.minions)

    // 當 key1 (gameOver) 改變時重新評估協程
    LaunchedEffect(key1 = gameOver) {
        while (!gameOver) {
            delay(100L) // 0.1 秒移動一次
            minionsX += 5
            minionsY += 10
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            CanvasWidth = size.width
            CamvaHeight = size.height

            // 繪製洋紅色的圓，半徑 100，圓心 (100, 100)
            drawCircle(
                Color.Magenta,
                100f,
                Offset(100f, 100f)
            )

            // 繪製藍色空心矩形，左上角 (300, 200)，寬高 200
            drawRect(
                Color.Blue,
                Offset(300f, 200f),
                Size(200f, 200f),
                style = Stroke(width = 5f)
            )

            // 繪製紅色的線
            drawLine(
                Color.Red,
                Offset(200f, 800f),
                Offset(size.width, size.height),
                strokeWidth = 10f
            )

            // 取得小小兵圖片寬高並加上位移量
            val minionsWidth = minionsImage.width
            val minionsHeight = minionsImage.height
            val offsetX = (size.width - minionsWidth) / 2f + minionsX
            val offsetY = (size.height - minionsHeight) / 2f + minionsY

            translate(offsetX, offsetY) {
                drawImage(minionsImage)
            }
        }

        // 頂部控制列：顯示畫布資訊與暫停/繼續按鈕
        Row(
            modifier = Modifier
                .padding(start = 8.dp, top = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "畫布寬度: $CanvasWidth, 長度: $CamvaHeight",
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    gameOver = !gameOver
                }
            ) {
                Text(if (gameOver) "繼續遊戲" else "暫停遊戲")
            }
        }
    }
}