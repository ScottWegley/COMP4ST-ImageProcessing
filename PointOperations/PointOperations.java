package PointOperations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class PointOperations {

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

    public static int[][][] binarize(int[][][] img, int[] thresholds, boolean auto) {
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

    public static void main(String[] args) {
        BufferedImage bi;
        String filename = ("C:\\Code\\CSC4ST-ImageProcessing\\PointOperations\\GrayImage.PNG");
        try {
            bi = ImageIO.read(new File(filename));
            int img[][][] = bi2int(bi);

            // Compute histogram of original image
            int[][] histograms = new int[3][256];
            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    int argb = bi.getRGB(j, i);
                    int red = (argb >> 16) & 0xFF; // -- RED
                    int green = (argb >> 8) & 0xFF; // -- GREEN
                    int blue = (argb >> 0) & 0xFF; // -- BLUE
                    histograms[0][red]++;
                    histograms[1][green]++;
                    histograms[2][blue]++;
                }
            }

            // Store statistics of original image.
            float[] redStat = new float[] { 255, 0, 0, 0, 0, 0 };
            float[] grnStat = new float[] { 255, 0, 0, 0, 0, 0 };
            float[] bluStat = new float[] { 255, 0, 0, 0, 0, 0 };
            int rMed = 0;
            int gMed = 0;
            int bMed = 0;

            int[] fifthPercentile = new int[3];
            int[] ninetyPercentile = new int[3];

            long maxPixels = 0;

            for (int i = 0; i < histograms[0].length; i++) {
                // Checking for highest pixel count
                if (histograms[0][i] > maxPixels) {
                    maxPixels = histograms[0][i];
                }
                if (histograms[1][i] > maxPixels) {
                    maxPixels = histograms[1][i];
                }
                if (histograms[2][i] > maxPixels) {
                    maxPixels = histograms[2][i];
                }
                // Checking for new maximum bin
                if (histograms[0][i] != 0) {
                    redStat[1] = i;
                }
                if (histograms[0][255 - i] != 0) {
                    redStat[0] = 255 - i;
                }
                if (histograms[1][i] != 0) {
                    grnStat[1] = i;
                }
                // Checking for new minimum
                if (histograms[1][255 - i] != 0) {
                    grnStat[0] = 255 - i;
                }
                if (histograms[2][i] != 0) {
                    bluStat[1] = i;
                }
                if (histograms[2][255 - i] != 0) {
                    bluStat[0] = 255 - i;
                }
                // Calculating the mean.
                redStat[2] += i * histograms[0][i];
                grnStat[2] += i * histograms[1][i];
                bluStat[2] += i * histograms[2][i];
                // Standard Deviation
                redStat[3] += i * i * histograms[0][i];
                grnStat[3] += i * i * histograms[1][i];
                bluStat[3] += i * i * histograms[2][i];
                // Calculating the Median
                redStat[4] += histograms[0][i];
                grnStat[4] += histograms[1][i];
                bluStat[4] += histograms[2][i];
                if (redStat[4] <= (bi.getWidth() * bi.getHeight()) / 2) {
                    rMed = i;
                }
                if (grnStat[4] <= (bi.getWidth() * bi.getHeight()) / 2) {
                    gMed = i;
                }
                if (bluStat[4] <= (bi.getWidth() * bi.getHeight()) / 2) {
                    bMed = i;
                }
                // Calculating 5th Percentile
                if (redStat[4] <= (bi.getWidth() * bi.getHeight()) * .05) {
                    fifthPercentile[0] = i;
                }
                if (grnStat[4] <= (bi.getWidth() * bi.getHeight()) * .05) {
                    fifthPercentile[1] = i;
                }
                if (bluStat[4] <= (bi.getWidth() * bi.getHeight()) * .05) {
                    fifthPercentile[2] = i;
                }
                // Calculating 90th Percentile
                if (redStat[4] <= (bi.getWidth() * bi.getHeight()) * .9) {
                    ninetyPercentile[0] = i;
                }
                if (grnStat[4] <= (bi.getWidth() * bi.getHeight()) * .9) {
                    ninetyPercentile[1] = i;
                }
                if (bluStat[4] <= (bi.getWidth() * bi.getHeight()) * .9) {
                    ninetyPercentile[2] = i;
                }
                // Calculating the Mode
                if (histograms[0][i] > histograms[0][(int) redStat[5]]) {
                    redStat[5] = i;
                }
                if (histograms[1][i] > histograms[1][(int) grnStat[5]]) {
                    grnStat[5] = i;
                }
                if (histograms[2][i] > histograms[2][(int) bluStat[5]]) {
                    bluStat[5] = i;
                }
            }

            redStat[3] = (float) Math.sqrt((redStat[3] - ((redStat[2] * redStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));
            grnStat[3] = (float) Math.sqrt((grnStat[3] - ((grnStat[2] * grnStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));
            bluStat[3] = (float) Math.sqrt((bluStat[3] - ((bluStat[2] * bluStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));

            redStat[2] /= bi.getHeight() * bi.getWidth();
            grnStat[2] /= bi.getHeight() * bi.getWidth();
            bluStat[2] /= bi.getHeight() * bi.getWidth();

            redStat[4] = rMed;
            grnStat[4] = gMed;
            bluStat[4] = bMed;

            // Print Statistics of original image.
            System.out.println("Original Statistics");
            System.out.println("       min:   max:    mean:   dev:  median: mode: ");
            System.out.print("red:   ");
            for (int i = 0; i < redStat.length; i++) {
                System.out.print(String.format("%.2f", redStat[i]) + "  ");
            }
            System.out.print("\ngrn:   ");
            for (int i = 0; i < grnStat.length; i++) {
                System.out.print(String.format("%.2f", grnStat[i]) + "  ");
            }
            System.out.print("\nblu:   ");
            for (int i = 0; i < bluStat.length; i++) {
                System.out.print(String.format("%.2f", bluStat[i]) + "  ");
            }

            System.out.println();
            // Scale original Image
            float factor = (255 - 0) / (ninetyPercentile[1] - fifthPercentile[1]);
            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    for (int plane = 0; plane < 3; plane++) {
                        double result = factor * (img[plane][i][j] - fifthPercentile[1]);
                        img[plane][i][j] = (int) (result > 255 ? 255 : (result < 0 ? 0 : result));
                    }
                }
            }
            bi = int2bi(img);
            // Draw Modified Image
            BufferedImage outImg = new BufferedImage(bi.getWidth(), bi.getHeight() + 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) outImg.getGraphics();
            g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), 0, 0, bi.getWidth(), bi.getHeight(), Color.BLACK,
                    null);
            g2d.fillRect(0, bi.getHeight(), bi.getWidth(), 200);
            // Calculate Modified Histogram
            histograms = new int[3][256];
            for (int i = 0; i < bi.getHeight(); i++) {
                for (int j = 0; j < bi.getWidth(); j++) {
                    histograms[0][img[0][i][j]]++;
                    histograms[1][img[1][i][j]]++;
                    histograms[2][img[2][i][j]]++;
                }
            }
            // Calculate Modified Statistics
            redStat = new float[] { 255, 0, 0, 0, 0, 0 };
            grnStat = new float[] { 255, 0, 0, 0, 0, 0 };
            bluStat = new float[] { 255, 0, 0, 0, 0, 0 };
            rMed = 0;
            gMed = 0;
            bMed = 0;
            maxPixels = 0;
            for (int i = 0; i < histograms[0].length; i++) {
                // Checking for highest pixel count
                if (histograms[0][i] > maxPixels) {
                    maxPixels = histograms[0][i];
                }
                if (histograms[1][i] > maxPixels) {
                    maxPixels = histograms[1][i];
                }
                if (histograms[2][i] > maxPixels) {
                    maxPixels = histograms[2][i];
                }
                // Checking for new maximum bin
                if (histograms[0][i] != 0) {
                    redStat[1] = i;
                }
                if (histograms[0][255 - i] != 0) {
                    redStat[0] = 255 - i;
                }
                if (histograms[1][i] != 0) {
                    grnStat[1] = i;
                }
                // Checking for new minimum
                if (histograms[1][255 - i] != 0) {
                    grnStat[0] = 255 - i;
                }
                if (histograms[2][i] != 0) {
                    bluStat[1] = i;
                }
                if (histograms[2][255 - i] != 0) {
                    bluStat[0] = 255 - i;
                }
                // Calculating the mean.
                redStat[2] += i * histograms[0][i];
                grnStat[2] += i * histograms[1][i];
                bluStat[2] += i * histograms[2][i];
                // Standard Deviation
                redStat[3] += i * i * histograms[0][i];
                grnStat[3] += i * i * histograms[1][i];
                bluStat[3] += i * i * histograms[2][i];
                // Calculating the Median
                redStat[4] += histograms[0][i];
                grnStat[4] += histograms[1][i];
                bluStat[4] += histograms[2][i];
                if (redStat[4] <= (bi.getWidth() * bi.getHeight()) / 2) {
                    rMed = i;
                }
                if (grnStat[4] <= (bi.getWidth() * bi.getHeight()) / 2) {
                    gMed = i;
                }
                if (bluStat[4] <= (bi.getWidth() * bi.getHeight()) / 2) {
                    bMed = i;
                }
                // Calculating the Mode
                if (histograms[0][i] > histograms[0][(int) redStat[5]]) {
                    redStat[5] = i;
                }
                if (histograms[1][i] > histograms[1][(int) grnStat[5]]) {
                    grnStat[5] = i;
                }
                if (histograms[2][i] > histograms[2][(int) bluStat[5]]) {
                    bluStat[5] = i;
                }
            }

            redStat[3] = (float) Math.sqrt((redStat[3] - ((redStat[2] * redStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));
            grnStat[3] = (float) Math.sqrt((grnStat[3] - ((grnStat[2] * grnStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));
            bluStat[3] = (float) Math.sqrt((bluStat[3] - ((bluStat[2] * bluStat[2]) / (bi.getHeight() * bi.getWidth())))
                    / (bi.getHeight() * bi.getWidth()));

            redStat[2] /= bi.getHeight() * bi.getWidth();
            grnStat[2] /= bi.getHeight() * bi.getWidth();
            bluStat[2] /= bi.getHeight() * bi.getWidth();

            redStat[4] = rMed;
            grnStat[4] = gMed;
            bluStat[4] = bMed;
            // Print Modified Statistics
            System.out.println("New Statistics");
            System.out.println("       min:   max:    mean:   dev:  median: mode: ");
            System.out.print("red:   ");
            for (int i = 0; i < redStat.length; i++) {
                System.out.print(String.format("%.2f", redStat[i]) + "  ");
            }
            System.out.print("\ngrn:   ");
            for (int i = 0; i < grnStat.length; i++) {
                System.out.print(String.format("%.2f", grnStat[i]) + "  ");
            }
            System.out.print("\nblu:   ");
            for (int i = 0; i < bluStat.length; i++) {
                System.out.print(String.format("%.2f", bluStat[i]) + "  ");
            }

            System.out.println();
            // Draw Histogram
            int redOffset = 0;
            int grnOffset = 256;
            int bluOffset = 512;

            for (int i = 0; i < histograms[0].length; i++) {
                histograms[0][i] = (int) (((double) 200 / maxPixels) * histograms[0][i]);
                histograms[1][i] = (int) (((double) 200 / maxPixels) * histograms[1][i]);
                histograms[2][i] = (int) (((double) 200 / maxPixels) * histograms[2][i]);
                g2d.setColor(Color.RED);
                g2d.drawLine(redOffset + i, outImg.getHeight(), redOffset + i, outImg.getHeight() - histograms[0][i]);
                g2d.setColor(Color.GREEN);
                g2d.drawLine(grnOffset + i, outImg.getHeight(), grnOffset + i, outImg.getHeight() - histograms[1][i]);
                g2d.setColor(Color.BLUE);
                g2d.drawLine(bluOffset + i, outImg.getHeight(), bluOffset + i, outImg.getHeight() - histograms[2][i]);
            }

            ImageIO.write(outImg, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\PointOperations\\output.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}