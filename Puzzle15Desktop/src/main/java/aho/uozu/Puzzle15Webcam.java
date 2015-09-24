package aho.uozu;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.samples.puzzle15.Puzzle15Processor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;

import aho.uozu.opencv.swingui.CameraManager;
import aho.uozu.opencv.swingui.Utils;

public class Puzzle15Webcam extends JFrame {

    private Puzzle15Surface puzzle15Surface;
    private CameraManager cameraManager;

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        try {
            Puzzle15Webcam puzzle15 = new Puzzle15Webcam(0);
        } catch (IOException e) {
            System.out.println("Error initialising video");
            e.printStackTrace();
        }
    }

    public Puzzle15Webcam(int videoSource) throws IOException {
        cameraManager = new CameraManager(videoSource);
        puzzle15Surface = new Puzzle15Surface(
                cameraManager.getFrameWidth(), cameraManager.getFrameHeight());

        // ui
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(puzzle15Surface, BorderLayout.CENTER);
        setTitle("15 puzzle");
        pack();

        // send camera frames to the puzzle surface
        cameraManager.setFrameProcessor(new CameraManager.FrameProcessor() {
            @Override
            public void onFrame(Mat frame) {
                puzzle15Surface.newFrame(frame);
            }
        });

        // release resources when window closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                cameraManager.release();
            }
        });

        setVisible(true);
        cameraManager.run();
    }

    private class Puzzle15Surface extends JComponent {

        private Puzzle15Processor mPuzzle;
        private Mat lastFrame;

        public Puzzle15Surface(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            mPuzzle = new Puzzle15Processor();
            mPuzzle.prepareNewGame();
            mPuzzle.prepareGameSize(width, height);
            lastFrame = new Mat();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    System.out.println(e.getX() + ", " + e.getY());
                    mPuzzle.deliverTouchEvent(e.getX(), e.getY());
                }
            });
        }

        public void newFrame(Mat m) {
            // convert frame to type required by processor
            Mat cvt = new Mat();
            Imgproc.cvtColor(m, cvt, Imgproc.COLOR_BGR2BGRA);
            lastFrame = mPuzzle.puzzleFrame(cvt);
            cvt.release();
            repaint();
        }

        @Override
        public void paint(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            BufferedImage frame = Utils.MatToBufferedImage(lastFrame);
            g2d.drawImage(frame, 0, 0, null);
        }
    }
}
