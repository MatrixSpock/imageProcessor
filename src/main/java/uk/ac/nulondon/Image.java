package uk.ac.nulondon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class Image {
    private List<List<Color>> columns;

    private int width;
    private int height;
    private static final int HIGHLIGHT_RED = 255;
    private static final int HIGHLIGHT_GREEN = 0;
    private static final int HIGHLIGHT_BLUE = 0;


    public Image(BufferedImage img) {
        width = img.getWidth();
        height = img.getHeight();
        columns = new ArrayList<>();
        for (int i = 0; i < width; i++) {
            List<Color> column = new ArrayList<>();
            columns.add(column);
            for (int j = 0; j < height; j++) {
                column.add(new Color(img.getRGB(i, j)));
            }
        }
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color pixel = columns.get(i).get(j);
                image.setRGB(i, j, pixel.getRGB());
            }
        }
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Color> highlightColumn(int i) {
        // Return the original column (for the test's verification) but change each color
        // in-place to produce a highlighted version (Example: set the red channel to
        // 255 while keeping green and blue the same).

        // Error wih 

        // Index check
        if (i < 0 || i >= width) {
            throw new IndexOutOfBoundsException("Invalid column index for highlight: " + i);
        }

        // Copy original column so we can return it
        List<Color> originalColumn = new ArrayList<>(columns.get(i));

        // Modify each color in-place to give a "highlight" effect
        for (int row = 0; row < height; row++) {
            Color c = columns.get(i).get(row);
            // Example highlight: set red to 255, keep green/blue
            columns.get(i).set(row, new Color(HIGHLIGHT_RED, HIGHLIGHT_GREEN, HIGHLIGHT_BLUE));
        }

        // Return the original, unmodified column for the test
        return originalColumn;

    }

    public List<Color> removeColumn(int i) {
        // Remove the column at index i from the list of columns and return it.
        // Index check
        if (i < 0 || i >= width) {
            throw new IndexOutOfBoundsException("Invalid column index for removal: " + i);
        }

        // Remove the column from our list-of-lists
        List<Color> removed = columns.remove(i);

        // Decrement width to reflect the removal
        width--;
        return removed;
    }

    public void addColumn(int index, List<Color> column) {
        // insert a column at the specified index.
        // Index check: can insert at 'width' to append at the end
        if (index < 0 || index > width) {
            throw new IndexOutOfBoundsException("Invalid insert index: " + index);
        }

        // Add the new column
        columns.add(index, column);
        // Increase the width
        width++;
    }

    public int getGreenest() {
        // Compute which column has the largest sum of green-channel values.
        long maxSum = Long.MIN_VALUE;
        int greenestIndex = 0;

        for (int col = 0; col < width; col++) {
            long sumGreen = 0;
            for (Color c : columns.get(col)) {
                sumGreen += c.getGreen();
            }
            if (sumGreen > maxSum) {
                maxSum = sumGreen;
                greenestIndex = col;
            }
        }
        return greenestIndex;
    }
}
