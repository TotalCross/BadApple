import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.event.UpdateListener;
import totalcross.ui.gfx.Graphics;

import totalcross.io.ByteArrayStream;
import totalcross.io.LineReader;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.sys.Vm;

public class BadApple extends MainWindow {

    public BadApple() {
        setUIStyle(Settings.MATERIAL_UI);
    }

    long timestamp = 0;
    int frames = 0;

    Label fps;

    @Override
    public void initUI() {

        fps = new Label("Number of FPS");

        add(fps, RIGHT - 8, TOP + 8);

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
            i++;
        }

        fileName = "data/out (" + i + ").txt";

        // FileReader fr = null;
        // BufferedReader br = null;
        LineReader reader = null;
        ByteArrayStream bas = null;
        try {

            bas = new ByteArrayStream(Vm.getFile(fileName));
            reader = new LineReader(bas);

            String temp;

            int j = 0;

            while ((temp = reader.readLine()) != null) {
                String[] data = temp.split("\s");
                int w = 0;
                while (w < data.length) {
                    int start = Convert.toInt(data[w++]);
                    int end = Convert.toInt(data[w++]);
                    g.drawLine(start, j, end, j);
                }

                j++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bas.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
