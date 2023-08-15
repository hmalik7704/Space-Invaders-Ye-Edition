import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane

private var counter = 0

class Enemy: Pane() {
    var currX: Double = 0.0
    var currY: Double = 0.0
    var speed: Double = 1.0
    var enemyWidth: Double = 50.0
    var enemyHeight: Double = 25.0
    val enemyImage = ImageView()
    var isDestroyed = false
    var value = ENEMYPOINTS

    init {
        var imageToAdd: Image? = null
        if (counter % 2 == 0) {
            imageToAdd = Image("enemy.png")
        } else {
            imageToAdd = Image("enemy2.png")
        }
        counter++
        enemyImage.image = imageToAdd
        children.add(enemyImage)
    }

    fun resetEnemy() {
        enemyImage.isVisible = true
        isDestroyed = false
    }

    fun setInvisible() {
        enemyImage.isVisible = false
        isDestroyed = true
    }

    fun hitTest(inputX: Double, inputY: Double, projWidth: Double, projHeight: Double): Boolean {
        var xBound = currX + this.enemyWidth
        var yBound = currY + this.enemyHeight
        if (currX < inputX + projWidth && inputX < xBound && currY < inputY + projHeight && inputY < yBound) {

            return true
        }
        return false
    }

    fun draw() {

    }
}

class EnemyGrid() {
    var currX: Double = 1.0
    var currY: Double = 75.0
    var endX: Double = 0.0
    var endY: Double = 0.0
    var speed: Double = 1.0
    var allGone: Boolean = false
    var movingLeft: Boolean = true
    var spacing: Double = 60.0 //spacing of each enemy in the matrix
    var ySpacing: Double = 35.0
    var width = ENEMYWIDTH //width of the array
    var height = ENEMYHEIGHT //height of the array
    var leftMostColumn = 0
    var rightMostColumn = width-1
    var bottomRow = height-1
    var matrix = Array(height) {Array(width) {Enemy()} }

    init {
        redrawMatrix()
    }

    fun destroyEnemy(row: Int, column: Int) {
        matrix[row][column].setInvisible()
        var columnEmpty = false
        leftMostColumn = 0
        for (i in 0 until width) {
            for (j in 0 until height) {
                columnEmpty = matrix[j][i].isDestroyed
                if (!columnEmpty) {
                    break
                }
            }
            if (columnEmpty) {
                leftMostColumn += 1
            } else {
                break
            }
        }


        columnEmpty = false
        rightMostColumn = width-1
        for (i in width-1 downTo  0) {
            for (j in height-1 downTo 0) {
                columnEmpty = matrix[j][i].isDestroyed
                if (!columnEmpty) {
                    break
                }
            }
            if (columnEmpty) {
                rightMostColumn -= 1
            } else {
                break
            }
        }
        
        var rowEmpty = false
        for (i in 0 until width) {
            rowEmpty = matrix[bottomRow][i].isDestroyed
            if (!rowEmpty) {
                break
            }
        }
        if (rowEmpty) {
            bottomRow -= 1
        }
        //check if all aliens dead
        var everyoneDead = false
        this.speed += SPEEDINCREASE //increase the speed of all people when one dies
        for (i in 0 until height) {
            for (j in 0 until width) {

                everyoneDead = matrix[i][j].isDestroyed
                if (!everyoneDead) {
                    break
                }
            }
            if (!everyoneDead) {
                break
            }
        }

        //all aliens gone
        if (everyoneDead) {
            allGone = true

        }

    }

    fun chooseRandomEnemy(): Enemy {
        var rowRand = (0..height-1).random()
        var columnRand = (0..width-1).random()
        if (!allGone) {
            while (matrix[rowRand][columnRand].isDestroyed) {
                rowRand = (0..height - 1).random()
                columnRand = (0..width - 1).random()
            }
        }
        return matrix[rowRand][columnRand]
    }

    fun isEnemyHit(inputX: Double, inputY: Double, projWidth: Double, projHeight: Double): Boolean {
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (matrix[i][j].hitTest(inputX, inputY, projWidth, projHeight) && !matrix[i][j].isDestroyed) {
                    destroyEnemy(i, j)
                    return true
                }
            }
        }
        return false
    }

    fun redrawMatrix(): Boolean {
        var returnValue = false
        if (this.endX >= WIDTH ) {
            this.currY += ENEMYDESCENDAMOUNT
            this.movingLeft = false
            this.currX -= 10.0
            returnValue = true
        } else if (this.currX + leftMostColumn * spacing <= 0.0) {
            this.currY += ENEMYDESCENDAMOUNT
            this.movingLeft = true
            this.currX += 10.0
            returnValue = true
        }

        for (i in 0 until height) {
            for (j in 0 until width) {
                matrix[i][j].currX = this.currX + j * this.spacing
                matrix[i][j].currY = this.currY + i * this.ySpacing
            }
        }
        this.endX = this.currX + (this.rightMostColumn + 1) * this.spacing
        this.endY = this.currY + (this.bottomRow + 1) * this.ySpacing
        return returnValue
    }

    fun resetEnemies() {
        this.currX = 1.0
        this.currY = 75.0
        this.allGone = false
        this.movingLeft = true
        /*for (i in 0 until height) {
            for (j in 0 until width) {
                matrix[i][j].resetEnemy()
            }
        }*/
        this.leftMostColumn = 0
        this.rightMostColumn = width-1
        this.bottomRow = height-1
        this.redrawMatrix()
    }

    fun makeEnemiesVisible() {
        for (i in 0 until height) {
            for (j in 0 until width) {
                matrix[i][j].resetEnemy()
            }
        }
    }
}