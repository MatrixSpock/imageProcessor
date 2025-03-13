package uk.ac.nulondon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public final class Image {
    private List<List<Color>> columns;

    private int width;
    private int height;


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
        if (i < 0 || i >= columns.size()) {
            throw new IndexOutOfBoundsException("Invalid column index");
        }
        
        List<Color> column = columns.get(i);
        for (int j = 0; j < height; j++) {
            Color pixel = column.get(j);
            int red = pixel.getRed();
            int green = pixel.getGreen();
            int blue = pixel.getBlue();
            column.set(j, new Color(red, green, blue));
        }
        
        return column;
    }

    public List<Color> removeColumn(int i) {
        if (i < 0 || i >= columns.size()) {
            throw new IndexOutOfBoundsException("Invalid column index");
        }
        
        List<Color> removed = columns.remove(i);
        width--;
        return removed;
    }

    public void addColumn(int index, List<Color> column) {
        if (index < 0 || index > columns.size()) {
            throw new IndexOutOfBoundsException("Invalid column index");
        }
        
        if (column == null || column.size() != height) {
            throw new IllegalArgumentException("Column must have the same height as the image");
        }
        
        columns.add(index, new ArrayList<>(column));
        width++;
    }

    public int getGreenest() {
        int greenestIndex = -1;
        int maxGreenValue = -1;
        
        for (int i = 0; i < columns.size(); i++) {
            List<Color> column = columns.get(i);
            int totalGreen = 0;
            
            for (Color pixel : column) {
                totalGreen += pixel.getGreen();
            }
            
            if (totalGreen > maxGreenValue) {
                maxGreenValue = totalGreen;
                greenestIndex = i;
            }
        }
        
        return greenestIndex;
        // return 0;
    }
}
