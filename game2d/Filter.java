package game2d;

public class Filter
{
  private static int buf[] = new int[1024];

  public static void antialias(Bitmap bmp)
  {
    int x, y;
    int p = 0;

    // read 1st line
    for(x = 0; x < bmp.w; x++)
      buf[x] = bmp.data[p++];

    // antialias
    for(y = 1; y < bmp.h; y++)
    {
      // skip 1st pixel
      int left = bmp.data[p];
      p++;
      for(x = 1; x < bmp.w; x++)
      {
        // read up/left pixels, combine with current
        int top = buf[x];
        int r = ((((top >> 16) & 0xFF) + ((left >> 16) & 0xFF) >> 1) + ((bmp.data[p] >> 16) & 0xFF)) >> 1;
        int g = ((((top >> 8) & 0xFF) + ((left >> 8) & 0xFF) >> 1) + ((bmp.data[p] >> 8) & 0xFF)) >> 1;
        int b = ((((top) & 0xFF) + ((left) & 0xFF) >> 1) + ((bmp.data[p]) & 0xFF)) >> 1;
        buf[x] = bmp.data[p];
        left = bmp.data[p] = b | (g << 8) | (r << 16) | 0xFF000000;
        p++;
      }
    }
  }
}
