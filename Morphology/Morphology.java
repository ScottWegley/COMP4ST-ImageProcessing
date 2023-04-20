package Morphology;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import Library.ImageEditor;

public class Morphology {

    public static int[][][] median(int in[][][], int rows, int cols)
            throws IllegalArgumentException {
        if (rows % 2 == 0 || cols % 2 == 0) {
            throw new IllegalArgumentException("Mask size must be odd");
        }

        int comps = in.length;
        int height = in[0].length;
        int width = in[0][0].length;

        int out[][][] = new int[comps][height][width];

        ArrayList<Integer> list = new ArrayList<Integer>();

        for (int c = 0; c < out.length; c++) {
            // Two loops for getting every pixel;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    list.clear();
                    for (int k = -rows / 2; k <= rows / 2; k++) {
                        for (int l = -cols / 2; l <= cols / 2; l++) {
                            try {
                                list.add(in[c][i + k][j + l]);
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                        }
                    }
                    Collections.sort(list);
                    out[c][i][j] = list.get(list.size() / 2);
                }
            }
        }

        return out;
    }


    public static void main(String[] args) {
        try {
            BufferedImage bi = ImageEditor.readImg("C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\Shapes.PNG");
            ImageEditor.writePNG(ImageEditor.int2bi(median(ImageEditor.bi2int(bi), 3, 3)), "C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\out.PNG");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
