import totalcross.ui.Button;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.PressListener;
import totalcross.ui.event.UpdateListener;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.io.BufferedStream;
import totalcross.io.ByteArrayStream;
import totalcross.io.DataStream;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.LineReader;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.sys.Vm;

public class BadApple extends MainWindow {

    DataStream input = null;

    public BadApple() {
        setUIStyle(Settings.MATERIAL_UI);
        // try {
        // convert(null);
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        try {
            // File f = new File("data/out.txt", File.READ_ONLY);
            input = new DataStream(new ByteArrayStream(Vm.getFile("data/out.txt")));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    long timestamp = 0;
    int frames = 0;

    Label fps;
    Button start, stop, continueBtn;

    boolean stopAnimation = false;

    @Override
    public void initUI() {

        fps = new Label("Number of FPS");
        start = new Button("Start");
        stop = new Button("Stop");
        continueBtn = new Button("Continue");

        add(fps, RIGHT - 8, TOP + 8);
        add(start, RIGHT - 40, AFTER + 8, SAME, PREFERRED);
        add(stop, SAME, AFTER + 8, SAME, PREFERRED);
        add(continueBtn, SAME, AFTER + 8, SAME, PREFERRED);

        start.addPressListener(new PressListener() {

            @Override
            public void controlPressed(ControlEvent e) {
                i = 1;
                stopAnimation = false;
            }

        });

        stop.addPressListener(new PressListener() {

            @Override
            public void controlPressed(ControlEvent e) {
                stopAnimation = true;
            }

        });
        continueBtn.addPressListener(new PressListener() {

            @Override
            public void controlPressed(ControlEvent e) {
                stopAnimation = false;
            }

        });

        addUpdateListener(new UpdateListener() {

            @Override
            public void updateListenerTriggered(int elapsedMilliseconds) {
                repaintNow();
                ++frames;
                timestamp += elapsedMilliseconds;
                if (timestamp > 1000) {
                    fps.setText(frames + " FPS");
                    frames = 0;
                    timestamp = 0;
                }
            }

        });
    }

    private String fileName;
    private int i = 1;

    @Override
    public void onPaint(Graphics g) {
        super.onPaint(g);

        if (i == 6569) {
            i = 0;
            return;
        } else {

            if (!stopAnimation)
                i++;
        }

        fileName = "data/out (" + i + ").txt";

        // FileReader fr = null;
        // BufferedReader br = null;
        BufferedStream reader = null;
        ByteArrayStream bas = null;
        try {
            for (int i = 0 ; i < 480 ; i++) {
                for (int j = input.readShort() ; j > 0 ; j--) {
                    g.drawLine(input.readShort(), i, input.readShort(), i);
                }
            }

            // bas = new ByteArrayStream(Vm.getFile(fileName));
            // reader = new BufferedStream(bas, BufferedStream.READ, 4096);

            // String temp;

            // int j = 0;

            // while ((temp = reader.readLine()) != null) {
            //     if (temp.length() > 2) {
            //         String[] data = Convert.tokenizeString(temp, " ");
            //         int w = 0;
            //         while (w < data.length) {
            //             int start = Convert.toInt(data[w++]);
            //             int end = Convert.toInt(data[w++]);
            //             g.drawLine(start, j, end, j);
            //         }
            //     }

            //     j++;
            // }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // try {
            //     bas.close();
            // } catch (Exception e) {
            //     e.printStackTrace();
            // }
        }
    }


    public static void convert(String[] args) throws Exception {
        ByteArrayStream out = new ByteArrayStream(4096);
        DataStream ds = new DataStream(out);
        
        byte[] frame = null;
        for(int i = 1 ; (frame = Vm.getFile("data/out (" + i + ").txt")) != null ; i++) {
            ByteArrayStream bas = null;

            bas = new ByteArrayStream(frame);
            BufferedStream reader = new BufferedStream(bas, BufferedStream.READ, 4096);

            String temp;

            if (i == 129) {
                int a = 10;
                a++;
            }

            for (int j = 0 ; (temp = reader.readLine()) != null ; j++) {
                if (temp.length()  < 2) {
                    ds.writeShort(0);
                    continue;
                }
                String[] data = Convert.tokenizeString(temp, " ");
                int w = 0;
                ds.writeShort(data.length / 2);
                while (w < data.length) {
                    int start = Convert.toInt(data[w++]);
                    int end = Convert.toInt(data[w++]);
                    ds.writeShort(start);
                    ds.writeShort(end);
                    // g.drawLine(start, j, end, j);
                }
            }
        }
        File f = new File("out.txt", File.CREATE_EMPTY);
        f.writeBytes(out.getBuffer(), 0, out.getPos());
        f.close();
    }
}
