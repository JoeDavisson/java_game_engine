import java.awt.*;
import java.awt.image.*;
import java.applet.Applet;
//import java.awt.AWTEvent;
//import java.awt.event.KeyEvent;
import java.awt.Event;
import javax.sound.sampled.*;
import java.util.Random;

public class Small extends Applet implements Runnable
{
  public Small()
  {
    new Thread(this).start();
  }

  boolean key[] = new boolean[65536];

  // screen
  static final int sw = 128;
  static final int sh = 192;

  int data[] = new int[sw * sh];
  int row[] = new int[sh];
  int pal[] = new int[16];

  DataBuffer db = new DataBufferInt(data, sw * sh);
  WritableRaster wr = Raster.createPackedRaster(db, sw, sh, sw, new int[]{ 0xFF0000, 0xFF00, 0xFF, 0xFF000000 }, null);
  ColorModel cm = ColorModel.getRGBdefault();

  BufferedImage bi = new BufferedImage(cm, wr, false, null);
  boolean pk = false;

    // sprites
    int s[] =
    {
      // 0 - ship
      0x000FF000,
      0x000FF000,
      0x000FF000,
      0x00FFFF00,
      0x0EEFFEE0,
      0xDDEEEEDD,
      0xCCDDDDCC,
      0x0CCCCCC0,
 
      // 1 - mush1
      0x00555500,
      0x05665550,
      0x50660055,
      0x00000000,
      0x00000000,
      0x00000000,
      0x00000000,
      0x00000000,

      // 2 - mush2
      0x00555500,
      0x05665550,
      0x56666555,
      0x56655555,
      0x55005055,
      0x00000000,
      0x00000000,
      0x00000000,

      // 3 - mush3
      0x00555500,
      0x05665550,
      0x56666555,
      0x56655555,
      0x55555555,
      0x00444400,
      0x00404400,
      0x00000000,

      // 4 - mush4
      0x00555500,
      0x05665550,
      0x56666555,
      0x56655555,
      0x55555555,
      0x00444400,
      0x00454400,
      0x00454400,

      // 5 shot
      0x000FF000,
      0x000FF000,
      0x00077000,
      0x00077000,
      0x00077000,
      0x00077000,
      0x00077000,
      0x00077000,

      // 6 snake1
      0x00999900,
      0x09AAA990,
      0x9ABBAA98,
      0x9ABBAA98,
      0x9AAAAA98,
      0x99AAA998,
      0x09999980,
      0x00888800,

      // 7 - numerical display
      0x01111000,
      0x60000200,
      0x60000200,
      0x07777000,
      0x50000300,
      0x50000300,
      0x04444000,
      0x00000000,

      // 8 - flea
      0x0DDDDDD0,
      0xDCCDDCCD,
      0xDECDDECD,
      0xDDDDDDDD,
      0xDDCCCCDD,
      0xDCCCCCCD,
      0xDCCCCCCD,
      0xDDDDDDDD
    };
 
  @Override
  public void run()
  {
    int i, j, k;

    // timing/repaint
    int fr = 0;

    // sound
    AudioFormat af = new AudioFormat(32768, 8, 1, true, true);
    SourceDataLine line = null;

    byte buf[] = new byte[1024];
    byte ch[] = new byte[4 * 65536];
    byte snd = 0;
    short ps = 0;
    short sz = 0;

    // misc
    Random rnd = new Random();

    // game related
    int score = 0;
    byte mr[] = new byte[16 * 24];
    float px = 0, py = 0;
    float ix = 0, iy = 0;
    boolean h = false;
    short hx = 0, hy = 0;
    byte sx[] = new byte[20];
    byte sy[] = new byte[20];
    byte dx[] = new byte[20];
    byte dy[] = new byte[20];
    byte sc = 0;
    boolean st[] = new boolean[20];
    byte sl = 0;
    byte gm = 0;

    // 7-segment display decoding
    byte seg[] = { 63, 6, 91, 79, 102, 109, 125, 7, 127, 111 };

    // back buffer rows
    for(i = 0; i < sh; i++)
      row[i] = sw * i;

    // palette
    for(i = 0; i < 16; i++)
      pal[i] = (i * 0x442211) | 0xFF000000;

    // sound
    try
    {
      line = AudioSystem.getSourceDataLine(af);
    }
    catch(LineUnavailableException e)
    {
    }

    try
    {
      line.open(af, 1024);
    }
    catch(LineUnavailableException e)
    {
    }

    int r = 0;
    for(i = 0; i < 4096; i++)
    {
      // shot sound
      ch[(1 << 16) + i] = (byte)((((i * 123 & (i >> 3)) & 255) - 128) / 12);
      // explosion sound
      ch[(2 << 16) + i] = (byte)(((r & 255) - 127) / 16);
      if((i & 7) == 7)
        r = rnd.nextInt();
    }

    // sound
    line.start();

    // main loop
    while(true)
    {
restart:
      switch(gm)
      {
        case 0:
          // new playfield
          for(i = 16; i < 288; i++)
            mr[i] = (rnd.nextInt() & 7) == 7 ? (byte)4 : (byte)0;

          px = 60;
          py = 176;
          sl = 8;
          score = 0;
          gm++;
        // drop through
        case 1:
          h = false;
          st[16] = false;

          for(i = 0; i < sl; i++)
          {
            sx[i] = (byte)((sl - 1) * 2 - i * 2);
            sy[i] = (byte)0;
            dx[i] = 1;
            dy[i] = 2;
            st[i] = true;
            sc = sl;
          }
          gm++;
          break;
      }

      // check keys
      if(key[106])
      {
        ix -= .1f;
      }

      if(key[108])
      {
        ix += .1f;
      }

      if(key[105])
      {
        iy -= .1f;
      }

      if(key[107])
      {
        iy += .1f;
      }

      if(key[32] && !h)
      {
        h = true;
        hx = (short)px;
        hy = (short)py;
        snd = 1; sz = 2048; ps = 0;
      }

      // check shot collisions
      if(h)
      {
        for(i = 0; i < 352; i++)
        {
          if(mr[i] > 0 && ((i % 16) == ((hx + 4) / 8)) && ((i / 16) == (hy / 8)))
          {
            score += 10;
            mr[i]--;
            h = false;
            snd = 2; sz = 4096; ps = 0;
            break;
          }
        }

        for(i = 0; i < 17; i++)
        {
          if(st[i] && ((hx + 4) / 8) == sx[i] / 2 && (hy / 8) == (sy[i] / 2))
          {
            score += 100;
            //if(i == 0)
            //  score += 500;
            st[i] = false;
            mr[(sy[i] / 2) * 16 + (sx[i] / 2)] = 4;
            sc--;
            if(sc < 1)
            {
              sl += 2;
              if(sl > 16)
                sl = 16;
              score += 1000;
              gm = 1;
            }
          }
        }
      }

      // move ship
      px += ix;
      py += iy;
      if(px < 0)
        px = 0;
      if(px > 120)
        px = 120;
      if(py < 128)
        py = 128;
      if(py > 176)
        py = 176;
      ix *= .9f;
      iy *= .9f;

      // move shot 
      hy -= 2;
      if(hy < 0)
        h = false;

      // flea
      i = rnd.nextInt() % 1024;
      if(!st[16] && i >= 0 && i < 16)
      {
        st[16] = true;
        sx[16] = (byte)(i * 2);
        sy[16] = 0;
      }

      if(st[16] & ((fr & 3) == 3))
      {
        if((i & 31) == 31)
          mr[(sy[16] / 2) * 16 + (sx[16] / 2)] = 4;
        sy[16]++;
        if(sy[16] > 46)
          st[16] = false;
      }

      // move snake
      if((fr % 6) == 0)
      for(i = 0; i < 17; i++)
      {
          // mushroom hit
          if(st[i] && mr[(sy[i] / 2) * 16 + (sx[i] / 2)] > 0)
          {
            dx[i] = (byte)-dx[i];
            //sx[i] += dx[i];
            sy[i] += dy[i];
          }

          // ship hit
          if(st[i] && (sx[i] == (int)px / 4) && (sy[i] == (int)py / 4))
          {
            // restart game
            gm = 0;
          }
          if(i == 16)
            break;

          // boundarys
          sx[i] += dx[i];

          if(sx[i] > 30)
          {
            sx[i] = 30;
            dx[i] = (byte)-dx[i];
            sy[i] += dy[i];
          }
          if(sx[i] < 0)
          {
            sx[i] = 0;
            dx[i] = (byte)-dx[i];
            sy[i] += dy[i];
          }

          if(sy[i] > 46 || sy[i] < 0)
          {
            sy[i] = 0;
            dy[i] = (byte)-dy[i];
            dx[i] = (byte)-dx[i];
          }
      } 

      // draw frame
      if(!pk)
      {
        // draw frame here

        // clear screen
        for(i = 0; i < sw * sh; i++)
          data[i] = 0xFF001100;

        // draw mushroom patch
        for(i = 0; i < 352; i++)
          if(mr[i] > 0)
            spr(mr[i], (i % 16) * 8, (i / 16) * 8);

        // draw snake
        for(i = 0; i < 16; i++)
        {
          if(st[i] && sx[i] >= 0 /* && sy[i] >= 0*/)
            spr(6, sx[i] * 4, sy[i] * 4);
        }

        // draw flea
        if(st[16])
          spr(8, sx[16] * 4, sy[16] * 4);

        // draw shot
        if(h)
          spr(5, hx, hy);

        // draw ship
        spr(0, (int)px, (int)py);

        // draw score
        int div = 1;
        short xx = 88;
        for(k = 0; k < 8; k++)
        {
          int val = (score / div) % 10;
          
          for(j = 0; j < 8; j++)
          {
            int temp = s[56 + j];

            for(i = 0; i < 8; i++)
            {
              int p = temp & 0xF;
              temp >>= 4;
              if( (p > 0) && (1 << (p - 1)) == (seg[val] & (1 << (p - 1))))
                data[row[184 + j] + xx + (7 - i)] = pal[15];
            }
          }
          xx -= 8;
          div *= 10;
        }

        // update window
        pk = true;
        repaint();
      }

      // write sound chunk (provides 1/32 second delay)
      if((fr++ & 3) == 3)
      {
        for(j = 0; j < 1024; j++)
        {
          buf[j] = 0;
          if(snd > 0)
            buf[j] = ch[(snd << 16) + ps + j];
        }

        ps += 1024;
        if(ps >= sz)
          snd = 0;

        line.write(buf, 0, 1024);
      }

    }
  }

  public void spr(int num, int x, int y)
  {
    short i, j;

    for(j = 0; j < 8; j++)
    {
      int temp = s[(num * 8) + j];
      for(i = 0; i < 8; i++)
      {
        int p = temp & 0xF;
        temp >>= 4;
        if(p > 0)
          data[row[y + j] + x + (7 - i)] = pal[p];
      }
    }
  }

  @Override
  public void update(Graphics g)
  {
    if(pk)
    {
      g.drawImage(bi, 0, 0, sw * 2, sh * 2, null);
      pk = false;
    }
  }

  @Override
  public boolean handleEvent(Event e)
  {
    return key[e.key] = (e.id == 401);
  }

}

