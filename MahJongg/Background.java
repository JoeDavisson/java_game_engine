import game2d.*;

public class Background
{
  private static int frame = 0;
  private static int pulse = 0;
  private static int offset = 0;

  public static void render(Bitmap dest, Bitmap tile1, Bitmap tile2)
  {
    int x, y;

    // transparency
    int t = 192 + (int)(16 * Trig.sin[pulse & 1023]);

    // tile2 offset
    //int u = (offset & 255) / 2;
    int u = 0;

    // current pixel
    int p = 0;

    // turbulance
    int turb = (offset & 1023);

    // current pixel
    p = 0;

    for(y = 0; y < dest.h; y++)
    {
      int offy1 = tile2.row[y & 127];
      int offy2 = tile1.row[(u + y) & 127];
      for(x = 0; x < dest.w; x++)
      {
        int offx1 = (x & 127);
        int offx2 = (128 - u + x) & 127;

        // generate turbulance
        int xx = (((x << 8) + 32 * Trig.sin2[((turb + y) & 1023)]) >> 8) & 127;
        int yy = (((y << 8) + 32 * Trig.sin2[((turb + x) & 1023)]) >> 8) & 127;

        int c1 = tile2.data[xx + (yy << 7)];
        int c2 = tile1.data[offx2 + offy2];

        // blend
        int rb = (((((c1 & 0xFF00FF) - (c2 & 0xFF00FF)) * t) >> 8) + c2) & 0xFF00FF;
        int g = (((((c1 & 0xFF00) - (c2 & 0xFF00)) * t) >> 8) + c2) & 0xFF00;

        dest.data[p] = 0xFF000000 | rb | g;
        p++;
      }
    }
  }  
  
  public static void update()
  {
    frame++;
    if(frame > 0)
    {
      frame = 0;
      offset++;
    }

    // update pulser
    pulse += 4;
    pulse &= 1023;

  }

}

