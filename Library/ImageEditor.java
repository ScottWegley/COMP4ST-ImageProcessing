package Library;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageEditor {
    public static enum OPERATION {
        ADD, SUB, MUL, DIV
    }

    public static int[][] convolve(int in[][], double[][] mask, boolean normalize, boolean clamp)
            throws IllegalArgumentException {
        int mheight = mask.length;
        int mwidth = mask[0].length;
        if (mheight % 2 == 0 || mwidth % 2 == 0) {
            throw new IllegalArgumentException("Mask size must be odd");
        }

        if (normalize) {
            double sum = 0.0;
            for (int i = 0; i < mask.length; i++) {
                for (int j = 0; j < mask[0].length; j++) {
                    sum += mask[i][j];
                }
            }
            for (int i = 0; i < mask.length; i++) {
                for (int j = 0; j < mask[0].length; j++) {
                    mask[i][j] /= sum;
                }
            }
        }

        int height = in.length;
        int width = in[0].length;

        int out[][] = new int[height][width];

        // Two loops for getting every pixel;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double sum = 0;

                for (int k = -mheight / 2; k <= mheight / 2; k++) {
                    for (int l = -mwidth / 2; l <= mwidth / 2; l++) {
                        try {
                            sum += in[i + k][j + l] * mask[k + mheight / 2][l + mwidth / 2];
                        } catch (ArrayIndexOutOfBoundsException e) {
                        }
                    }
                }
                if (clamp) {
                    out[i][j] = (int) (sum < 0 ? 0 : (sum > 255 ? 255 : sum));
                } else {
                    out[i][j] = (int) sum;
                }
            }
        }

        return out;
    }

    public static int[][][] scale(int[][][] img, double outMin, double outMax) {
        double[] mins = new double[] { 255, 255, 255 };
        double[] maxs = new double[] { 0, 0, 0 };
        int[][][] out = new int[img.length][img[0].length][img[0][0].length];
        for (int i = 0; i < img[0].length; i++) {
            for (int j = 0; j < img[0][0].length; j++) {
                if (img[0][i][j] > maxs[0]) {
                    maxs[0] = img[0][i][j];
                }
                if (img[0][i][j] < mins[0]) {
                    mins[0] = img[0][i][j];
                }
                if (img[1][i][j] > maxs[1]) {
                    maxs[1] = img[1][i][j];
                }
                if (img[1][i][j] < mins[1]) {
                    mins[1] = img[1][i][j];
                }
                if (img[2][i][j] > maxs[2]) {
                    maxs[2] = img[2][i][j];
                }
                if (img[2][i][j] < mins[2]) {
                    mins[2] = img[2][i][j];
                }
            }
        }
        double[] sfs = { (outMax - outMin) / (maxs[0] - mins[0]),
                (outMax - outMin) / (maxs[1] - mins[1]),
                (outMax - outMin) / (maxs[2] - mins[2]) };
        for (int plane = 0; plane < 3; plane++) {
            for (int i = 0; i < img[0].length; i++) {
                for (int j = 0; j < img[0][0].length; j++) {
                    double result = sfs[plane] * (img[plane][i][j] - mins[plane]) + outMin;
                    out[plane][i][j] = (int) (result > 255 ? 255 : (result < 0 ? 0 : result));
                }
            }
        }
        return out;
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

    public static int[][][] binarize(int[][][] img, int[] thresholds /* , boolean auto */) {
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

    public static int otsu(int[][] img) {
        int height = img.length;
        int width = img[0].length;

        // -- histogram
        int[] histogram = new int[256];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                histogram[img[i][j]]++;
            }
        }

        double pixeltotal = 0;
        // -- the total sum of all image pixel values
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                pixeltotal += img[i][j];
            }
        }

        double sumB = 0;
        int wB = 0;
        int wF = 0;

        // -- the resultant (selected) threshold
        int threshold = 0;

        double varMax = 0;

        // -- loop through all possible thresholds
        for (int t = 0; t < histogram.length; t++) {

            // -- set the background weight to number of pixels
            // in the histogram below the current threshold
            // Note: this sum accumulates over loop indices
            wB += histogram[t];

            // -- if wB == 0 then there are no pixels in the background class, try next
            // threshold
            if (wB == 0)
                continue;

            // -- set the foreground weight to the remaining
            // number of pixels (
            wF = (height * width) - wB;

            // -- if wF == 0 all pixels are in the foreground class, done
            if (wF == 0)
                break;

            // -- sum of pixel values at this threshold
            sumB += (double) (t * histogram[t]);

            // -- mean of the background for this threshold
            double mB = sumB / wB;

            // -- mean of the foreground for this threshold
            double mF = (pixeltotal - sumB) / wF;

            // -- Variance is a measure of dispersion, how far a set of numbers
            // is spread out from their average value.
            // Between class variance is a measure of how far one set mean
            // is from the other
            // p(0) * p(1) * (mean(0) - mean(1))^2
            double varBetween = (double) wB * (double) wF * (mB - mF) * (mB - mF);
            // -- look to maximize the variance between the two classes which
            // will give us the maximal distance between the means of the two classes
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
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

    public static BufferedImage readImg(String path) throws IOException{
        return ImageIO.read(new File(path));
    }

    public static void writePNG(BufferedImage bi, String path) throws IOException{
        ImageIO.write(bi, "PNG", new File(path));
    }
}
