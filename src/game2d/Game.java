package game2d;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.applet.*;

// Main game thread, extend this class and give it the name of your game.
// Game logic runs at 125hz.
// Drawing is done at 60hz, skipping frames if necessary.

public abstract class Game extends Applet implements Runnable
{
  Thread game = null;
  boolean initialized = false;

  // back bufer
  public Bitmap backbuf;
  BufferedImage bi;

  boolean paintok = false;

  public Sound sound;
  public Input input;

  long gameTime = 1000000000 / 125;
  long frameTime = 1000000000 / 60;
  long fps_count = 0;
  long fps = 0, newfps = 0, oldfps = 0;
  public boolean show_fps = false;
  long nextFrameStart;

  // applet initialization
  public void init()
  {
    int i;

    // only initialize everything once
    if(initialized)
      return;

    // set screen size
    gameSize();

    // reset some stuff
    Trig.init();
    Rnd.set(12345);
    Blend.setMode(Blend.NORMAL);
    fps = 0;

    // back buffer related
    backbuf = new Bitmap(Screen.w, Screen.h);

    DataBuffer data_buffer = new DataBufferInt(backbuf.data, backbuf.w * backbuf.h);
    int band_masks[] = { 0xFF0000, 0xFF00, 0xFF, 0xFF000000 };
    WritableRaster write_raster = 
      Raster.createPackedRaster(data_buffer, backbuf.w, backbuf.h, backbuf.w,
                                band_masks, null);
    ColorModel color_model = ColorModel.getRGBdefault();

    bi = new BufferedImage(color_model, write_raster, false, null);

    setBackground(Color.BLACK);

    // center/scale screen within applet
    Dimension dim = getSize();
    Screen.maxw = dim.width;
    Screen.maxh = dim.height;
    int scalex = Screen.maxw / Screen.w;
    if(scalex < 1)
      scalex = 1;
    int scaley = Screen.maxh / Screen.h;
    if(scaley < 1)
      scaley = 1;
    Screen.scale = Int.min(scalex, scaley); 
    Screen.scaledw = Screen.w * Screen.scale; 
    Screen.scaledh = Screen.h * Screen.scale; 
    Screen.xpos = (Screen.maxw - Screen.scaledw) / 2;
    Screen.ypos = (Screen.maxh - Screen.scaledh) / 2;

    // start input
    input = new Input();

    addKeyListener(input);
    addMouseListener(input);
    addMouseMotionListener(input);
 
    // init game
    gameInit();

    // start sound thread
    sound = new Sound();

    // start game thread
    nextFrameStart = System.nanoTime();
    game = new Thread(this);
    game.start();

    // initialization complete
    initialized = true;
  }

  public void start()
  {
    // not needed since the game runs in a thread
  }

  public void run()
  {
    // main loop
    while(true)
    {
      long t1 = System.nanoTime();

      // game logic
      do
      {
        // game logic here
        gameLogic();
	
        nextFrameStart += gameTime;
      }
      while(nextFrameStart < System.nanoTime());

      long remaining = nextFrameStart - System.nanoTime();
      long time = System.nanoTime() - t1;
      if(time < frameTime)
        remaining = frameTime - time;

      long millis = remaining / 1000000;
      int nanos = (int)(remaining % 1000000);

      if(remaining > 0)
      {
        try
        {
          Thread.sleep(millis, nanos);
        }
        catch(InterruptedException e)
        {
        }
      }

      long t2 = System.nanoTime();
      fps_count += (t2 - t1);

      if(fps_count >= 1000000000)
      {
        fps_count = 0;
        // report fps
        newfps = (oldfps + fps) / 2;
        oldfps = fps;
        fps = 0;	
      } else {
        fps++;
      }

      // draw frame
      if(!paintok)
      {
        // draw frame here
        gameFrame();

        // update window
        paintok = true;
        repaint();
      }
    }
  }

  public void paint(Graphics g)
  {
    if(paintok)
    {
      // showfps
      if(show_fps)
      {
        Graphics g2 = bi.getGraphics();
        g2.setColor(Color.WHITE);
        g2.drawString("FPS = " + newfps, Screen.xpos + 16, Screen.ypos + 40);
      }

      g.drawImage(bi, Screen.xpos, Screen.ypos,
                  Screen.scaledw, Screen.scaledh, null);
      paintok = false;
    }

    //g.setColor(Color.WHITE);
    //g.drawString("FPS = " + newfps, Screen.xpos + 16, Screen.ypos + 40);
    //g.drawString("MaxSize = " + Screen.maxw + ", " + Screen.maxh, Screen.xpos + 16, Screen.ypos + 32);
    //g.drawString("FillRate = " + fps * backbuf.w * backbuf.h, 16, 32);
  }


  public void update(Graphics g)
  {
    // override update to prevent flicker
    if(paintok)
      paint(g);
  }

  public void stop()
  {
  }

  public void destroy()
  {
  }

  // set max fps (default is 60)
  public void maxFps(int hz)
  {
    if(hz > 125)
      hz = 125;
    frameTime = 1000000000 / hz;
  }

  // set Screen.w and Screen.g to appropriate values
  abstract public void gameSize();

  // game initialization
  abstract public void gameInit();

  // game logic section
  abstract public void gameLogic();

  // game rendering section
  abstract public void gameFrame();

}

