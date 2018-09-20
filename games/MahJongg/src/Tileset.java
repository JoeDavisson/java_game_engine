import game2d.*;

public class Tileset
{
  Bitmap bitmap[][];

  // makes new set of 42 tiles
  // width = 42 * 36, height = 52
  Tileset(Bitmap src, Bitmap bevelsrc)
  {
    bitmap = new Bitmap[4][42];

    int i, z;

    for(z = 0; z < 4; z++)
    {
      for(i = 0; i < 42; i++)
      {
        bitmap[z][i] = new Bitmap(40, 56);
        // draw bevel
        bevelsrc.blit(bitmap[z][i], 0, 0, 0, 0, 40, 56);
        // add tile face
        src.blitMasked(bitmap[z][i], i * 36, 0, 4, 0, 36, 52);
        // darken
        //bitmap[z][i].rect(4, 0, 39, 51, Col.makeRGB(0, 0, 0), 224);
        //bitmap[z][i].rect(5, 1, 38, 50, Col.makeRGB(0, 0, 0), 224);
        //darken(bitmap[z][i], 207 + z * 16); 
        darken(bitmap[z][i], 183 + z * 24); 
      }
    }
  }

  private void darken(Bitmap dest, int t)
  {
    int x, y;
    int tt = t;

    for(y = 0; y < dest.h; y++)
    {
      for(x = 0; x < dest.w; x++)
      {
        int c1 = dest.data[x + dest.row[y]];
        int c2 = Col.makeRGB(0, 8, 16);

        // darken border a little more so it stands out;
        if(dest.isEdge(x, y))
          tt = t - 64;
        else
          tt = t;

        // blend
        int rb = (((((c1 & 0xFF00FF) - (c2 & 0xFF00FF)) * tt) >> 8) + c2) & 0xFF00FF;
        int g = (((((c1 & 0xFF00) - (c2 & 0xFF00)) * tt) >> 8) + c2) & 0xFF00;

        if(dest.data[x + dest.row[y]] != 0xFFFF00FF)
          dest.data[x + dest.row[y]] = 0xFF000000 | rb | g;
      }
    }
  }
}
