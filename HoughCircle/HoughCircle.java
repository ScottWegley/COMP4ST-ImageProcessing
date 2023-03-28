package HoughCircle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Library.ImageEditor;

public class HoughCircle {

    private int radiusBins = 15;
    private int radiusStart = 10;
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
        try {
            String filename = "C:\\Code\\CSC4ST-ImageProcessing\\HoughTransform\\KanizsaSquare.png";
            BufferedImage bi;
            bi = ImageIO.read(new File(filename));
            int[][][] RGB = ImageEditor.bi2int(bi);
            HoughCircle houghcircle = new HoughCircle();
            houghcircle.Apply(RGB[0]);
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

            RGB = new int[3][RGB[0].length][RGB[0][0].length];
            for (int i = 0; i < houghcircle.accumulator.length; ++i) {
                for (int j = 0; j < houghcircle.accumulator[i].length; ++j) {
                    if (houghcircle.accumulator[i][j] > 75) {
                        int y1 = i / houghcircle.samples;
                        int x1 = i % houghcircle.samples;
                        System.out.println(houghcircle.accumulator[i][j] + " " + i + " " + (j + houghcircle.radiusStart)
                                + " (" + x1 + ", " + y1 + ")");
                    }
                }
            }

        } catch (IOException io) {
            System.out.println("io exception");
        }

    }

}
