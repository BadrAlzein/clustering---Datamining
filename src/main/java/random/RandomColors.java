package random;

//here just enums of color for each cluster
public class RandomColors {

    final int YELLOW = getRGB(255, 255, 0);
    final int GREEN = getRGB(94, 255, 69);
    final int BLACK = getRGB(0, 0, 0);
    final int WHITE = getRGB(255, 255, 255);
    final int RED = getRGB(255, 0, 0);
    final int PINK = getRGB(255, 41, 205);
    final int ORANGE = getRGB(255, 173, 31);
    final int BROWN = getRGB(112, 76, 14);

    int[] colors = { WHITE, PINK, GREEN, WHITE, RED, PINK, ORANGE, BROWN, YELLOW, GREEN, BLACK, WHITE, RED, PINK,
            ORANGE, BROWN, YELLOW, GREEN, BLACK, WHITE, RED, PINK, ORANGE, BROWN, YELLOW, GREEN, BLACK, WHITE, RED,
            PINK, ORANGE, BROWN, YELLOW, GREEN, BLACK, WHITE, RED, PINK, ORANGE, BROWN,YELLOW, GREEN, BLACK, WHITE, RED, PINK, ORANGE, BROWN,YELLOW, GREEN, BLACK, WHITE, RED, PINK, ORANGE, BROWN };

    /**
     * return r-g-b -> rgb
     */
    int getRGB(int red, int green, int blue) {
        return 0xff000000 | red << 16 | green << 8 | blue;
    }

    /**
     * find a 16 bit color using an index to an array of colors
     */
    public int findColor(int index) {
        if (index > colors.length) {
            int max = colors.length;
            int min = 0;
            int range = max - min + 1;
            index = (int) (Math.random() * range) + min;

        }
        return colors[index];
    }
}
