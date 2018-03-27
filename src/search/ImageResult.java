package search;

import java.awt.image.BufferedImage;

/**
 *
 * @author eadams
 * @author denny
 */
public class ImageResult implements Comparable {

    private String path;
    private double similarity;
    private BufferedImage bufferedImage;

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the similarity
     */
    public double getSimilarity() {
        return similarity;
    }

    /**
     * @param similarity the similarity to set
     */
    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }

    public int compareTo(Object o) {
        ImageResult imageResult = (ImageResult) o;
        if (imageResult.getSimilarity() > this.similarity) {
            return 1;
        }

        return 0;
    }

    /**
     * @return the bufferedImage
     */
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    /**
     * @param bufferedImage the bufferedImage to set
     */
    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }
}
