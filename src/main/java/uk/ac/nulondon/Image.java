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
        List<Color> highlightedColumn = new ArrayList<>();
    
        for (Color color : column) {
            int r = Math.min(255, color.getRed() + 50);  // Boost red to highlight
            int g = color.getGreen();
            int b = color.getBlue();
            highlightedColumn.add(new Color(r, g, b));
        }
    
        columns.set(i, highlightedColumn);
        return highlightedColumn;
    }
    

    public List<Color> removeColumn(int i) {
        if (i < 0 || i >= columns.size()) {
            throw new IndexOutOfBoundsException("Invalid column index");
        }
    
        List<Color> removedColumn = columns.remove(i);
        width--;  // Update width since we removed a column
        return removedColumn;
    }
    

    public void addColumn(int index, List<Color> column) {
        if (index < 0 || index > columns.size()) {
            throw new IndexOutOfBoundsException("Invalid column index");
        }
    
        columns.add(index, column);
        width++;  // Update width since we added a column
    }
    

    public int getGreenest() {
        int maxGreenSum = -1;
        int greenestIndex = -1;
    
        for (int i = 0; i < columns.size(); i++) {
            int greenSum = columns.get(i).stream().mapToInt(Color::getGreen).sum();
            
            if (greenSum > maxGreenSum) {
                maxGreenSum = greenSum;
                greenestIndex = i;
            }
        }
        return greenestIndex;
    }
    
}
