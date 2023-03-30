package HoughCircle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Library.ImageEditor;

public class HoughCircle {

    private int radiusBins = 15;
    private int radiusStart = 80;
    private int[][] accumulator;
    private int lines, samples;

    // -- primary driver routine for finding circles
    public void Apply(int[][] _image) {
        lines = _image.length;
        samples = _image[0].length;

        accumulator = new int[lines * samples][radiusBins + 1];

        for (int i = 0; i < lines; ++i) {
            for (int j = 0; j < samples; ++j) {
                if (_image[i][j] > 0) {
                    for (int r = radiusStart; r < radiusStart + radiusBins; ++r) {
                        CalculateCirclePoints(j, i, r);
                    }
                }
            }
        }
    }

    // -- Bresenham circle, this version is for finding circles via Hough transform
    private int CalculateCirclePoints(int _x, int _y, int radius) {
        int numPoints = 0;

        int x, y;
        int d;

        x = 0;
        y = radius;
        d = 1 - radius;

        CirclePoints(x, y, _x, _y, radius);
        numPoints += 8;
        while (y > x) {
            if (d < 0) {
                d = d + (2 * x) + 3;
                ++x;
            } else {
                d = d + (2 * (x - y)) + 5;
                ++x;
                --y;
            }
            CirclePoints(x, y, _x, _y, radius);
            numPoints += 8;
        }

        return numPoints;
    }

    public void CirclePoints(int _x, int _y, int _ox, int _oy, int _r) {
        int loc; // -- linearize the image coordinates for
                 // indexing into the hough accumulator

        loc = ((_x + _oy) * samples + (_y + _ox));
        ++accumulator[loc][_r - radiusStart];
        loc = ((_y + _oy) * samples + (_x + _ox));
        ++accumulator[loc][_r - radiusStart];
        loc = ((_y + _oy) * samples + (-_x + _ox));
        ++accumulator[loc][_r - radiusStart];
        loc = ((_x + _oy) * samples + (-_y + _ox));
        ++accumulator[loc][_r - radiusStart];
        loc = ((-_x + _oy) * samples + (-_y + _ox));
        ++accumulator[loc][_r - radiusStart];
        loc = ((-_y + _oy) * samples + (-_x + _ox));
        ++accumulator[loc][_r - radiusStart];
        loc = ((-_y + _oy) * samples + (_x + _ox));
        ++accumulator[loc][_r - radiusStart];
        loc = ((-_x + _oy) * samples + (_y + _ox));
        ++accumulator[loc][_r - radiusStart];
    }

    public static void main(String[] args) {
        String filename = "C:\\Code\\CSC4ST-ImageProcessing\\HoughCircle\\KanizsaSquare.png";
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(filename));
            int[][][] outputA = ImageEditor.bi2int(bi);
            int[][][] outputB = ImageEditor.bi2int(bi);
            int[][][] RGB = ImageEditor.bi2int(bi);
            // Sobel Transform
            double[][] horMask = new double[][] { { -1, -2, -1 },
                    { 0, 0, 0 },
                    { 1, 2, 1 } };
            double[][] verMask = new double[][] { { -1, 0, 1 },
                    { -2, 0, 2 },
                    { -1, 0, 1 } };
            int[][][] horImg = new int[3][0][0];
            int[][][] verImg = new int[3][0][0];
            int[][][] magImg = new int[3][RGB[0].length][RGB[0][0].length];
            for (int i = 0; i < 3; i++) {
                horImg[i] = ImageEditor.convolve(RGB[i], horMask, false, false);
                verImg[i] = ImageEditor.convolve(RGB[i], verMask, false, false);
            }
            for (int c = 0; c < RGB.length; c++) {
                for (int i = 0; i < RGB[0].length; i++) {
                    for (int j = 0; j < RGB[0][0].length; j++) {
                        magImg[c][i][j] = (int) Math
                                .round(Math.sqrt(Math.pow(horImg[c][i][j], 2) + Math.pow(verImg[c][i][j], 2)));
                    }
                }
            }
            // Scaling
            magImg = ImageEditor.scale(magImg, 0, 255);
            ImageIO.write(ImageEditor.int2bi(magImg), "PNG",
                    new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughCircle\\scaled.png"));
            // Binarize
            int[] thresholds = new int[3];
            for (int i = 0; i < 3; i++) {
                thresholds[i] = ImageEditor.otsu(magImg[i]);
            }
            magImg = ImageEditor.binarize(magImg, thresholds);
            ImageIO.write(ImageEditor.int2bi(magImg), "PNG",
                    new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughCircle\\binarized.png"));
            HoughCircle houghcircle = new HoughCircle();
            houghcircle.Apply(magImg[0]);
            int maxacc = 0;
            int maxpos = 0;
            int maxrad = 0;
            for (int i = 0; i < houghcircle.accumulator.length; ++i) {
                for (int j = 0; j < houghcircle.accumulator[i].length; ++j) {
                    if (houghcircle.accumulator[i][j] > maxacc) {
                        maxacc = houghcircle.accumulator[i][j];
                        maxpos = i;
                        maxrad = j;
                    }
                }
            }
            // loc = (( _x + _oy) * samples + ( _y + _ox));
            System.out.println("=====Results=====");
            int y = maxpos / houghcircle.samples;
            int x = maxpos % houghcircle.samples;
            System.out.println("Maximum: " + maxacc + " " + maxpos + " " + (maxrad + houghcircle.radiusStart) + " (" + x
                    + ", " + y + ")");
            BufferedImage outBIA = ImageEditor.int2bi(outputA);
            ArrayList<MyCircle> theCircles = new ArrayList<>();
            magImg = new int[3][magImg[0].length][magImg[0][0].length];
            for (int i = 0; i < houghcircle.accumulator.length; ++i) {
                for (int j = 0; j < houghcircle.accumulator[i].length; ++j) {
                    if (houghcircle.accumulator[i][j] > 105) {
                        int y1 = i / houghcircle.samples;
                        int x1 = i % houghcircle.samples;
                        // System.out.println(houghcircle.accumulator[i][j] + " " + i + " " + (j +
                        // houghcircle.radiusStart)+ " (" + x1 + ", " + y1 + ")");
                        theCircles.add(new MyCircle(x1, y1, houghcircle.radiusStart));
                        Graphics2D g = (Graphics2D) outBIA.getGraphics();
                        g.setColor(Color.RED);
                        g.setStroke(new BasicStroke(2));
                        g.drawOval(x1 - houghcircle.radiusStart, y1 - houghcircle.radiusStart,
                                houghcircle.radiusStart * 2, houghcircle.radiusStart * 2);
                    }
                }
            }
            ImageIO.write(outBIA, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughCircle\\OUTPUTA.png"));
            double[][] means = new double[][] { { 341, 342 }, { 682, 338 }, { 345, 681 }, { 680, 684 } };
            for (int t = 0; t < 100; t++) {
                // Assign groups
                for (int i = 0; i < theCircles.size(); i++) {
                    double[] distances = new double[4];
                    MyCircle c = theCircles.get(i);
                    // Get the distances
                    for (int j = 0; j < distances.length; j++) {
                        distances[j] = Math.sqrt(Math.pow(c.x - means[j][0], 2) + Math.pow(c.y - means[j][1], 2));
                    }
                    int group = 3;
                    // Determine the group
                    for (int j = 3; j >= 0; j--) {
                        if (distances[j] < distances[group]) {
                            group = j;
                        }
                    }
                    c.setGroup(group);
                    theCircles.set(i, c);
                }
                // Reset means
                for (int i = 0; i < means.length; i++) {
                    for (int j = 0; j < means[0].length; j++) {
                        means[i][j] = 0;
                    }
                }
                int[] gc = new int[] { 0, 0, 0, 0 };
                for (MyCircle c : theCircles) {
                    gc[c.getGroup()]++;
                    means[c.getGroup()][0] += c.x;
                    means[c.getGroup()][1] += c.y;
                }
                for (int i = 0; i < means.length; i++) {
                    means[i][0] /= gc[i];
                    means[i][1] /= gc[i];
                }
            }
            BufferedImage outBIB = ImageEditor.int2bi(outputB);
            Graphics2D g = (Graphics2D) outBIB.getGraphics();
            g.setColor(Color.ORANGE);
            g.setStroke(new BasicStroke(1));
            int[] gc = new int[] { 0, 0, 0, 0 };
            MyCircle[] fourCircle = new MyCircle[4];
            for (int i = 0; i < fourCircle.length; i++) {
                fourCircle[i] = new MyCircle(0, 0, 0);
            }
            for (MyCircle c : theCircles) {
                gc[c.getGroup()]++;
                fourCircle[c.getGroup()].x += c.x;
                fourCircle[c.getGroup()].y += c.y;
                fourCircle[c.getGroup()].radius += c.radius;
            }
            for (int i = 0; i < fourCircle.length; i++) {
                fourCircle[i].x /= gc[i];
                fourCircle[i].y /= gc[i];
                fourCircle[i].radius /= gc[i];
                g.drawOval(fourCircle[i].x - fourCircle[i].radius, fourCircle[i].y - fourCircle[i].radius,
                        fourCircle[i].radius * 2, fourCircle[i].radius * 2);
            }
            ImageIO.write(outBIB, "PNG", new File("C:\\Code\\CSC4ST-ImageProcessing\\HoughCircle\\OUTPUTB.png"));
            System.out.println("Size: " + theCircles.size());
            System.out.println("G0: " + gc[0] + "  G1: " + gc[1] + "  G2: " + gc[2] + "  G3: " + gc[3]);

        } catch (IOException io) {
            System.out.println("io exception");
        }

    }

}

class MyCircle {
    int x;
    int y;
    int radius;
    private int group;

    public MyCircle(int _x, int _y, int _r) {
        x = _x;
        y = _y;
        radius = _r;
    }

    public void setGroup(int _g) {
        group = _g;
    }

    public int getGroup() {
        return group;
    }

    public String toString() {
        return "X: " + x + "  Y: " + y + "  R: " + radius + "  G:" + group;
    }
}
