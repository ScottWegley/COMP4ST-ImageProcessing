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

    private static int[][][] erode(int[][][] input, int[][] struc, int xCen, int yCen) {
        int[][][] output = new int[input.length][input[0].length][input[0][0].length];
        input = ImageEditor.scale(input, 0, 1);
        for (int plane = 0; plane < input.length; plane++) {
            for (int i = 0; i < input[0].length; i++) {
                for (int j = 0; j < input[0][0].length; j++) {
                    // For every pixel
                    try {
                        if (input[plane][i][j] == 1) {
                            boolean express = true;
                            for (int xOff = 0 - xCen; xOff < struc.length - xCen; xOff++) {
                                for (int yOff = 0 - yCen; yOff < struc.length - yCen; yOff++) {
                                    if (input[plane][i + xOff][j + yOff] == 0 && struc[xCen + xOff][yCen + yOff] == 1) {
                                        express = false;
                                    }
                                }
                            }
                            output[plane][i][j] = (express ? 1 : 0);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            }
        }
        return ImageEditor.scale(output, 0, 255);
    }

    private static int[][][] hitMiss(int[][][] input, int[][] struc, int xCen, int yCen) {
        int[][][] output = new int[input.length][input[0].length][input[0][0].length];
        input = ImageEditor.scale(input, 0, 1);
        for (int plane = 0; plane < input.length; plane++) {
            for (int i = 0; i < input[0].length; i++) {
                for (int j = 0; j < input[0][0].length; j++) {
                    // For every pixel
                    try {
                        if (input[plane][i][j] == 1) {
                            boolean express = true;
                            for (int xOff = 0 - xCen; xOff < struc.length - xCen; xOff++) {
                                for (int yOff = 0 - yCen; yOff < struc.length - yCen; yOff++) {
                                    if (input[plane][i + xOff][j + yOff] != struc[xCen + xOff][yCen + yOff]
                                            && struc[xCen + xOff][yCen + yOff] != -1) {
                                        express = false;
                                    }
                                }
                            }
                            output[plane][i][j] = (express ? 1 : 0);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
            }
        }
        return ImageEditor.scale(output, 0, 255);
    }

    private static int[][][] open(int[][][] input, int[][] struc, int xCen, int yCen) {
        return dilate(erode(input, struc, xCen, yCen), struc, xCen, yCen);
    }

    private static int[][][] close(int[][][] input, int[][] struc, int xCen, int yCen) {
        return erode(dilate(input, struc, xCen, yCen), struc, xCen, yCen);
    }

    public static void main(String[] args) {
        try {
            BufferedImage bi = ImageEditor
                    .readImg("C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\WhiteSquareWithNoise.PNG");
            int[][][] img = ImageEditor.bi2int(bi);
            ImageEditor.writePNG(ImageEditor.int2bi(dilate(img, genStruc(3, 1), 2, 2)),
                    "C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\dilation.PNG");
            ImageEditor.writePNG(ImageEditor.int2bi(erode(img, genStruc(3, 1), 2, 2)),
                    "C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\erosion.PNG");
            ImageEditor.writePNG(ImageEditor.int2bi(open(img, genStruc(3, 1), 2, 2)),
                    "C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\open.PNG");
            ImageEditor.writePNG(ImageEditor.int2bi(close(img, genStruc(3, 1), 2, 2)),
                    "C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\close.PNG");
            ImageEditor.writePNG(ImageEditor.int2bi(close(img, hmElems[3], 1, 1)),
                    "C:\\Code\\CSC4ST-ImageProcessing\\Morphology\\hitMiss.PNG");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}