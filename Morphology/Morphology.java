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

    private static int[][][] hmElems = new int[][][] {
            {
                    { -1, 1, -1 },
                    { 0, 1, 1 },
                    { 0, 0, -1 }
            },
            {
                    { -1, 0, 0 },
                    { 1, 1, 0 },
                    { -1, 1, -1 }
            },
            {
                    { -1, 1, -1 },
                    { 1, 1, 0 },
                    { -1, 0, 0 }
            },
            {
                    { 0, 0, -1 },
                    { 0, 1, 1 },
                    { -1, 1, -1 }
            }
    };

    private static int[][][] dilate(int[][][] input, int[][] struc, int xCen, int yCen) {
        int[][][] output = new int[input.length][input[0].length][input[0][0].length];
        input = ImageEditor.scale(input, 0, 1);
        for (int plane = 0; plane < input.length; plane++) {
            for (int i = 0; i < input[0].length; i++) {
                for (int j = 0; j < input[0][0].length; j++) {
                    // For every pixel
                    try {
                        if (input[plane][i][j] == 1) {
                            for (int xOff = 0 - xCen; xOff < struc.length - xCen; xOff++) {
                                for (int yOff = 0 - yCen; yOff < struc.length - yCen; yOff++) {
                                    output[plane][i + xOff][j + yOff] = struc[xCen + xOff][yCen + yOff];
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            }
        }
        return ImageEditor.scale(output, 0, 255);
    }

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