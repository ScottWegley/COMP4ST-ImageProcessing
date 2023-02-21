package Library;
import java.awt.image.BufferedImage;

public class ImageEditor {
    public static enum OPERATION {
        ADD, SUB, MUL, DIV
    }

    public static int[][][] alter(int[][][] img, double[] scalars, OPERATION op) {
        int p = img.length;
        int h = img[0].length;
        int w = img[0][0].length;

        int[][][] output = new int[p][h][w];

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < h; j++) {
                for (int q = 0; q < w; q++) {
                    double pixel = img[i][j][q];
                    switch (op) {
                        case ADD:
                            pixel += scalars[i];
                            break;
                        case SUB:
                            pixel -= scalars[i];
                            break;
                        case MUL:
                            pixel *= scalars[i];
                            break;
                        case DIV:
                            pixel /= scalars[i];
                            break;
                    }
                    output[i][j][q] = (int) (pixel < 0 ? 0 : pixel > 255 ? 255 : pixel);
                }
            }
        }

        return output;
    }

    public static int[][][] alter(int[][][] img, int[][][] img1, OPERATION op) {
        int p = img.length;
        int h = img[0].length;
        int w = img[0][0].length;

        int[][][] output = new int[p][h][w];

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < h; j++) {
                for (int q = 0; q < w; q++) {
                    double pixel = img[i][j][q];
                    switch (op) {
                        case ADD:
                            pixel += img1[i][j][q];
                            break;
                        case SUB:
                            pixel -= img1[i][j][q];
                            break;
                        case MUL:
                            pixel *= img1[i][j][q];
                            break;
                        case DIV:
                            pixel /= img1[i][j][q];
                            break;
                    }
                    output[i][j][q] = (int) (pixel < 0 ? 0 : pixel > 255 ? 255 : pixel);
                }
            }
        }

        return output;
    }

    public static int[][][] binarize(int[][][] img, int[] thresholds /*, boolean auto */) {
        int p = img.length;
        int h = img[0].length;
        int w = img[0][0].length;

        int[][][] output = new int[p][h][w];

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < h; j++) {
                for (int q = 0; q < w; q++) {
                    output[i][j][q] = (img[i][j][q] < thresholds[i] ? 0 : 255);
                }
            }
        }

        return output;
    }

    public static int[][][] lookup(int[][][] img, int[][] lut) {
        int p = img.length;
        int h = img[0].length;
        int w = img[0][0].length;

        int[][][] output = new int[p][h][w];

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < h; j++) {
                for (int q = 0; q < w; q++) {
                    int pixel = lut[i][img[i][j][q]];
                    output[i][j][q] = (pixel < 0 ? 0 : pixel > 255 ? 255 : pixel);
                }
            }
        }

        return output;
    }

    public static int[][][] alpha(int[][][] img, int[][][] img1, double alp[]) {
        int p = img.length;
        int h = img[0].length;
        int w = img[0][0].length;

        int[][][] output = new int[p][h][w];

        for (int i = 0; i < p; i++) {
            for (int j = 0; j < h; j++) {
                for (int q = 0; q < w; q++) {
                    double pixel = alp[i] * img[i][j][q] + (1.0 - alp[i]) * img1[i][j][q];
                    output[i][j][q] = (int) (pixel < 0 ? 0 : pixel > 255 ? 255 : pixel);
                }
            }
        }

        return output;
    }

    public static int[][][] bi2int(BufferedImage bi) {
        int intimg[][][] = new int[3][bi.getHeight()][bi.getWidth()];
        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                int argb = bi.getRGB(x, y);
                intimg[0][y][x] = (argb >> 16) & 0xFF; // -- RED
                intimg[1][y][x] = (argb >> 8) & 0xFF; // -- GREEN
                intimg[2][y][x] = (argb >> 0) & 0xFF; // -- BLUE
            }
        }
        return intimg;
    }

    public static BufferedImage int2bi(int[][][] intimg) {
        BufferedImage bi = new BufferedImage(intimg[0][0].length, intimg[0].length, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                int rgb = (intimg[0][y][x] << 16) |
                        (intimg[1][y][x] << 8) |
                        (intimg[2][y][x] << 0);
                bi.setRGB(x, y, rgb);
            }
        }
        return bi;
    }
}
