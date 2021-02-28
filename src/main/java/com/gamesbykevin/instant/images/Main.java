package com.gamesbykevin.instant.images;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main {

    //allowed file extensions
    public static final String[] VALID_EXT = {".png", ",jpg", ".jpeg", ".gif"};

    //source and destination paths
    private static final String DEFAULT_DST = "dst\\";

    //instagram images work with these dimensions
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 600;

    //default background color to white
    private static final int COLOR_R = 255;
    private static final int COLOR_G = 255;
    private static final int COLOR_B = 255;

    public static void main(String[] args) {

        Main main = new Main();

        if (args.length == 0) {
            System.out.println();
            System.out.println("When you run specify image source folder and background color as r,g,b");
            System.out.println("java -jar image_resize_instagram-1.0-SNAPSHOT.jar images/ 255 255 255");
            System.out.println();
            System.out.println();
            throw new RuntimeException();
        }

        try {

            main.src = args[0];

            //parse color
            if (args.length >= 4)
                main.color = new Color(Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));

        } catch (Exception e) {
            e.printStackTrace();
        }

        main.convert();
    }

    //file path list of valid image files
    private List<String> paths;

    //source of images
    private String src;

    //background color
    private Color color;

    public Main() {
        //default
        color = new Color(COLOR_R, COLOR_G, COLOR_B);
    }

    public void convert() {

        File srcDir = new File(getSrc());
        File dstDir = new File(DEFAULT_DST);

        //source directory is required
        if (!srcDir.exists())
            throw new RuntimeException("image source directory doesn't exist: " + srcDir.getAbsolutePath());

        //if destination directory doesn't exist we will create it now
        if (!dstDir.exists()) {
            if (dstDir.mkdirs()) {
                System.out.println("Destination directory created: " + dstDir.getAbsolutePath());
            }
        }

        System.out.println("Searching for images with these file extensions: " + Arrays.toString(VALID_EXT));

        //get all images
        gatherPaths(srcDir);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("I found ").append(getPaths().size()).append(" files.");

        if (getPaths().isEmpty())
            return;

        int count = 0;

        //track start and finish
        Date start = new Date();

        for (String path : getPaths()) {

            count++;

            try {
                File source = new File(path);
                BufferedImage image = ImageIO.read(source);

                final int w = DEFAULT_WIDTH;
                final int h = DEFAULT_HEIGHT;

                int tmpW = w, tmpH = h;

                //if image is small enough, it will fit inside new image
                if (image.getWidth() < w && image.getHeight() < h) {
                    tmpW = image.getWidth();
                    tmpH = image.getHeight();
                } else {
                    if (image.getWidth() > image.getHeight()) {
                        tmpH = (int)(((float)image.getHeight() / (float)image.getWidth()) * tmpW);
                    } else {
                        tmpW = (int)(((float)image.getWidth() / (float)image.getHeight()) * tmpH);
                    }
                }

                BufferedImage convert = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = convert.createGraphics();
                graphics.setColor(getColor());
                graphics.fillRect(0,0, w, h);

                int x = (w / 2) - (tmpW / 2);
                int y = (h / 2) - (tmpH / 2);
                graphics.drawImage(image, x, y, tmpW, tmpH, null);

                //write update image to file
                ImageIO.write(convert, "png", new File(DEFAULT_DST + source.getName()));

            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println(count + " of " + getPaths().size());
        }

        System.out.println("Start: " + start);
        System.out.println("Done : " + new Date());
        System.out.println("Done, converted images are in '" + dstDir.getAbsolutePath() + "'");
    }

    private void gatherPaths(File tmp) {

        if (tmp == null)
            return;

        if (tmp.isFile()) {

            boolean valid = false;

            for (String ext : VALID_EXT) {
                if (tmp.getAbsolutePath().endsWith(ext)) {
                    valid = true;
                    break;
                }
            }

            if (valid) {
                getPaths().add(tmp.getAbsolutePath());
            }

        } else {

            File[] files = tmp.listFiles();

            if (files != null) {
                for (File file : files) {
                    gatherPaths(file);
                }
            }
        }
    }

    public List<String> getPaths() {

        if (this.paths == null)
            this.paths = new ArrayList<>();

        return this.paths;
    }

    public String getSrc() {
        return src;
    }

    public Color getColor() {
        return color;
    }
}