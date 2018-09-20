// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class FontBmp
{
  public int w;
  public int h;

  public Bitmap character[];

  public FontBmp(Bitmap fontbmp, int charWidth)
  {
    w = charWidth;
    h = fontbmp.h;

    character = new Bitmap[96];

    int i;
    for(i = 0; i < 96; i++)
    {
      character[i] = new Bitmap(w, h);
      fontbmp.blit(character[i], i * w, 0, 0, 0, w, h);
    }
  }

  public void drawChar(Bitmap dest, int x, int y, char c, float scale)
  {
    c -= 32;
    if(c < 0 || c > 95)
      return;

    character[c].rotateSprite(dest, x, y, 256, scale);
  }

  public void drawString(Bitmap dest, int x, int y, String s, float scale)
  {
    float xx = x + (w / 2);
    float yy = y + (h / 2);
    float step = w * scale;
    int i;

    for(i = 0; i < s.length(); i++)
    {
      drawChar(dest, (int)xx, (int)yy, s.charAt(i), scale);
      xx += step;
    }
  }

  public void drawCenteredString(Bitmap dest, int x, int y, String s, float scale)
  {
    int len = s.length();
    int sw = (int)(len * (w * scale));
    float xx = x - (sw / 2) + ((w * scale) / 2);
    float step = (float)(w * scale);
    int i;

    for(i = 0; i < len; i++)
    {
      drawChar(dest, (int)xx, y, s.charAt(i), scale);
      xx += step;
    }
  }
}

