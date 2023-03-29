package Tetris;

import Main.Game;
import Worlds.Worlds;

import java.awt.*;

public class Tetris extends Worlds {
    private final int WIDTH = 10;
    private final int HEIGHT = 20;
    private int[][] map = new int[WIDTH][HEIGHT];
    private final int BLOCK_SIZE = 25;
    private long timer = System.currentTimeMillis();
    private boolean keyPressed = false;
    /*
    0:  empty

    1:  static o
    2:  static i
    3:  static s
    4:  static z
    5:  static l
    6:  static j
    7:  static t

    8:  moving o
    9:  moving i
    10: moving s
    11: moving z
    12: moving l
    13: moving j
    14: moving t
     */

    /**
     * Constructor
     */
    public Tetris(Game game) {
        super(game);
        game.getDisplay().resize(1000, 1000);
    }

    @Override
    public void tick() {
        input();
        if (!movingBlock()) {
            spawnBlock('z');
        }
        if (System.currentTimeMillis() - timer > 800) {
            moveDown();
            timer = System.currentTimeMillis();
        }
        checkLine();
    }

    private void input() {
        if (game.getKeyHandler().s) {
            moveDown();
        }
        if (game.getKeyHandler().a && !keyPressed) {
            moveLeft();
            keyPressed = true;
        }
        if (game.getKeyHandler().d && !keyPressed) {
            moveRight();
            keyPressed = true;
        }
        if (!game.getKeyHandler().a && !game.getKeyHandler().d) {
            keyPressed = false;
        }
    }

    private void spawnBlock(char block) {
        switch (block) {
            case 'o' ->{
                map[5][0] = 1;
                map[6][0] = 1;
                map[5][1] = 1;
                map[6][1] = 1;
            }
            case 'l' -> {
                map[5][0] = 4;
                map[5][1] = 4;
                map[5][2] = 4;
                map[6][2] = 4;
            }
            case 'j' ->{
                map[6][0] = 5;
                map[6][1] = 5;
                map[6][2] = 5;
                map[5][2] = 5;
            }
            case 's' -> {
                map[5][0] = 2;
                map[6][0] = 2;
                map[6][1] = 2;
                map[7][1] = 2;
            }
            case 'z' ->{
                map[5][1] = 3;
                map[6][1] = 3;
                map[6][0] = 3;
                map[7][0] = 3;
            }
            case 'i'->{
                map[5][1] = 9;
                map[5][2] = 9;
                map[5][3] = 9;
                map[5][4] = 9;
            }
            case 't' -> {
                map[5][0] = 6;
                map[6][0] = 6;
                map[7][0] = 6;
                map[6][1] = 6;
            }
        }
    }

    private boolean movingBlock() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (map[j][i] <= 7 && map[j][i] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void moveLeft() {
        if (checkLeft()) {
            for (int i = 0; i < HEIGHT; i++) {
                for (int j = 0; j < WIDTH; j++) {
                    if (map[j][i] <= 7 && map[j][i] != 0 && j - 1 >= 0) {
                        map[j - 1][i] = map[j][i];
                        map[j][i] = 0;
                    }
                }
            }
        }
    }

    private void moveRight() {
        if (checkRight()) {
            for (int i = HEIGHT - 1; i >= 0; i--) {
                for (int j = WIDTH - 1; j >= 0; j--) {
                    if (map[j][i] <= 7 && map[j][i] != 0 && j + 1 <= WIDTH - 1) {
                        map[j + 1][i] = map[j][i];
                        map[j][i] = 0;
                    }
                }
            }
        }

    }

    private void moveDown() {
        if (checkDown()) {
            for (int i = HEIGHT - 1; i >= 0; i--) {
                for (int j = 0; j < WIDTH; j++) {
                    if (map[j][i] <= 7 && map[j][i] != 0) {
                        map[j][i + 1] = map[j][i];
                        map[j][i] = 0;
                    }
                }
            }
        } else {
            makeStatic();
        }
    }

    private boolean checkDown() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (map[j][i] <= 7 && map[j][i] != 0) {
                    if (i == HEIGHT - 1 || map[j][i + 1] >= 8) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkRight() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (map[j][i] <= 7 && map[j][i] != 0) {
                    if (j == WIDTH - 1 || map[j + 1][i] >= 8) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkLeft() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (map[j][i] <= 7 && map[j][i] != 0) {
                    if (j == 0 || map[j - 1][i] >= 8) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private void checkLine() {
        for (int i = 0; i < HEIGHT; i++) {
            boolean line = true;
            for (int j = 0; j < WIDTH; j++) {
                if (map[j][i] <= 7) {
                    line = false;
                    break;
                }
            }
            if (line) {
                removeLine(i);
                moveLines(i);
            }
        }
    }

    private void removeLine(int line) {
        for (int i = 0; i < WIDTH; i++) {
            map[i][line] = 0;
        }
    }

    private void moveLines(int line) {
        for (int i = line; i >= 0; i--) {
            for (int j = 0; j < WIDTH; j++) {
                if (map[j][i] >= 8) {
                    map[j][i + 1] = map[j][i];
                    map[j][i] = 0;
                }
            }
        }
    }

    private void makeStatic() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (map[j][i] <= 7 && map[j][i] != 0) {
                    map[j][i] += 7;
                }
            }
        }
    }

    @Override
    public void render(Graphics g) {
        final int SPACING = 2 * BLOCK_SIZE;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                switch (map[j][i]){
                    default -> g.setColor(Color.WHITE);
                    case 1,8 -> g.setColor(Color.YELLOW);
                    case 2,9-> g.setColor(Color.GREEN);
                    case 3,10-> g.setColor(Color.RED);
                    case 4,11-> g.setColor(Color.ORANGE);
                    case 5,12-> g.setColor(Color.CYAN);
                    case 6,13-> g.setColor(new Color(211, 16, 211, 255));
                    case 7,14-> g.setColor(new Color(77, 0, 77));
                }
                g.fillRect(SPACING + j * BLOCK_SIZE, SPACING + i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(SPACING + j * BLOCK_SIZE, SPACING + i * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }
}