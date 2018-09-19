// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

import java.awt.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.*;
//import java.util.Arrays;

public class Bitmap extends Component
{
  public int w, h;
  public int cl, cr, ct, cb;
  public int data[];
  public int row[];

  private static int mul[];
  private static boolean initialized = false;

  private static int ldist[];
  private static int la[];
  private static int luCol[];
  private static int lvCol[];
  private static int lvRow[];
  private static int lrowV[];
  private static int lv[];
  private static int lvv[];

  public Bitmap(int ww, int hh)
  {
    int i;

    w = ww;
    h = hh;
    cl = 0;
    ct = 0;
    cr = w - 1;
    cb = h - 1;
    data = new int[w * h];
    row = new int[h];

    for(i = 0; i < h; i++)
        row[i] = w * i;

    //Arrays.fill(data, Col.makeRGB(0, 0, 0));
  }

  public Bitmap(String s)
  {
    int i;

    BufferedImage src = null;
    try
    {
      src = ImageIO.read(getClass().getResource(s));
    }
    catch (IOException e)
    {
    }

    w = src.getWidth(null);
    h = src.getHeight(null);

    cl = 0;
    ct = 0;
    cr = w - 1;
    cb = h - 1;
    data = new int[w * h];
    row = new int[h];

    for(i = 0; i < h; i++)
      row[i] = w * i;
	
    int x, y;

    for(y = 0; y < h; y++)
      for(x = 0; x < w; x++)
        data[x + row[y]] = src.getRGB(x, y) | 0xFF000000;
  }

  // create tables
  public static void init(int lcount)
  {

    // multiplication table
    if(!initialized)
    {
      mul = new int[65536];
      int i, j;

      for(j = 0; j < 256; j++)
        for(i = 0; i < 256; i++)
          mul[i + (j << 8)] = (i * j) >> 8;

      // lighting tables
      ldist = new int[lcount];
      la = new int[lcount];
      luCol = new int[lcount];
      lvCol = new int[lcount];
      lvRow = new int[lcount];
      lrowV = new int[lcount];
      lv = new int[lcount];
      lvv = new int[lcount];

      initialized = true;
    }

  }

  public void clear(int c)
  {
    int i;

    for(i = 0; i < w * h; i++)
      data[i] = c;

    //Arrays.fill(data, c);
  }

  public void setpixel(int x, int y, int c, int t)
  {
    if(x < cl || x > cr  || y < ct || y > cb)
      return;

    data[x + row[y]] = Blend.current(data[x + row[y]], c, t);
  }

  public int getpixel(int x, int y)
  {
    if(x < cl || x > cr  || y < ct || y > cb)
      return 0;

    return data[x + row[y]];
  }

  public void hline(int x1, int y, int x2, int c, int t)
  {
    int swap;

    if(x2 < x1)
    {
      swap = x1;
      x1 = x2;
      x2 = swap;
    }
		
    if(x1 < cl)
      x1 = cl;
    if(x2 > cr)
      x2 = cr;
    if(y < ct)
      return;
    if(y > cb)
      return;

    while(x1 <= x2)
    {
      setpixel(x1, y, c, t);
      x1++;
    }
  }

  public void rect(int x1, int y1, int x2, int y2, int c, int t)
  {
    int swap;

    if(x2 < x1)
    {
      swap = x1;
      x1 = x2;
      x2 = swap;
    }

    if(y2 < y1)
    {
      swap = y1;
      y1 = y2;
      y2 = swap;
    }

    if(x1 < cl)
      x1 = cl;
    if(y1 < ct)
      y1 = ct;
    if(x2 > cr)
      x2 = cr;
    if(y2 > cb)
      y2 = cb;

    hline(x1, y1, x2, c, t);

    while(y1 < y2)
    {
      setpixel(x1, y1, c, t);
      setpixel(x2, y1, c, t);
      y1++;
    }
		
    hline(x1, y1, x2, c, t);
  }

  public void rectfill(int x1, int y1, int x2, int y2, int c, int t)
  {
    int swap;

    if(x2 < x1) {
      swap = x1;
      x1 = x2;
      x2 = swap;
    }

    if(y2 < y1) {
      swap = y1;
      y1 = y2;
      y2 = swap;
    }

    if(x1 < cl)
      x1 = cl;
    if(y1 < ct)
      y1 = ct;
    if(x2 > cr)
      x2 = cr;
    if(y2 > cb)
      y2 = cb;

    while(y1 <= y2)
    {
      hline(x1, y1, x2, c, t);
      y1++;
    }
  }

  // source bitmap must be ^2 size
  public void rotozoom(Bitmap dest, int xx, int yy, int angle, float scale)
  {
    int x, y;
    int ww = w / 2;
    int hh = h / 2;

    int duCol = (int)(Trig.sin[(1023 - angle) & 1023] / scale * 256);
    int dvCol = (int)(Trig.sin[(1023 - (angle + 256)) & 1023] / scale * 256);
    int duRow = -dvCol;
    int dvRow = duCol;

    int rowU = ww << 8;
    int rowV = hh << 8;
    rowU -= xx * duCol + yy * duRow;
    rowV -= xx * dvCol + yy * dvRow;

    int umask = w - 1;
    int vmask = h - 1;

    for(y = dest.ct; y <= dest.cb; y++)
    {
      int u = rowU;
      int v = rowV;
      rowU += duRow;
      rowV += dvRow;
      int p = dest.row[y] + dest.cl;

      for(x = dest.cl; x <= dest.cr; x++, p++)
      {
        int c = data[((u >> 8) & umask) + row[((v >> 8) & vmask)]];
        if(c != 0xFFFF00FF)
          dest.data[p] = c;
        u += duCol;
        v += dvCol;
      }
    }
  }

  // rotate/scale a sprite
  public void rotateSprite(Bitmap dest, int xx, int yy, int angle, float scale)
  {
    // rotation
    int duCol = (int)(Trig.sin[(1023 - angle) & 1023] * scale * 256);
    int dvCol = (int)(Trig.sin[(1023 - (angle + 256)) & 1023] * scale * 256);
    int duRow = -dvCol;
    int dvRow = duCol;
    int ww = w / 2;
    int hh = h / 2;

    // origin
    int ox = xx + ww;
    int oy = yy + hh;
	
    // project new corners
    int x0 = xx - ox;
    int y0 = yy - oy;
    int x1 = xx + (w - 1) - ox;
    int y1 = yy - oy;
    int x2 = xx - ox;
    int y2 = yy + (h - 1) - oy;
    int x3 = xx + (w - 1) - ox;
    int y3 = yy + (h - 1) - oy;

    // rotate
    int newx0 = (int)(x0 * duCol + y0 * duRow) >> 8;
    int newy0 = (int)(x0 * dvCol + y0 * dvRow) >> 8;
    int newx1 = (int)(x1 * duCol + y1 * duRow) >> 8;
    int newy1 = (int)(x1 * dvCol + y1 * dvRow) >> 8;
    int newx2 = (int)(x2 * duCol + y2 * duRow) >> 8;
    int newy2 = (int)(x2 * dvCol + y2 * dvRow) >> 8;
    int newx3 = (int)(x3 * duCol + y3 * duRow) >> 8;
    int newy3 = (int)(x3 * dvCol + y3 * dvRow) >> 8;

    x0 = newx0 + xx;
    y0 = newy0 + yy;
    x1 = newx1 + xx;
    y1 = newy1 + yy;
    x2 = newx2 + xx;
    y2 = newy2 + yy;
    x3 = newx3 + xx;
    y3 = newy3 + yy;

    // find new bounding box
    int bx1 = Int.min(x0, Int.min(x1, Int.min(x2, x3)));
    int by1 = Int.min(y0, Int.min(y1, Int.min(y2, y3)));
    int bx2 = Int.max(x0, Int.max(x1, Int.max(x2, x3)));
    int by2 = Int.max(y0, Int.max(y1, Int.max(y2, y3)));
    int bw = (bx2 - bx1) / 2;
    int bh = (by2 - by1) / 2;

    // draw

    duCol = (int)(Trig.sin[angle & 1023] / scale * 256);
    dvCol = (int)(Trig.sin[(angle + 256) & 1023] / scale * 256);
    duRow = -dvCol;
    dvRow = duCol;

    int rowU = ww << 8;
    int rowV = hh << 8;
    rowU -= bw * duCol + bh * duRow;
    rowV -= bw * dvCol + bh * dvRow;

    int x, y;

    for(y = by1; y <= by2; y++)
    {
      int u = rowU;
      int v = rowV;
      rowU += duRow;
      rowV += dvRow;
      if(y < dest.ct || y > dest.cb)
        continue;
      int p = dest.row[y] + bx1;
      for(x = bx1; x <= bx2; x++, p++)
      {
        int uu = u >> 8;
        int vv = v >> 8;
        u += duCol;
        v += dvCol;
        if(uu < 0 || uu >= w || vv < 0 || vv >= h)
          continue;
        int c = data[uu + row[vv]];
        if(x < dest.cl || x > dest.cr || c == 0xFFFF00FF)
          continue;
        dest.data[p] = c;
      }
    }
  }

  // rotate/scale with lighting
  public void rotateLitSprite(Bitmap dest, int xx, int yy, int angle, float scale, Light light[], int lcount)
  {
    // rotation
    int duCol = (int)(Trig.sin[(1023 - angle) & 1023] * scale * 256);
    int dvCol = (int)(Trig.sin[(1023 - (angle + 256)) & 1023] * scale * 256);
    int duRow = -dvCol;
    int dvRow = duCol;
    int ww = w / 2;
    int hh = h / 2;

    // origin
    int ox = xx + ww;
    int oy = yy + hh;
	
    // project new corners
    int x0 = xx - ox;
    int y0 = yy - oy;
    int x1 = xx + (w - 1) - ox;
    int y1 = yy - oy;
    int x2 = xx - ox;
    int y2 = yy + (h - 1) - oy;
    int x3 = xx + (w - 1) - ox;
    int y3 = yy + (h - 1) - oy;

    // rotate
    int newx0 = (int)(x0 * duCol + y0 * duRow) >> 8;
    int newy0 = (int)(x0 * dvCol + y0 * dvRow) >> 8;
    int newx1 = (int)(x1 * duCol + y1 * duRow) >> 8;
    int newy1 = (int)(x1 * dvCol + y1 * dvRow) >> 8;
    int newx2 = (int)(x2 * duCol + y2 * duRow) >> 8;
    int newy2 = (int)(x2 * dvCol + y2 * dvRow) >> 8;
    int newx3 = (int)(x3 * duCol + y3 * duRow) >> 8;
    int newy3 = (int)(x3 * dvCol + y3 * dvRow) >> 8;

    x0 = newx0 + xx;
    y0 = newy0 + yy;
    x1 = newx1 + xx;
    y1 = newy1 + yy;
    x2 = newx2 + xx;
    y2 = newy2 + yy;
    x3 = newx3 + xx;
    y3 = newy3 + yy;

    // find new bounding box
    int bx1 = Int.min(x0, Int.min(x1, Int.min(x2, x3)));
    int by1 = Int.min(y0, Int.min(y1, Int.min(y2, y3)));
    int bx2 = Int.max(x0, Int.max(x1, Int.max(x2, x3)));
    int by2 = Int.max(y0, Int.max(y1, Int.max(y2, y3)));
    int bw = (bx2 - bx1) / 2;
    int bh = (by2 - by1) / 2;

    // fill lighting tables
    int i;

    for(i = 0; i < lcount; i++)
    {
      // find distance from light
      ldist[i] = Int.fdist(xx, yy, light[i].x, light[i].y);

      // clip
      if(ldist[i] < 0)
        ldist[i] = 0;
      if(ldist[i] > 255)
        ldist[i] = 255;
      ldist[i] = 255 - ldist[i];

      // find angle to light
      la[i] = (int)Trig.angle((float)(yy - light[i].y), (float)(xx - light[i].x));

      luCol[i] = (int)(Trig.sin[la[i] & 1023] / scale * 256);
      lvCol[i] = (int)(Trig.sin[(la[i] + 256) & 1023] / scale * 256);
      lvRow[i] = luCol[i];

      lrowV[i] = hh << 8;
      lrowV[i] -= bw * lvCol[i] + bh * lvRow[i];
    }

    // draw
    duCol = (int)(Trig.sin[angle & 1023] / scale * 256);
    dvCol = (int)(Trig.sin[(angle + 256) & 1023] / scale * 256);
    duRow = -dvCol;
    dvRow = duCol;

    int rowU = ww << 8;
    int rowV = hh << 8;
    rowU -= bw * duCol + bh * duRow;
    rowV -= bw * dvCol + bh * dvRow;

    int x, y;

    for(y = by1; y <= by2; y++)
    {
      int u = rowU;
      int v = rowV;
      for(i = 0; i < lcount; i++)
      {
        lv[i] = lrowV[i];
        lrowV[i] += lvRow[i];
      }
      rowU += duRow;
      rowV += dvRow;
      if(y < dest.ct || y > dest.cb)
        continue;
      int p = dest.row[y] + bx1;
      for(x = bx1; x <= bx2; x++, p++)
      {
        int uu = u >> 8;
        int vv = v >> 8;
        for(i = 0; i < lcount; i++)
        {
          lvv[i] = lv[i] >> 8;
          lv[i] += lvCol[i];
        }
        u += duCol;
        v += dvCol;
        if(uu < 0 || uu >= w || vv < 0 || vv >= h)
          continue;
        int c = data[uu + row[vv]];
        if(x < dest.cl || x > dest.cr || c == 0xFFFF00FF)
          continue;

        int r = (c >> 16) & 0xFF;
        int g = (c >> 8) & 0xFF;
        int b = c & 0xFF;
     
        for(i = 0; i < lcount; i++)
        {
          int t = ldist[i] - lvv[i];
          if(t < 0)
            t = 0;
          if(t > 255)
            t = 255;
          r += mul[((light[i].color >> 16) & 0xFF) + (t << 8)];
          g += mul[((light[i].color >> 8) & 0xFF) + (t << 8)];
          b += mul[(light[i].color & 0xFF) + (t << 8)];
        }
        r = r > 255 ? 255 : r;
        g = g > 255 ? 255 : g;
        b = b > 255 ? 255 : b;

        dest.data[p] = b | (g << 8) | (r << 16) | 0xFF000000;
      }
    }
  }

  // warning: does not clip
  public void stretchBlit(Bitmap dest,
      int xs1, int ys1, int xs2, int ys2,
      int xd1, int yd1, int xd2, int yd2)
  {
    xs2 += xs1;
    xs2--;
    ys2 += ys1;
    ys2--;
    xd2 += xd1;
    xd2--;
    yd2 += yd1;
    yd2--;

    int dx = Int.abs(yd2 - yd1);
    int dy = Int.abs(ys2 - ys1) << 1;
    int sx = Int.sign(yd2 - yd1);
    int sy = Int.sign(ys2 - ys1);
    int e = dy - dx;
    int dx2 = dx << 1;
    int d;

    for(d = 0; d <= dx; d++)
    {
      int ddx = Int.abs(xd2 - xd1);
      int ddy = Int.abs(xs2 - xs1) << 1;
      int ssx = Int.sign(xd2 - xd1);
      int ssy = Int.sign(xs2 - xs1);
      int ee = ddy - ddx;
      int ddx2 = ddx << 1;

      int p = dest.row[yd1] + xd1;
      int q = row[ys1] + xs1;
      int dd;
	
      for(dd = 0; dd <= ddx; dd++)
      {
        dest.data[p] = data[q];
        while(ee >= 0)
        {
          q += ssy;
          ee -= ddx2;
        }
        p += ssx;
        ee += ddy;
      }

      while(e >= 0)
      {
        ys1 += sy;
        e -= dx2;
      }

      yd1 += sx;
      e += dy;
    }
  }

  public void blit(Bitmap dest,
    int sx, int sy, int dx, int dy, int ww, int hh)
  {
    int x, y;

    if((sx >= w) || (sy >= h) ||
    (dx >= dest.cr) || (dy >= dest.cb))
      return;

    // clip src left
    if(sx < 0)
    {
      ww += sx;
      dx -= sx;
      sx = 0;
    }

    // clip src top
    if(sy < 0)
    {
      hh += sy;
      dy -= sy;
      sy = 0;
    }

    // clip src right
    if((sx + ww) > w)
      ww = w - sx;

    // clip src bottom
    if((sy + hh) > h)
      hh = h - sy;

    // clip dest left
    if(dx < dest.cl)
    {
      dx -= dest.cl;
      ww += dx;
      sx -= dx;
      dx = dest.cl;
    }

    // clip dest top
    if(dy < dest.ct)
    {
      dy -= dest.ct;
      hh += dy;
      sy -= dy;
      dy = dest.ct;
    }

    // clip dest right
    if((dx + ww - 1) > dest.cr)
      ww = dest.cr - dx;

    // clip dest bottom
    if((dy + hh - 1) > dest.cb)
      hh = dest.cb - dy;

    if(ww < 1 || hh < 1)
      return;

    int sy1 = sy;
    int dy1 = dy;
    for(y = 0; y < hh; y++)
    {
      int sx1 = sx + row[sy1];
      int dx1 = dx + dest.row[dy1];
      for(x = 0; x < ww; x++, sx1++, dx1++)
        dest.data[dx1] = data[sx1];
      sy1++;
      dy1++;
      // could maybe use this but its a function call
      //System.arraycopy(data, sx + row[sy + y],
      //                 dest.data, dx + dest.row[dy + y], ww);
    }
  }

  public void blitMasked(Bitmap dest,
    int sx, int sy, int dx, int dy, int ww, int hh)
  {
    int x, y;

    if((sx >= w) || (sy >= h) ||
    (dx >= dest.cr) || (dy >= dest.cb))
      return;

    // clip this left
    if(sx < 0)
    {
      ww += sx;
      dx -= sx;
      sx = 0;
    }

    // clip this top
    if(sy < 0)
    {
      hh += sy;
      dy -= sy;
      sy = 0;
    }

    // clip this right
    if((sx + ww) > w)
      ww = w - sx;

    // clip this bottom
    if((sy + hh) > h)
      hh = h - sy;

    // clip dest left
    if(dx < dest.cl)
    {
      dx -= dest.cl;
      ww += dx;
      sx -= dx;
      dx = dest.cl;
    }

    // clip dest top
    if(dy < dest.ct)
    {
      dy -= dest.ct;
      hh += dy;
      sy -= dy;
      dy = dest.ct;
    }

    // clip dest right
    if((dx + ww - 1) > dest.cr)
      ww = dest.cr - dx;

    // clip dest bottom
    if((dy + hh - 1) > dest.cb)
      hh = dest.cb - dy;

    if(ww < 1 || hh < 1)
      return;

    int sy1 = sy;
    int dy1 = dy;
    for(y = 0; y < hh; y++)
    {
      int sx1 = sx + row[sy1];
      int dx1 = dx + dest.row[dy1];
      for(x = 0; x < ww; x++, sx1++, dx1++)
      {
        if(data[sx1] != 0xFFFF00FF)
          dest.data[dx1] = data[sx1];
      }
      sy1++;
      dy1++;
      // could use this but its a function call
      //System.arraycopy(data, sx + row[sy + y],
      //                 dest.data, dx + dest.row[dy + y], ww);
    }
  }

  public void blitMaskedTrans(Bitmap dest,
    int sx, int sy, int dx, int dy, int ww, int hh, int t)
  {
    int x, y;

    if((sx >= w) || (sy >= h) ||
    (dx >= dest.cr) || (dy >= dest.cb))
      return;

    // clip this left
    if(sx < 0)
    {
      ww += sx;
      dx -= sx;
      sx = 0;
    }

    // clip this top
    if(sy < 0)
    {
      hh += sy;
      dy -= sy;
      sy = 0;
    }

    // clip this right
    if((sx + ww) > w)
      ww = w - sx;

    // clip this bottom
    if((sy + hh) > h)
      hh = h - sy;

    // clip dest left
    if(dx < dest.cl)
    {
      dx -= dest.cl;
      ww += dx;
      sx -= dx;
      dx = dest.cl;
    }

    // clip dest top
    if(dy < dest.ct)
    {
      dy -= dest.ct;
      hh += dy;
      sy -= dy;
      dy = dest.ct;
    }

    // clip dest right
    if((dx + ww - 1) > dest.cr)
      ww = dest.cr - dx;

    // clip dest bottom
    if((dy + hh - 1) > dest.cb)
      hh = dest.cb - dy;

    if(ww < 1 || hh < 1)
      return;

    int sy1 = sy;
    int dy1 = dy;
    for(y = 0; y < hh; y++)
    {
      int sx1 = sx + row[sy1];
      int dx1 = dx + dest.row[dy1];
      for(x = 0; x < ww; x++, sx1++, dx1++)
      {
        if(data[sx1] != 0xFFFF00FF)
          dest.data[dx1] = data[sx1];
      }
      sy1++;
      dy1++;
      // could use this but its a function call
      //System.arraycopy(data, sx + row[sy + y],
      //                 dest.data, dx + dest.row[dy + y], ww);
    }
  }

  // check edge boundary (for making outline effect)
  public boolean isEdge(int x, int y)
  {
    if(getpixel(x, y) == 0xFFFF00FF)
      return false;
    if(x <= cl || x >= cr || y <= ct || y >= cb)
      return true;
    if( (getpixel(x + 1, y) == 0xFFFF00FF)
     || (getpixel(x - 1, y) == 0xFFFF00FF)
     || (getpixel(x, y + 1) == 0xFFFF00FF)
     || (getpixel(x, y - 1) == 0xFFFF00FF) )
      return true;
    else
      return false;
  }
}

