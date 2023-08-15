import javafx.animation.AnimationTimer
import javafx.scene.Scene
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.media.AudioClip
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.stage.Stage
import java.awt.Rectangle
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import java.io.File

// very simple interface for these demos
//from demo slides on Jeff Avery Github
interface AnimatableScene {
    abstract var x: Double
    abstract var bulletCounter: Double
    abstract var enemyBulletCounter: Double
    abstract var resetTimer: Double
    abstract var randomAlienShotTimer: Double
    abstract var randomAlienShotTimer2: Double
    abstract var randomAlienShotTimer3: Double
    var playerMovingLeft: Boolean
    var playerMovingRight: Boolean
    var playerShooting: Boolean
    var playerX: Double
    var playerY: Double
    var yeezyMode: Boolean
    fun draw() { }
    fun restartGame()
    fun quit(): Boolean
    fun giveYeezyMode()
}

class GameScene(level: Int): AnimatableScene, Pane() {
    override var yeezyMode = false
    override var x: Double = 0.0
    override var playerX: Double = WIDTH/2
    override var playerY: Double = HEIGHT - 100.0
    var playerBullets: ArrayList<PlayerBullet> = ArrayList()
    var enemyBullets: ArrayList<EnemyBullet> = ArrayList()
    var currentLevel = level
    val gamePane = Pane()
    val player = Player()
    val enemies = EnemyGrid()
    var loadingNewLevel = false
    var gameEnd = false
    var gameOver = false
    var bulletSpeed = LEVELONE_SHOT_SPEED
    var alienShotTimerStart = LEVELONE_SHOT_TIMER
    var numSmartEnemies = LEVELONE_SMART_ENEMY
    var score = 0
    override var bulletCounter = 0.0
    override var enemyBulletCounter = 0.0
    override var randomAlienShotTimer = LEVELONE_SHOT_TIMER
    override var randomAlienShotTimer2 = -1.0
    override var randomAlienShotTimer3 = -1.0
    override var playerMovingLeft = false
    override var playerMovingRight = false
    override var playerShooting = false
    override var resetTimer = 0.0
    val classLoader = Thread.currentThread().contextClassLoader
    val enemyShootBulletSound = AudioClip(File("${System.getProperty("user.dir")}/src/main/resources/brr.mp3").toURI().toString())
    val shootBulletSound = AudioClip(File("${System.getProperty("user.dir")}/src/main/resources/bang bang.mp3").toURI().toString())
    val playerDeathSound = AudioClip(File("${System.getProperty("user.dir")}/src/main/resources/bruh.mp3").toURI().toString())
    val enemyDeathSound = AudioClip(File("${System.getProperty("user.dir")}/src/main/resources/hitmarker_2.mp3").toURI().toString())
    val gameOverSound = AudioClip(File("${System.getProperty("user.dir")}/src/main/resources/what happened.mp3").toURI().toString())
    val gameWinSound = AudioClip(File("${System.getProperty("user.dir")}/src/main/resources/lets go.mp3").toURI().toString())


    var scoreLabel = Label("Score: $score")
    var livesLabel = Label("Lives: ${player.lives}")
    var levelLabel = Label("Level: $currentLevel")
    var rectangle = javafx.scene.shape.Rectangle(0.0, 0.0, WIDTH, HEIGHT)



    init {
        children.add(rectangle)
        rectangle.layoutX = 0.0
        rectangle.layoutY = 0.0
        player.currX = PLAYERDEFAULTX
        player.currY = PLAYERDEFAULTY
        gamePane.children.add(player)
        children.add(gamePane)
        for (i in 0 until enemies.height) {
            for (j in 0 until enemies.width) {
                children.add(enemies.matrix[i][j])
            }
        }
        if (currentLevel == 1) {
            enemies.speed = LEVELONE_SPEED
            alienShotTimerStart = LEVELONE_SHOT_TIMER
            randomAlienShotTimer = LEVELONE_SHOT_TIMER
            bulletSpeed = LEVELONE_SHOT_SPEED
            randomAlienShotTimer2 = -1.0
            randomAlienShotTimer3 = -1.0
            numSmartEnemies = LEVELONE_SMART_ENEMY
        } else if (currentLevel == 2) {
            enemies.speed = LEVELTWO_SPEED
            alienShotTimerStart = LEVELTWO_SHOT_TIMER
            randomAlienShotTimer = LEVELONE_SHOT_TIMER
            bulletSpeed = LEVELTWO_SHOT_SPEED
            randomAlienShotTimer2 = LEVELTWO_SHOT_TIMER
            randomAlienShotTimer3 = -1.0
            numSmartEnemies = LEVELTWO_SMART_ENEMY
        } else if (currentLevel == 3) {
            enemies.speed = LEVELTHREE_SPEED
            alienShotTimerStart = LEVELTHREE_SHOT_TIMER
            bulletSpeed = LEVELTHREE_SHOT_SPEED
            randomAlienShotTimer = LEVELONE_SHOT_TIMER
            randomAlienShotTimer2 = LEVELTWO_SHOT_TIMER
            randomAlienShotTimer3 = LEVELTHREE_SHOT_TIMER
            numSmartEnemies = LEVELTHREE_SMART_ENEMY
        }
        scoreLabel.font = Font("Arial", 24.0)
        scoreLabel.textFill = Color.WHITE
        livesLabel.font = Font("Arial", 24.0)
        livesLabel.textFill = Color.WHITE
        levelLabel.font = Font("Arial", 24.0)
        levelLabel.textFill = Color.WHITE
        rectangle.fill = Color.BLACK

        children.add(scoreLabel)
        scoreLabel.layoutX = 10.0
        scoreLabel.layoutY = 15.0
        children.add(livesLabel)
        livesLabel.layoutX = WIDTH - 400.0
        livesLabel.layoutY = 15.0
        children.add(levelLabel)
        levelLabel.layoutX = WIDTH - 200.0
        levelLabel.layoutY = 15.0


    }

    override fun giveYeezyMode() {
        yeezyMode = !yeezyMode
        player.setYeezy(yeezyMode)
    }

    override fun quit(): Boolean {
        return (gameOver || gameEnd)
    }

    override fun restartGame() {
        if (gameOver || gameEnd) {
            yeezyMode = false
            player.setYeezy(yeezyMode)
            if (playerBullets.isNotEmpty()) {
                for (i in playerBullets.size - 1 downTo 0) {
                   playerBullets.remove(playerBullets[i])
                }
            }
            if (enemyBullets.isNotEmpty()) {
                for (i in enemyBullets.size - 1 downTo 0) {
                    enemyBullets.remove(enemyBullets[i])
                }
            }
            children.clear()
            children.add(rectangle)
            rectangle.layoutX = 0.0
            rectangle.layoutY = 0.0
            children.add(scoreLabel)
            scoreLabel.layoutX = 10.0
            scoreLabel.layoutY = 15.0
            children.add(livesLabel)
            livesLabel.layoutX = WIDTH - 400.0
            livesLabel.layoutY = 15.0
            children.add(levelLabel)
            levelLabel.layoutX = WIDTH - 200.0
            levelLabel.layoutY = 15.0

            score = 0
            player.currX = PLAYERDEFAULTX
            player.currY = PLAYERDEFAULTY
            gamePane.children.clear()
            gamePane.children.add(player)
            children.add(gamePane)
            enemies.resetEnemies()

                for (i in 0 until enemies.height) {
                    for (j in 0 until enemies.width) {
                        children.add(enemies.matrix[i][j])
                    }
                }

                currentLevel = 1
                if (currentLevel == 1) {
                    enemies.speed = LEVELONE_SPEED
                    alienShotTimerStart = LEVELONE_SHOT_TIMER
                    bulletSpeed = LEVELONE_SHOT_SPEED
                    randomAlienShotTimer = LEVELONE_SHOT_TIMER
                    randomAlienShotTimer2 = -1.0
                    randomAlienShotTimer3 = -1.0
                    numSmartEnemies = LEVELONE_SMART_ENEMY
                }else {
                    gameEnd = true
                }
                gameEnd = false
                gameOver = false
                player.restartGame()
                player.resetPlayer()
                enemies.makeEnemiesVisible()
            }
    }


    fun shootBullet() {
        if (bulletCounter <= 0) {
            shootBulletSound.play(0.5)
            var newBullet = PlayerBullet()
            newBullet.setYeezyBullet(yeezyMode)
            newBullet.currX = player.currX
            newBullet.currY = player.currY
            children.add(newBullet)
            newBullet.layoutX = newBullet.currX
            newBullet.layoutY = newBullet.currY
            playerBullets.add(newBullet)
            bulletCounter = 30.0
        }
    }

    fun shootEnemyBullet() {
            enemyShootBulletSound.play(0.25)
            var enemySelected = enemies.chooseRandomEnemy()
            var newBullet = EnemyBullet()
            newBullet.currX = enemySelected.currX
            newBullet.currY = enemySelected.currY
            children.add(newBullet)
            newBullet.layoutX = newBullet.currX
            newBullet.layoutY = newBullet.currY
            enemyBullets.add(newBullet)

    }

    override fun draw() {
        if (player.lives == 0) {
            children.clear()
            gameOver = true
            var endScreen = Pane(ImageView("ran out of lives.png"))
            children.add(endScreen)
            endScreen.layoutX = 0.0
            endScreen.layoutY = 0.0
            children.add(scoreLabel)
            scoreLabel.layoutX = 610.0
            scoreLabel.layoutY = 225.0
        } else if (gameEnd) {
            children.clear()
            var endScreen = Pane(ImageView("win screen.png"))
            children.add(endScreen)
            endScreen.layoutX = 0.0
            endScreen.layoutY = 0.0
            children.add(scoreLabel)
            scoreLabel.layoutX = 610.0
            scoreLabel.layoutY = 225.0
        } else if (gameOver) {
            children.clear()
            var endScreen = Pane(ImageView("enemy bottom screen.png"))
            children.add(endScreen)
            endScreen.layoutX = 0.0
            endScreen.layoutY = 0.0
            children.add(scoreLabel)

            scoreLabel.layoutX = 610.0
            scoreLabel.layoutY = 225.0
        }

        else if (!enemies.allGone && resetTimer <= 0.0 && !player.playerDead) {

            scoreLabel.text = "Score: $score"
            livesLabel.text = "Lives: ${player.lives}"
            levelLabel.text = "Level: $currentLevel"
            if (this.playerMovingLeft) {
                if (this.playerX - 15.0 <= 0) {
                    this.playerX = 0.0
                } else {
                    this.playerX -= PLAYERMOVESPEED
                }
            } else if (this.playerMovingRight) {
                if (this.playerX + player.playerWidth >= WIDTH) {
                    this.playerX = WIDTH - player.playerWidth
                } else {
                    this.playerX += PLAYERMOVESPEED
                }
            }

            if (this.playerShooting) {
                this.shootBullet()
            }



            player.currX = playerX
            player.currY = playerY
            gamePane.layoutX = playerX
            gamePane.layoutY = playerY


            if (enemies.movingLeft) {
                enemies.currX += enemies.speed
            } else {
                enemies.currX -= enemies.speed
            }

            var hitBoundary = enemies.redrawMatrix()
            if (hitBoundary) {
                if (enemyBulletCounter <= 0) {
                    enemyBulletCounter = 20.0
                    shootEnemyBullet()
                }
            }
            if (randomAlienShotTimer == 0.0) {
                shootEnemyBullet()
                randomAlienShotTimer = alienShotTimerStart
            }
            if (randomAlienShotTimer2 == 0.0) {
                shootEnemyBullet()
                randomAlienShotTimer2 = alienShotTimerStart
            }
            if (randomAlienShotTimer3 == 0.0) {
                shootEnemyBullet()
                randomAlienShotTimer3 = alienShotTimerStart
            }

            for (i in 0 until enemies.height) {
                for (j in 0 until enemies.width) {
                    enemies.matrix[i][j].layoutX = enemies.matrix[i][j].currX
                    enemies.matrix[i][j].layoutY = enemies.matrix[i][j].currY
                    var isPlayerHit = player.hitTest(enemies.matrix[i][j].currX,
                        enemies.matrix[i][j].currY, enemies.matrix[i][j].enemyWidth, enemies.matrix[i][j].enemyHeight
                    ) && !enemies.matrix[i][j].isDestroyed
                    if (isPlayerHit) {
                        player.killPlayer()
                        if (player.lives == 0) {
                            gameOverSound.play()
                        } else {
                            playerDeathSound.play()
                        }
                    }
                    if (enemies.matrix[i][j].currY >= HEIGHT - 50.0 && !enemies.matrix[i][j].isDestroyed) {
                        gameOver = true
                        gameOverSound.play()
                    }
                }
            }

            if (playerBullets.isNotEmpty()) {
                for (i in playerBullets.size - 1 downTo 0) {
                    playerBullets[i].currY -= BULLETSPEED
                    playerBullets[i].layoutY = playerBullets[i].currY
                    var enemyHit = enemies.isEnemyHit(
                        playerBullets[i].currX,
                        playerBullets[i].currY,
                        playerBullets[i].bulletWidth,
                        playerBullets[i].bulletHeight
                    )
                    if (enemyHit) {
                        score += ENEMYPOINTS
                        enemyDeathSound.play(0.5)
                    }
                    enemyHit = enemyHit && !yeezyMode

                    if (playerBullets[i].currY + playerBullets[i].bulletHeight < 0 || enemyHit) {
                        children.remove(playerBullets[i])
                        playerBullets.remove(playerBullets[i])
                    }
                }
            }

            if (enemyBullets.isNotEmpty()) {
                for (i in enemyBullets.size - 1 downTo 0) {
                    enemyBullets[i].currY += bulletSpeed
                    enemyBullets[i].layoutY = enemyBullets[i].currY
                    var isPlayerHit = player.hitTest(enemyBullets[i].currX,
                        enemyBullets[i].currY, enemyBullets[i].bulletWidth, enemyBullets[i].bulletHeight
                    )
                    if (enemyBullets[i].currY > HEIGHT || isPlayerHit) {
                        children.remove(enemyBullets[i])
                        enemyBullets.remove(enemyBullets[i])
                    }
                    if (isPlayerHit) {
                        player.killPlayer()
                        playerDeathSound.play()
                    }
                }
            }


        } else if (player.playerDead) {
            if (this.resetTimer == 0.0) {
                resetTimer = 120.0
                if (playerBullets.isNotEmpty()) {
                    for (i in playerBullets.size - 1 downTo 0) {
                        children.remove(playerBullets[i])
                        playerBullets.remove(playerBullets[i])
                    }
                }
                if (enemyBullets.isNotEmpty()) {
                    for (i in enemyBullets.size - 1 downTo 0) {
                        children.remove(enemyBullets[i])
                        enemyBullets.remove(enemyBullets[i])
                    }
                }

            } else if (this.resetTimer == 1.0) {
                player.resetPlayer()
                playerX = player.currX
                playerY = player.currY
                gamePane.layoutX = playerX
                gamePane.layoutY = playerY
            }
        }

        else { // the player killed all the enemies
            if (this.resetTimer == 0.0) {
                resetTimer = 120.0
                if (playerBullets.isNotEmpty()) {
                    for (i in playerBullets.size - 1 downTo 0) {
                        children.remove(playerBullets[i])
                        playerBullets.remove(playerBullets[i])
                    }
                }
                if (enemyBullets.isNotEmpty()) {
                    for (i in enemyBullets.size - 1 downTo 0) {
                        children.remove(enemyBullets[i])
                        enemyBullets.remove(enemyBullets[i])
                    }
                }
                enemies.resetEnemies()
                for (i in 0 until enemies.height) {
                    for (j in 0 until enemies.width) {
                        enemies.matrix[i][j].layoutX = enemies.matrix[i][j].currX
                        enemies.matrix[i][j].layoutY = enemies.matrix[i][j].currY
                    }
                }
                currentLevel++
                if (currentLevel == 1) {
                    enemies.speed = LEVELONE_SPEED
                    alienShotTimerStart = LEVELONE_SHOT_TIMER
                    randomAlienShotTimer = LEVELONE_SHOT_TIMER
                    bulletSpeed = LEVELONE_SHOT_SPEED
                    randomAlienShotTimer2 = -1.0
                    randomAlienShotTimer3 = -1.0
                    numSmartEnemies = LEVELONE_SMART_ENEMY
                } else if (currentLevel == 2) {
                    enemies.speed = LEVELTWO_SPEED
                    alienShotTimerStart = LEVELTWO_SHOT_TIMER
                    randomAlienShotTimer = LEVELONE_SHOT_TIMER
                    bulletSpeed = LEVELTWO_SHOT_SPEED
                    randomAlienShotTimer2 = LEVELTWO_SHOT_TIMER
                    randomAlienShotTimer3 = -1.0
                    numSmartEnemies = LEVELTWO_SMART_ENEMY
                } else if (currentLevel == 3) {
                    enemies.speed = LEVELTHREE_SPEED
                    alienShotTimerStart = LEVELTHREE_SHOT_TIMER
                    bulletSpeed = LEVELTHREE_SHOT_SPEED
                    randomAlienShotTimer = LEVELONE_SHOT_TIMER
                    randomAlienShotTimer2 = LEVELTWO_SHOT_TIMER
                    randomAlienShotTimer3 = LEVELTHREE_SHOT_TIMER
                    numSmartEnemies = LEVELTHREE_SMART_ENEMY
                } else {
                    gameEnd = true
                    gameWinSound.play()
                }
                player.resetPlayer()
                playerX = player.currX
                playerY = player.currY
                gamePane.layoutX = playerX
                gamePane.layoutY = playerY
            } else if (this.resetTimer == 1.0) {
                enemies.makeEnemiesVisible()
            }
        }
    }
}


// from Jeff Avery Github Timer Demo
fun animationTimerDemo(aniScene: AnimatableScene) {
    // create timer using JavaFX 60FPS execution thread
    val timer: AnimationTimer = object : AnimationTimer() {
        override fun handle(now: Long) {
            aniScene.x += 1.0       // animate parameter
            if (aniScene.resetTimer > 0) {
                aniScene.resetTimer -= 1.0
            } else {
                if (aniScene.bulletCounter > 0) {
                    aniScene.bulletCounter -= 1.0
                }
                if (aniScene.enemyBulletCounter > 0) {
                    aniScene.enemyBulletCounter -= 1.0
                }

                if (aniScene.randomAlienShotTimer > 0) {
                    aniScene.randomAlienShotTimer -= 1.0
                }
                if (aniScene.randomAlienShotTimer2 > 0) {
                    aniScene.randomAlienShotTimer -= 1.0
                }
                if (aniScene.randomAlienShotTimer3 > 0) {
                    aniScene.randomAlienShotTimer -= 1.0
                }
            }
            aniScene.draw()         // redraw updated scene
        }
    }
    // start timer
    timer.start()
}

fun loadingScreen(stage: Stage): Scene {
    val image = ImageView("Kanye Quest.png")
    val scene = Scene(Pane(image), WIDTH, HEIGHT)
    scene.setOnKeyPressed {
        if (it.code == KeyCode.Q) {
            stage.close()
        } else if (it.code == KeyCode.ENTER) {
            stage.scene = gameStart(stage, 1)
        } else if (it.code == KeyCode.DIGIT1) {
            stage.scene = gameStart(stage, 1)
        } else if (it.code == KeyCode.DIGIT2) {
            stage.scene = gameStart(stage, 2)
        } else if (it.code == KeyCode.DIGIT3) {
            stage.scene = gameStart(stage, 3)
        }
    }
    return scene
}


fun gameStart(stage: Stage, level: Int): Scene {
    val aniScene = GameScene(level)
    animationTimerDemo(aniScene)
    val scene = Scene(Pane(aniScene), WIDTH, HEIGHT)
    scene.fill = Color.BLACK
    scene.setOnKeyPressed {
        if (it.code == KeyCode.A || it.code == KeyCode.LEFT) {
            aniScene.playerMovingLeft = true
            aniScene.playerMovingRight = false
        } else if (it.code == KeyCode.D || it.code == KeyCode.RIGHT) {
            aniScene.playerMovingLeft = false
            aniScene.playerMovingRight = true
        } else if (it.code == KeyCode.SPACE) {
            aniScene.playerShooting = true
        } else if (it.code == KeyCode.R) {
            aniScene.restartGame()
        } else if (it.code == KeyCode.Q) {
            if (aniScene.quit()) {
                stage.close()
            }
        } else if (it.code == KeyCode.Y) {
            aniScene.giveYeezyMode()
        }
    }

    scene.setOnKeyReleased {
        if (it.code == KeyCode.A || it.code == KeyCode.LEFT) {
            aniScene.playerMovingLeft = false
            aniScene.playerMovingRight = false
        } else if (it.code == KeyCode.D || it.code == KeyCode.RIGHT) {
            aniScene.playerMovingLeft = false
            aniScene.playerMovingRight = false
        } else if (it.code == KeyCode.SPACE) {
            aniScene.playerShooting = false
        }
    }

    return scene
}

