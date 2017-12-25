// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

import java.util.Arrays;

public class Collision
{
  // checks collision between two Prop-derived objects
  public static boolean check(Prop p1, Prop p2)
  {
    if(Collision.test(p1.anim.frame(),
                      (int)p1.x, (int)p1.y, (int)p1.angle, p1.scale,
                      p2.anim.frame(),
                      (int)p2.x, (int)p2.y, (int)p2.angle, p2.scale)) 
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  // test collision between two bitmaps
  public static boolean test(Bitmap img1, int xx1, int yy1, int a1, float s1,
                             Bitmap img2, int xx2, int yy2, int a2, float s2)
  {
    // rotations
    int duCol1 = (int)(Trig.sin[(1023 - a1) & 1023] * s1 * 256);
    int dvCol1 = (int)(Trig.sin[(1023 - (a1 + 256)) & 1023] * s1 * 256);
    int duRow1 = -dvCol1;
    int dvRow1 = duCol1;
    int ww1 = img1.w / 2;
    int hh1 = img1.h / 2;

    int duCol2 = (int)(Trig.sin[(1023 - a2) & 1023] * s2 * 256);
    int dvCol2 = (int)(Trig.sin[(1023 - (a2 + 256)) & 1023] * s2 * 256);
    int duRow2 = -dvCol2;
    int dvRow2 = duCol2;
    int ww2 = img2.w / 2;
    int hh2 = img2.h / 2;

    // origin
    int ox1 = xx1 + ww1;
    int oy1 = yy1 + hh1;
	
    int ox2 = xx2 + ww2;
    int oy2 = yy2 + hh2;
	
    // project new corners
    int x01 = xx1 - ox1;
    int y01 = yy1 - oy1;
    int x11 = xx1 + (img1.w - 1) - ox1;
    int y11 = yy1 - oy1;
    int x21 = xx1 - ox1;
    int y21 = yy1 + (img1.h - 1) - oy1;
    int x31 = xx1 + (img1.w - 1) - ox1;
    int y31 = yy1 + (img1.h - 1) - oy1;

    int x02 = xx2 - ox2;
    int y02 = yy2 - oy2;
    int x12 = xx2 + (img2.w - 1) - ox2;
    int y12 = yy2 - oy2;
    int x22 = xx2 - ox2;
    int y22 = yy2 + (img2.h - 1) - oy2;
    int x32 = xx2 + (img2.w - 1) - ox2;
    int y32 = yy2 + (img2.h - 1) - oy2;

    // rotate corners
    int newx01 = (int)(x01 * duCol1 + y01 * duRow1) >> 8;
    int newy01 = (int)(x01 * dvCol1 + y01 * dvRow1) >> 8;
    int newx11 = (int)(x11 * duCol1 + y11 * duRow1) >> 8;
    int newy11 = (int)(x11 * dvCol1 + y11 * dvRow1) >> 8;
    int newx21 = (int)(x21 * duCol1 + y21 * duRow1) >> 8;
    int newy21 = (int)(x21 * dvCol1 + y21 * dvRow1) >> 8;
    int newx31 = (int)(x31 * duCol1 + y31 * duRow1) >> 8;
    int newy31 = (int)(x31 * dvCol1 + y31 * dvRow1) >> 8;

    int newx02 = (int)(x02 * duCol2 + y02 * duRow2) >> 8;
    int newy02 = (int)(x02 * dvCol2 + y02 * dvRow2) >> 8;
    int newx12 = (int)(x12 * duCol2 + y12 * duRow2) >> 8;
    int newy12 = (int)(x12 * dvCol2 + y12 * dvRow2) >> 8;
    int newx22 = (int)(x22 * duCol2 + y22 * duRow2) >> 8;
    int newy22 = (int)(x22 * dvCol2 + y22 * dvRow2) >> 8;
    int newx32 = (int)(x32 * duCol2 + y32 * duRow2) >> 8;
    int newy32 = (int)(x32 * dvCol2 + y32 * dvRow2) >> 8;
	
    // adjust
    x01 = newx01 + xx1;
    y01 = newy01 + yy1;
    x11 = newx11 + xx1;
    y11 = newy11 + yy1;
    x21 = newx21 + xx1;
    y21 = newy21 + yy1;
    x31 = newx31 + xx1;
    y31 = newy31 + yy1;

    x02 = newx02 + xx2;
    y02 = newy02 + yy2;
    x12 = newx12 + xx2;
    y12 = newy12 + yy2;
    x22 = newx22 + xx2;
    y22 = newy22 + yy2;
    x32 = newx32 + xx2;
    y32 = newy32 + yy2;

    // find new bounding box
    int bx11 = Int.min(x01, Int.min(x11, Int.min(x21, x31)));
    int by11 = Int.min(y01, Int.min(y11, Int.min(y21, y31)));
    int bx21 = Int.max(x01, Int.max(x11, Int.max(x21, x31)));
    int by21 = Int.max(y01, Int.max(y11, Int.max(y21, y31)));
    int bw1 = (bx21 - bx11) / 2;
    int bh1 = (by21 - by11) / 2;

    int bx12 = Int.min(x02, Int.min(x12, Int.min(x22, x32)));
    int by12 = Int.min(y02, Int.min(y12, Int.min(y22, y32)));
    int bx22 = Int.max(x02, Int.max(x12, Int.max(x22, x32)));
    int by22 = Int.max(y02, Int.max(y12, Int.max(y22, y32)));
    int bw2 = (bx22 - bx12) / 2;
    int bh2 = (by22 - by12) / 2;

    // find overlapping box, cop out if no overlap
    int ix1 = Int.max(bx11, bx12);
    int iy1 = Int.max(by11, by12);
    int ix2 = Int.min(bx21, bx22);
    int iy2 = Int.min(by21, by22);

    int swap;

    if(ix2 < ix1)
    {
      swap = ix1;
      ix1 = ix2;
      ix2 = swap;
    }

    if(iy2 < iy1)
    {
      swap = iy1;
      iy1 = iy2;
      iy2 = swap;
    }

    if(ix2 < ix1 && iy2 < iy1)
      return false;

    int iw = ix2 - ix1;
    int ih = iy2 - iy1;

    int x, y;

    // small buffers for collision test
    int buf[] = new int[iw * ih];
    int row[] = new int[ih];

    for(y = 0; y < ih; y++)
      row[y] = iw * y;

    Arrays.fill(buf, 0);

    // new rotations
    duCol1 = (int)(Trig.sin[a1 & 1023] / s1 * 256);
    dvCol1 = (int)(Trig.sin[(a1 + 256) & 1023] / s1 * 256);
    duRow1 = -dvCol1;
    dvRow1 = duCol1;
    int rowU1 = ww1 << 8;
    int rowV1 = hh1 << 8;
    rowU1 -= bw1 * duCol1 + bh1 * duRow1;
    rowV1 -= bw1 * dvCol1 + bh1 * dvRow1;
	
    duCol2 = (int)(Trig.sin[a2 & 1023] / s2 * 256);
    dvCol2 = (int)(Trig.sin[(a2 + 256) & 1023] / s2 * 256);
    duRow2 = -dvCol2;
    dvRow2 = duCol2;
    int rowU2 = ww2 << 8;
    int rowV2 = hh2 << 8;
    rowU2 -= bw2 * duCol2 + bh2 * duRow2;
    rowV2 -= bw2 * dvCol2 + bh2 * dvRow2;

    int c1, c2;

    // sprite 1
    for(y = by11; y <= by21; y++)
    {
      int u1 = rowU1;
      int v1 = rowV1;
      rowU1 += duRow1;
      rowV1 += dvRow1;
      for(x = bx11; x <= bx21; x++)
      {
        if(x >= ix1 && x < ix2 && y >= iy1 && y < iy2)
        {
          int uu1 = u1 >> 8;
          int vv1 = v1 >> 8;
          if(uu1 < 0 || uu1 >= img1.w || vv1 < 0 || vv1 >= img1.h)
            c1 = 0xFFFF00FF;
          else
            c1 = img1.data[uu1 + img1.row[vv1]];
          if(c1 != 0xFFFF00FF)
            buf[x - ix1 + row[y - iy1]] = 1;
        }
         u1 += duCol1;
         v1 += dvCol1;
      }
    }

    // sprite 2
    for(y = by12; y <= by22; y++)
    {
      int u2 = rowU2;
      int v2 = rowV2;
      rowU2 += duRow2;
      rowV2 += dvRow2;
      for(x = bx12; x <= bx22; x++)
      {
        if(x >= ix1 && x < ix2 && y >= iy1 && y < iy2)
        {
          int uu2 = u2 >> 8;
          int vv2 = v2 >> 8;
          if(uu2 < 0 || uu2 >= img2.w || vv2 < 0 || vv2 >= img2.h)
            c2 = 0xFFFF00FF;
          else
          c2 = img2.data[uu2 + img2.row[vv2]];
          if(c2 != 0xFFFF00FF && buf[x - ix1 + row[y - iy1]] > 0)
            return true;
        }
        u2 += duCol2;
        v2 += dvCol2;
      }
    }

    return false;
  }
}

