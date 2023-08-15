import javafx.application.Application
import javafx.scene.Node
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.control.*
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage
import javafx.stage.DirectoryChooser
import javafx.scene.control.ListView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.*
import javafx.scene.paint.Color

import java.io.File
import java.util.*

const val WIDTH = 1280.0
const val HEIGHT = 720.0
const val BULLETSPEED: Double = 10.0
const val PLAYERDEFAULTX = WIDTH / 2
const val PLAYERDEFAULTY = HEIGHT - 100.0
const val LEVELONE_SPEED = 1.0
const val LEVELTWO_SPEED = 1.5
const val LEVELTHREE_SPEED = 2.0
const val LEVELONE_SMART_ENEMY = 1
const val LEVELTWO_SMART_ENEMY = 2
const val LEVELTHREE_SMART_ENEMY = 3
const val LEVELONE_SHOT_TIMER = 300.0
const val LEVELTWO_SHOT_TIMER = 200.0
const val LEVELTHREE_SHOT_TIMER = 150.0
const val LEVELONE_SHOT_SPEED = 5.0
const val LEVELTWO_SHOT_SPEED = 7.5
const val LEVELTHREE_SHOT_SPEED = 10.0
const val ENEMYWIDTH = 10 //num enemies per row
const val ENEMYHEIGHT = 5 //num of enemy columns
const val ENEMBULLETSPEED = 5.0
const val PLAYERMOVESPEED = 6.5
const val SPEEDINCREASE = 0.15
const val ENEMYPOINTS = 25
const val ENEMYDESCENDAMOUNT = 25.0

class SpaceInvaders : Application() {

    override fun start(stage: Stage) {
        // CREATE AND SHOW SCENE
        val scene = loadingScreen(stage)
        stage.scene = scene
        stage.title = "Space Invaders"
        stage.isResizable = false
        stage.show()
    }
}

