package se233.chapter4.model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import se233.chapter4.Launcher;
import se233.chapter4.view.GameStage;

import java.nio.file.Path;

public class GameCharacter extends Pane {
    int count;
    int columns;
    int rows;
    int width;
    int height;
    public  int CHARACTER_WIDTH ;
    public  int CHARACTER_HEIGHT ;
    private static final Logger logger = LogManager.getLogger(GameCharacter.class);
    private Image gameCharacterImg;
    private AnimatedSprite imageView;
    private int x;
    private int y;
    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode upKey;
    int xVelocity = 0;
    int yVelocity = 0;
    boolean isMoveLeft = false;
    boolean isMoveRight = false;
    boolean isFalling = true;
    boolean canJump = false;
    boolean isJumping = false;
    int xAcceleration = 1;
    int yAcceleration = 1;
    int xMaxVelocity = 7;
    int yMaxVelocity = 17;
    public Image getGameCharacterImg() {
        return gameCharacterImg;
    }
    public AnimatedSprite getImageView() {
        return imageView;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public KeyCode getLeftKey() {
        return leftKey;
    }

    public KeyCode getRightKey() {
        return rightKey;
    }

    public KeyCode getUpKey() {
        return upKey;
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public int getyVelocity() {
        return yVelocity;
    }

    public boolean isFalling() {
        return isFalling;
    }

    public GameCharacter(int x, int y, int offsetX, int offsetY, KeyCode leftKey, KeyCode rightKey, KeyCode upKey, int xMaxVelocity, int yMaxVelocity, String CharterImg,int count,int columns,int rows,int width,int height,int CHARACTER_WIDTH,int CHARACTER_HEIGHT) {
        this.x = x;
        this.y = y;
        this.CHARACTER_HEIGHT=CHARACTER_HEIGHT;
        this.CHARACTER_WIDTH=CHARACTER_WIDTH;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.gameCharacterImg = new Image(Launcher.class.getResourceAsStream(CharterImg));
        this.imageView = new AnimatedSprite(this.gameCharacterImg, this.count = count, this.columns= columns, this.rows = rows, offsetX, offsetY, this.width = width, this.height = height);
        this.imageView.setFitWidth(CHARACTER_WIDTH);
        this.imageView.setFitHeight(CHARACTER_HEIGHT);
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.upKey = upKey;
        this.getChildren().addAll(this.imageView);
        this.xMaxVelocity = xMaxVelocity;
        this.yMaxVelocity = yMaxVelocity;

    }

    public void moveLeft() {
        setScaleX(-1);
        isMoveLeft = true;
        isMoveRight = false;
    }

    public void moveRight() {
        setScaleX(1);
        isMoveLeft = false;
        isMoveRight = true;
    }

    public void stop() {
        isMoveLeft = false;
        isMoveRight = false;
    }

    public void moveX() {
        setTranslateX(x);
        if (isMoveLeft) {
            xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
            x = x - xVelocity;
        }
        if (isMoveRight) {
            xVelocity = xVelocity >= xMaxVelocity ? xMaxVelocity : xVelocity + xAcceleration;
            x = x + xVelocity;
        }
    }

    public void moveY() {
        setTranslateY(y);
        if (isFalling) {
            yVelocity = yVelocity >= yMaxVelocity ? yMaxVelocity : yVelocity + yAcceleration;
            y = y + yVelocity;
        } else if (isJumping) {
            yVelocity = yVelocity <= 0 ? 0 : yVelocity - yAcceleration;
            y = y - yVelocity;
        }
    }

    public void repaint() {
        moveX();
        moveY();
    }

    public void checkReachGameWall() {
        if (x <= 0) {
            x = 0;
            logger.info("ReachGameWall", x, y, xVelocity, yVelocity);
        } else if (x + getWidth() >= GameStage.WIDTH) {
            x = GameStage.WIDTH - (int)getWidth();
            logger.info("ReachGameWall", x, y, xVelocity, yVelocity);
        }
    }

    public void jump() {
        if (canJump) {
            yVelocity = yMaxVelocity;
            canJump = false;
            isJumping = true;
            isFalling = false;
        }
    }

    public void checkReachHighest() {
        if (isJumping && yVelocity <= 0) {
            isJumping = false;
            isFalling = true;
            yVelocity = 0;
        }
    }

    public void checkReachFloor() {
        if (isFalling && y >= GameStage.GROUND - CHARACTER_HEIGHT) {
            isFalling = false;
            canJump = true;
            yVelocity = 0;
        }
    }

    public void trace() {
        logger.info("X:{}, Y:{} VX:{} VY:{}", x, y, xVelocity, yVelocity);
    }
}