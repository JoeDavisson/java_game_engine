import game2d.*;

public class Shrooms
{
   private int map[];

   public Shrooms()
   {
     int x, y;

     map = new int[16 * 16];

     for(y = 0; y < 16; y++)
       for(x = 0; x < 16; x++)
         map[x + 16 * y] = (((Rnd.get() % 5) == 1) ? 4 : 0);
   } 

   public int get(int x, int y)
   {
     if(x > 15 || y > 15 || x < 0 || y < 0)
       return 0;

     return map[x + 16 * y];
   }

   public void dec(int x, int y)
   {
     if(x > 15 || y > 15 || x < 0 || y < 0)
       return;

     int temp = map[x + 16 * y];

     temp--;
     if(temp < 0)
       temp = 0;

     map[x + 16 * y] = temp;

   }
}

