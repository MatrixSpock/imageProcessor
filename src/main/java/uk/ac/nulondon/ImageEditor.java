package uk.ac.nulondon;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/*APPLICATION SERVICE LAYER*/
public final class ImageEditor {

    private Image image;

    /**
     * Loads the image from the file path. PLEASE DO NOT CHANGE
     *
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

    // Stack to store undone columns so we can restore if needed
    private static class RemovedColumn {
        private int index;
        private List<Color> column;

        RemovedColumn(int index, List<Color> column) {
            this.index = index;
            this.column = column;
        }
    }

    private final Deque<RemovedColumn> undoStack = new ArrayDeque<>();

    /**
     * Remove the greenest column: find it, highlight it,
     * remove it, and push on the undo stack.
     */
    public void removeGreenestColumn() {
        int index = image.getGreenest();

        // same pattern
        List<Color> originalColumn = image.highlightColumn(index);
        undoStack.push(new RemovedColumn(index, originalColumn));
        image.removeColumn(index);
    }

    /**
     * Remove the specified column index: highlight it, remove it,
     * and push on the undo stack.
     *
     * @param index The column to remove.
     */
    public void removeColumn(int index) {
        // 1. highlightColumn modifies the column in-place
        // but *returns* the unmodified original
        List<Color> originalColumn = image.highlightColumn(index);

        // 3. Push the *original* list (not the highlighted one)
        undoStack.push(new RemovedColumn(index, originalColumn));

        // 2. removeColumn returns the highlighted version,
        // but we don’t use it for undo
        image.removeColumn(index);

    }

    /**
     * Undo the most recent removal by popping from the stack
     * and re-inserting the column at its original index.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            // Pop the last removed column from the stack,
            RemovedColumn last = undoStack.pop();
            // Now lets add the column to the original image.
            image.addColumn(last.index, last.column);
        }
    }
}
