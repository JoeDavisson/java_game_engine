import game2d.*;

public class Snd 
{
  public static Sample nothing = new Sample(4, 1000.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);

  public static Sample select = new Sample(2, 200.0f, 0.5f, 0.0f, 0.0f, .1f, 0.0f, 2.0f);

  public static Sample match1 = new Sample(3, 1, 0.3f, 0.1f, 0.1f, 0.4f, 0.3f, 0.0f, 3,
    "qeti");
  public static Sample match2 = new Sample(3, 2, 0.4f, 0.1f, 0.2f, 0.2f, 0.3f, 0.0f, 3,
    "etip");

  public static Sample music1 = new Sample(1, 1, 0.4f, 0.1f, 0.1f, 0.4f, 0.1f, 0.0f, 4,
    "i i t t i i 7 7 i i t t i i 7 7 i i t t i i 7 7 i t i t i t i t ");
  public static Sample music2 = new Sample(2, 2, 0.5f, 0.3f, 0.3f, 0.3f, 0.3f, 0.0f, 4,
    "iiii7 7 t t 7   iiii0 0 i i 7   iiii7 7 t t 7   iiii7trtr3q3q   ");
  public static Sample music3 = new Sample(4, 3, 0.3f, 0.0f, 0.0f, 0.05f, 0.1f, 0.0f, 4,
    "t   ewq t   e   t   ewq t   etitt   ewq t   e   t   ewq t   etit");
  public static Sample music4 = new Sample(3, 2, 0.5f, 0.3f, 0.9f, 0.2f, 0.2f, 0.0f, 3,
    "qwerwertertyrtyutyuiyuiopoiuytrereweqqqq");
  public static Sample music5 = new Sample(3, 1, 0.4f, 0.3f, 0.8f, 0.1f, 0.1f, 0.0f, 3,
    "q   w   e   r   t   y   p   y   r   q   ");
}

