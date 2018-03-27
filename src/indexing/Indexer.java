package indexing;

import com.google.code.jcbir.Constants;
import com.google.code.jcbir.decomposition.HaarWavelet;
import com.google.code.jcbir.decomposition.Wavelet;
import com.google.code.jcbir.featureextraction.FNormFeature;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.StringTokenizer;
import org.ejml.data.DenseMatrix64F;

/**
 *
 * @author eadams
 * @author denny
 */
public abstract class Indexer {

    protected FNormFeature fNormFeature = new FNormFeature();
    protected Wavelet haar = new HaarWavelet();
    public static final String ROW_TOKEN="/";
    public static final String COL_TOKEN=";";
    public static final String MATRIX_TOKEN="\n";

    public String parseMatrixs(DenseMatrix64F[] matrixs) {
        StringBuffer values = new StringBuffer();
        for (DenseMatrix64F m : matrixs) {
            values.append(parseMatrix(m));
            values.append(Indexer.MATRIX_TOKEN);
        }
        return values.toString();
    }

    public String parseDoubles(double[] values) {
        StringBuffer strValues = new StringBuffer();
        for (double d : values) {
            strValues.append(round(d));
            strValues.append(Indexer.COL_TOKEN);
        }
        return strValues.toString();
    }

    public double[] toDoubles(String representation) {
        StringTokenizer tokenizer = new StringTokenizer(representation, Indexer.COL_TOKEN);
        int n = tokenizer.countTokens();
        double[] values = new double[n];

        for (int i = 0; i < n; i++) {
            values[i] = Double.parseDouble(tokenizer.nextToken());
        }

        return values;
    }

    public DenseMatrix64F[] toMatrixs(String representation) {
        StringTokenizer matTokenizer = new StringTokenizer(representation, Indexer.MATRIX_TOKEN);
        int numOfMat = matTokenizer.countTokens();
        DenseMatrix64F[] matrixs = new DenseMatrix64F[numOfMat];
        for (int i = 0; i < numOfMat; i++) {
            String str = matTokenizer.nextToken();
            matrixs[i] = toMatrix(str);
        }
        return matrixs;
    }

    public String parseMatrix(DenseMatrix64F matrixs) {
        StringBuffer values = new StringBuffer();
        for (int r = 0; r < matrixs.getNumRows(); r++) {
            for (int c = 0; c < matrixs.getNumCols(); c++) {
                values.append(round(matrixs.get(r, c)));
                values.append(Indexer.COL_TOKEN);
            }
            values.append(Indexer.ROW_TOKEN);
        }
        return values.toString();
    }

    public DenseMatrix64F toMatrix(String representation) {
        //System.out.println("rep :"+representation);
        StringTokenizer rowTokenizer = new StringTokenizer(representation, Indexer.ROW_TOKEN);
        StringTokenizer colTokenizer = new StringTokenizer(representation, Indexer.COL_TOKEN);
        int row = rowTokenizer.countTokens();
        int col = colTokenizer.countTokens() / row;

        DenseMatrix64F values = new DenseMatrix64F(row, col);

        for (int r = 0; r < row; r++) {
            String str = rowTokenizer.nextToken();
            colTokenizer = new StringTokenizer(str, Indexer.COL_TOKEN);
            col = colTokenizer.countTokens();
            for (int c = 0; c < col; c++) {
                values.set(r, c, Double.parseDouble(colTokenizer.nextToken()));
            }
        }

        return values;
    }

    public String round(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(d);
    }

    public Index getIndex(BufferedImage bufferedImage) {
        Index index = new Index();

        int w = Constants.IMAGE_WIDTH;
        int h = Constants.IMAGE_HEIGHT;
        int level = Constants.DECOMPOSITION_LEVEL;

        // create Red Matrix , Green Matrix, and Blue Matrix
        DenseMatrix64F red = new DenseMatrix64F(h, w);
        DenseMatrix64F green = new DenseMatrix64F(h, w);
        DenseMatrix64F blue = new DenseMatrix64F(h, w);


        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int rgb = bufferedImage.getRGB(x, y);
                Color color = new Color(rgb);
                red.set(y, x, color.getRed());
                green.set(y, x, color.getGreen());
                blue.set(y, x, color.getBlue());
            }
        }

        // Decompose Red matrix, Green Matrix, and Blue Matrix with Haar wavelet
        DenseMatrix64F[] redHaarAllLevels = haar.transform(red, level);
        DenseMatrix64F[] greenHaarAllLevels = haar.transform(green, level);
        DenseMatrix64F[] blueHaarAllLevels = haar.transform(blue, level);

        // Calculate Red, Green, Blue Haar Wavelet Coefficients with F-Norm
        DenseMatrix64F redHaarFeatures = fNormFeature.extract(redHaarAllLevels[level - 1]);
        DenseMatrix64F greenHaarFeatures = fNormFeature.extract(greenHaarAllLevels[level - 1]);
        DenseMatrix64F blueHaarFeatures = fNormFeature.extract(blueHaarAllLevels[level - 1]);

        // Calculate Red LL,LH,HL-Haar wavelet coefficients with F-Norm
        DenseMatrix64F[] redLLHaarFeatures = fNormFeature.extractCoeff(redHaarAllLevels, "LL");
        DenseMatrix64F[] redLHHaarFeatures = fNormFeature.extractCoeff(redHaarAllLevels, "LH");
        DenseMatrix64F[] redHLHaarFeatures = fNormFeature.extractCoeff(redHaarAllLevels, "HL");

        // Calculate Red LL,LH,HL-Haar Wavelet Coefficient variances
        double[] redLLHaarVariances = fNormFeature.calculateVariance(redLLHaarFeatures);
        double[] redLHHaarVariances = fNormFeature.calculateVariance(redLHHaarFeatures);
        double[] redHLHaarVariances = fNormFeature.calculateVariance(redHLHaarFeatures);

        // Calculate Green LL,LH,HL-Haar wavelet coefficients with F-Norm
        DenseMatrix64F[] greenLLHaarFeatures = fNormFeature.extractCoeff(greenHaarAllLevels, "LL");
        DenseMatrix64F[] greenLHHaarFeatures = fNormFeature.extractCoeff(greenHaarAllLevels, "LH");
        DenseMatrix64F[] greenHLHaarFeatures = fNormFeature.extractCoeff(greenHaarAllLevels, "HL");

        // Calculate Green LL,LH,HL-Haar Wavelet Coefficient variances
        double[] greenLLHaarVariances = fNormFeature.calculateVariance(greenLLHaarFeatures);
        double[] greenLHHaarVariances = fNormFeature.calculateVariance(greenLHHaarFeatures);
        double[] greenHLHaarVariances = fNormFeature.calculateVariance(greenHLHaarFeatures);

        // Calculate Blue LL,LH,HL-Haar wavelet coefficients with F-Norm
        DenseMatrix64F[] blueLLHaarFeatures = fNormFeature.extractCoeff(blueHaarAllLevels, "LL");
        DenseMatrix64F[] blueLHHaarFeatures = fNormFeature.extractCoeff(blueHaarAllLevels, "LH");
        DenseMatrix64F[] blueHLHaarFeatures = fNormFeature.extractCoeff(blueHaarAllLevels, "HL");

        // Calculate Blue LL,LH,HL-Haar Wavelet Coefficient variances
        double[] blueLLHaarVariances = fNormFeature.calculateVariance(blueLLHaarFeatures);
        double[] blueLHHaarVariances = fNormFeature.calculateVariance(blueLHHaarFeatures);
        double[] blueHLHaarVariances = fNormFeature.calculateVariance(blueHLHaarFeatures);


        // set Haar related variables
        index.setRHaarFeature(parseMatrix(redHaarFeatures));
        index.setGHaarFeature(parseMatrix(greenHaarFeatures));
        index.setBHaarFeature(parseMatrix(blueHaarFeatures));

        index.setRLLhaarvariance(parseDoubles(redLLHaarVariances));
        index.setGLLhaarvariance(parseDoubles(greenLLHaarVariances));
        index.setBLLhaarvariance(parseDoubles(blueLLHaarVariances));

        index.setRLHhaarvariance(parseDoubles(redLHHaarVariances));
        index.setGLHhaarvariance(parseDoubles(greenLHHaarVariances));
        index.setBLHhaarvariance(parseDoubles(blueLHHaarVariances));

        index.setRHLhaarvariance(parseDoubles(redHLHaarVariances));
        index.setGHLhaarvariance(parseDoubles(greenHLHaarVariances));
        index.setBHLhaarvariance(parseDoubles(blueHLHaarVariances));

        index.setRLLhaarfeature(parseMatrixs(redLLHaarFeatures));
        index.setGLLhaarfeature(parseMatrixs(greenLLHaarFeatures));
        index.setBLLhaarfeature(parseMatrixs(blueLLHaarFeatures));


        return index;
    }
}
