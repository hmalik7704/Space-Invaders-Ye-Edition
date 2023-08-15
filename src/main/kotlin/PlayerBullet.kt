import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane




class PlayerBullet: Pane() {
    var currX: Double = 0.0
    var currY: Double = 0.0
    var bulletWidth: Double = 20.0
    var bulletHeight: Double = 40.0
    var isYeezy = false

    val bulletImage = ImageView()
    var markedForRemove: Boolean = false
    val regularBullet = Image("yeezy1.png")
    val yeezyBullet = Image("yeezyBullet.png")

    init {
        var imageToAdd = regularBullet
        bulletImage.image = imageToAdd
        children.add(bulletImage)
    }

    fun setYeezyBullet(isYeezy: Boolean) {
        if (isYeezy) {
            bulletWidth = 60.0
            bulletHeight = 120.0
            bulletImage.image = yeezyBullet
        } else {
            bulletWidth = 20.0
            bulletHeight = 40.0
            bulletImage.image = regularBullet
        }
    }

    fun draw() {

    }
}