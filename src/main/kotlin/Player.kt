import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.paint.Color

class Player: Pane() {
    var currX: Double = 0.0
    var currY: Double = 0.0
    var playerWidth = 50.0
    var playerHeight = 75.0
    var playerDead = false
    var lives: Int = 3
    val playerImage = ImageView()
    val kanyeImage = Image("Kanye West.png")
    val yeezyImage = Image("superYeezy.png")

    init {
        var imageToAdd = kanyeImage
        playerImage.image = imageToAdd
        children.add(playerImage)
    }

    fun resetPlayer() {
        playerImage.isVisible = true
        this.currX = PLAYERDEFAULTX
        this.currY = PLAYERDEFAULTY
        this.playerDead = false
    }

    fun setYeezy(isYeezy: Boolean) {
        if (isYeezy) {
            playerImage.image = yeezyImage
        } else {
            playerImage.image = kanyeImage
        }
    }

    fun restartGame() {
        lives = 3
    }

    fun killPlayer(): Boolean {
        playerImage.isVisible = false
        this.playerDead = true
        lives--
        return (lives == 0)
    }

    fun draw() {

    }

    fun hitTest(inputX: Double, inputY: Double, projWidth: Double, projHeight: Double): Boolean {
        var xBound = currX + this.playerWidth
        var yBound = currY + this.playerHeight
        if (currX < inputX + projWidth && inputX < xBound && currY < inputY + projHeight && inputY < yBound) {

            return true
        }
        return false
    }


}