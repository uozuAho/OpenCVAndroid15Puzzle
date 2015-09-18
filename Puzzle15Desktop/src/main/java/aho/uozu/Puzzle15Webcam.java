package aho.uozu;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.samples.puzzle15.Puzzle15Processor;

import java.awt.Graphics2D;

import aho.uozu.opencv.ui.CameraViewer;
import aho.uozu.opencv.ui.FrameProcessor;

public class Puzzle15Webcam {

    private static final String TAG = "puz15wc";

    private static class Processor implements FrameProcessor {

        private Puzzle15Processor mPuzzle;

        public Processor() {
            mPuzzle = new Puzzle15Processor();
            mPuzzle.prepareNewGame();
        }

        public void setSize(int width, int height) {
            mPuzzle.prepareGameSize(width, height);
        }

        public Mat process(Mat m) {
            // convert to type required by processor
            Mat cvt = new Mat();
            Imgproc.cvtColor(m, cvt, Imgproc.COLOR_BGR2BGRA);
            Mat imgOut = mPuzzle.puzzleFrame(cvt);
            cvt.release();
            return imgOut;
        }

        public void draw(Graphics2D g) {
        }
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        CameraViewer camview = new CameraViewer("15 puzzle");
//        Log.d(TAG, String.format("camview: %dx%d", camview.getFrameWidth(),
//                camview.getFrameHeight()));
        Processor puzzleProcessor = new Processor();
        puzzleProcessor.setSize(camview.getFrameWidth(), camview.getFrameHeight());
        camview.setFrameProcessor(puzzleProcessor);
        camview.run();
    }
}
