import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane

class EnemyBullet: Pane() {
    var currX: Double = 0.0
    var currY: Double = 0.0
    val bulletWidth = 25.0
    val bulletHeight = 25.0
    val bulletImage = ImageView()

    var markedForRemove: Boolean = false

    init {
        var imageToAdd = Image("tomato.png")
        bulletImage.image = imageToAdd
        children.add(bulletImage)
    }



    fun draw() {

    }
}