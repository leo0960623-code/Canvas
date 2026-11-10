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
import androidx.compose.runtime.mutableFloatStateOf
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
    var CanvasWidth by remember { mutableFloatStateOf(0f) }
    var CamvaHeight by remember { mutableFloatStateOf(0f) }

    var minionsX by remember { mutableFloatStateOf(0f) }
    var minionsY by remember { mutableFloatStateOf(0f) }

    // 遊戲狀態：true 為暫停，false 為進行中
    var gameOver by remember { mutableStateOf(false) }

    // 載入小小兵圖片
    val minionsImage: ImageBitmap =
        ImageBitmap.imageResource(id = R.drawable.minions)

    // 每 0.1 秒移動並檢查邊界
    LaunchedEffect(key1 = gameOver) {
        while (!gameOver) {
            delay(100L) // 0.1 秒
            minionsX += 15f // 向右移動
            minionsY += 20f // 向下移動

            // 計算小小兵目前的實際繪製座標
            if (CanvasWidth > 0f && CamvaHeight > 0f) {
                val currentX = (CanvasWidth - minionsImage.width) / 2f + minionsX
                val currentY = (CamvaHeight - minionsImage.height) / 2f + minionsY

                // 當移出手機螢幕邊界 (超出右方或下方)
                if (currentX >= CanvasWidth || currentY >= CamvaHeight) {
                    // 重設回左上角 (0, 0)
                    minionsX = -(CanvasWidth - minionsImage.width) / 2f
                    minionsY = -(CamvaHeight - minionsImage.height) / 2f
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            CanvasWidth = size.width
            CamvaHeight = size.height

            // 繪製洋紅色的圓，半徑 100，圓心 (100, 100)
            drawCircle(
                color = Color.Magenta,
                radius = 100f,
                center = Offset(100f, 100f)
            )

            // 繪製藍色空心矩形，左上角 (300, 200)，寬高 200
            drawRect(
                color = Color.Blue,
                topLeft = Offset(300f, 200f),
                size = Size(200f, 200f),
                style = Stroke(width = 5f)
            )

            // 繪製紅色的線，從 (200, 800) 到右下角
            drawLine(
                color = Color.Red,
                start = Offset(200f, 800f),
                end = Offset(size.width, size.height),
                strokeWidth = 10f
            )

            // 計算置中與位移後的位置
            val minionsWidth = minionsImage.width
            val minionsHeight = minionsImage.height
            val offsetX = (size.width - minionsWidth) / 2f + minionsX
            val offsetY = (size.height - minionsHeight) / 2f + minionsY

            translate(offsetX, offsetY) {
                drawImage(minionsImage)
            }
        }

        // 頂部控制列
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