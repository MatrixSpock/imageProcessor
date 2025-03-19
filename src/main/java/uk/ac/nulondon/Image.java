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
        // Return the original column (for the test's verification) but change each
        // color
        // in-place to produce a highlighted version (Example: set the red channel to
        // 255 while keeping green and blue the same).

        // Error wih

        // Index check
        if (i < 0 || i >= width) {
            throw new IndexOutOfBoundsException("Invalid column index for highlight: " + i);
        }

        List<Color> originalColumn = new ArrayList<>();

        // Copy the original column into a new list, so we can return it later
        for (Color c : columns.get(i)) {
            // Copy each color by its RGB
            originalColumn.add(new Color(c.getRGB()));
        }

        // Now highlight in place
        for (int row = 0; row < height; row++) {
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
        // We'll store the maximum sum of green-channel values seen so far
        // and also track which column has that maximum sum.
        long maxSum = Long.MIN_VALUE;
        int greenestIndex = 0;
    
        // Iterate over each column in the image
        for (int col = 0; col < width; col++) {
            // We'll accumulate the green value from every pixel in the column
            long sumGreen = 0;
    
            // Sum up the green values of all pixels in the current column
            for (Color c : columns.get(col)) {
                sumGreen += c.getGreen();
            }
    
            // If this column's total green value is larger than our current maximum,
            // update both the maxSum and the greenestIndex accordingly
            if (sumGreen > maxSum) {
                maxSum = sumGreen;
                greenestIndex = col;
            }
        }
    
        // After checking all columns, return the one with the highest green-sum
        return greenestIndex;
    }
    
}
