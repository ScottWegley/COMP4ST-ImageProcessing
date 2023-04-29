package Morphology;

import java.awt.image.BufferedImage;
import java.io.IOException;

import Library.ImageEditor;

public class Morphology {

    private static int[][] genStruc(int sizeSqr, int fillVal) {
        int[][] out = new int[sizeSqr][sizeSqr];
        for (int i = 0; i < out.length; i++) {
            for (int j = 0; j < out[0].length; j++) {
                out[i][j] = fillVal;
            }
        }
        return out;
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
            BufferedImage bi = ImageEditor
                    .readImg("C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\WhiteSquareWithNoise.PNG");
            int[][][] img = ImageEditor.bi2int(bi);
            ImageEditor.writePNG(ImageEditor.int2bi(erode(img, genStruc(5, 1), 2, 2)),
                    "C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\Out.PNG");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}