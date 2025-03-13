package uk.ac.nulondon;


import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/*APPLICATION SERVICE LAYER*/
public final class ImageEditor {

    private Image image;


    /**
     * Loads the image from the file path. PLEASE DO NOT CHANGE
     * @param filePath File path, e. g. /home/img.png
     * @throws IOException If the file is missing or cannot be read
     */
    public void load(String filePath) throws IOException {
        File originalFile = new File(filePath);
        BufferedImage img = ImageIO.read(originalFile);
        image = new Image(img);
    }

    public void save(String filePath) throws IOException {
        BufferedImage img = image.toBufferedImage();
        ImageIO.write(img, "png", new File(filePath));
    }


    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Removes a column at the specified index
     * 
     * @param index the index of the column to remove
     * @return true if successful, false otherwise
     */
    public boolean removeColumn(int index) {
        if (index < 0 || index >= image.getWidth()) {
            return false;
        }
        
        List<Color> removed = image.removeColumn(index);
        if (!removed.isEmpty()) {
            removedColumns.push(removed);
            removedPositions.push(index);
            return true;
        }
        
        return false;
    }

    /**
     * Removes the greenest column from the image
     * 
     * @return the index of the removed column, or -1 if unsuccessful
     */
    public int removeGreenestColumn() {
        int greenestIndex = image.getGreenest();
        if (greenestIndex >= 0) {
            List<Color> removed = image.removeColumn(greenestIndex);
            removedColumns.push(removed);
            removedPositions.push(greenestIndex);
            return greenestIndex;
        }
        
        return -1;
    }

    /**
     * Undoes the last column removal operation
     * 
     * @return true if an operation was undone, false otherwise
     */
    public boolean undo() {
        if (removedColumns.isEmpty() || removedPositions.isEmpty()) {
            return false;
        }
        
        List<Color> columnToRestore = removedColumns.pop();
        int positionToRestore = removedPositions.pop();
        
        image.addColumn(positionToRestore, columnToRestore);
        return true;
    }

    /**
     * Checks if undo operation is available
     * 
     * @return true if there are operations to undo
     */
    public boolean canUndo() {
        return !removedColumns.isEmpty();
    }

}
