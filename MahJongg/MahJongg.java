import game2d.*;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.image.*;

public class MahJongg extends Game
{
  // game related
  boolean go_ahead = false;
  boolean restart_game = true;
  boolean paused = false;

  int game_mode = 0;
  int pause_timer = 0;
  int pulse;
  int menu_delay = 0;
  boolean paintok = false;

  int tilex = -1, tiley = -1, tilez = -1;
  int tilex1 = -1, tiley1 = -1, tilez1 = -1;
  int tilex2 = -1, tiley2 = -1, tilez2 = -1;
  boolean highlight = false;
  boolean selected = false;
  boolean lost = false;
  int tile_count;
  int wintile, winrotate;

  Board board = new Board();
  Tileset tileset = new Tileset(Sprite.tiles_sm, Sprite.bevel);
  Tileset tileset_hi = new Tileset(Sprite.tiles_hi, Sprite.bevel_hi);
  Tileset tileset_sel = new Tileset(Sprite.tiles_sel, Sprite.bevel_sel);

  FontBmp font16x32 = new FontBmp(Sprite.font16x32, 16);

  int MAX_LIGHTS = 2;
  Light light[] = new Light[MAX_LIGHTS];

  public void gameSize()
  {
    Screen.w = 640;
    Screen.h = 480;
  }

  public void gameInit()
  {
    maxFps(60);
    //show_fps = true;

    int i;

    // go back to main menu
    game_mode = 0;
    go_ahead = false;

    // lights
    for(i = 0; i < MAX_LIGHTS; i++)
      light[i] = new Light();

    // init bitmap light tables
    Bitmap.init(MAX_LIGHTS);
  }

  public void gameLogic()
  {
    // update pulser
    pulse += 4;
    pulse &= 1023;

    // game logic
    switch(game_mode)
    {
      case 0:
        if(menuLoop() == true)
        {
          game_mode = 1;
          go_ahead = false;
          restartGame();
        }
        break;
      case 1:
        if(actionLoop() == true)
        {
          game_mode = 0;
          go_ahead = false;
        }
        break;
      case 2:
        if(winLoop() == true)
        {
          game_mode = 0;
          go_ahead = false;
        }
        break;
    }
  }

  public void gameFrame()
  {
    // draw frame
    if(!paintok)
    {
      switch(game_mode)
      {
        case 0:
          menuFrame();
          break;
        case 1:
          actionFrame();
          break;
        case 2:
          winFrame();
          break;
      }
    }
  }

  public boolean actionLoop()
  {
    int i, j, use;

    // update background motion
    Background.update();

    // check quit
    if(input.getKey(KeyEvent.VK_Q))
    {
      sound.quiet();
      game_mode = 0;
      return true;
    }

    // check pause
    if(input.getKey(KeyEvent.VK_P) && pause_timer > 20)
    {
      pause_timer = 0;
      if(paused)
        paused = false;
      else
        paused = true;
    }

    pause_timer++;
    if(paused)
      return false;

    if(lost)
      return false;

    // check hint
    if(input.getKey(KeyEvent.VK_H))
    {
      input.setKey(KeyEvent.VK_H, false);
      checkHint();
    }

    // check mouse over tile
    highlight = false;
    int x, y, z;

    for(z = 0; z < 4; z++)
    {
      int yy = 32 - z * 4;
      for(y = 0; y < 8; y++)
      {
        int xx = Screen.w - 32 - 40 + z * 4;
        for(x = 15; x >= 0; x--)
        {
          int tile = board.getMap(x, y, z);
          if(tile != -1)
          {
            if(input.mousex >= xx && input.mousey >= yy
               && input.mousex < xx + 40 && input.mousey < yy + 56)
            {
              tilex = x;
              tiley = y;
              tilez = z;
              highlight = true;
            }
          }
          xx -= 36;
        }
        yy += 52;
      }
    }

    // see if theres any thing above
    if(highlight)
    {
      for(z = tilez + 1; z < 4; z++)
      {
        if(board.getMap(tilex, tiley, z) != -1)
          tilez = z;
      }
    }

    // check match
    if(highlight && input.button1)
    {
      input.button1 = false;
      if(selected)
      {
        tilex2 = tilex;
        tiley2 = tiley;
        tilez2 = tilez;
        if(match(tilex1, tiley1, tilez1, tilex2, tiley2, tilez2))
        {
          sound.play(Snd.match1, false);
          sound.play(Snd.match2, false);
          board.setMap(tilex1, tiley1, tilez1, -1);
          wintile = board.getMap(tilex2, tiley2, tilez2);
          board.setMap(tilex2, tiley2, tilez2, -1);
          selected = false;

          tile_count -= 2;
          if(tile_count <= 0)
          {
            game_mode = 2;
            go_ahead = false;
            sound.quiet();
            // play win music once
            sound.play(Snd.music4, false);
            sound.play(Snd.music5, false);
            return false;
          }

          if(checkLose())
            lost = true;
        }
        else
        {
          selected = false;
        }
      }
      else
      {
        tilex1 = tilex;
        tiley1 = tiley;
        tilez1 = tilez;
        sound.play(Snd.select, false);
        selected = true;
      }
    }

    // update animations
    for(i = 0; i < AnimList.count; i++)
      AnimList.anim[i].update();

    return false;
  }

  public void actionFrame()
  {
    int i;

    int lcount = 0;
    light[lcount].x = input.mousex;
    light[lcount].y = input.mousey;
    light[lcount].color = Col.makeRGB(32, 0, 0);
    lcount++;
    if(selected)
    {
      light[lcount].x = tilex1 * 36 + (32 + 20) + tilez1 * 4;
      light[lcount].y = tiley1 * 52 + (32 + 28) - tilez1 * 4;
      light[lcount].color = Col.makeRGB(32, 32, 0);
      lcount++;
    }

    // draw water
    Background.render(backbuf, Sprite.tex1, Sprite.tex2);

    // draw tiles
    int x, y, z;
    int black = Col.makeRGB(0, 4, 8);

    for(z = 0; z < 4; z++)
    {
      int yy = (32 + 28) - z * 4;

      for(y = 0; y < 8; y++)
      {
        int xx = Screen.w - (32 + 20) + z * 4;
        for(x = 15; x >= 0; x--)
        {
          int tile = board.getMap(x, y, z);
          if(tile != -1)
          {
            tileset.bitmap[z][tile].rotateLitSprite(backbuf, xx, yy, 256, 1.0f, light, lcount);
            if(highlight && x == tilex && y == tiley && z == tilez)
            {
              tileset_hi.bitmap[z][tile].rotateLitSprite(backbuf, xx, yy, 256, 1.0f, light, lcount);
            }
            if(selected && x == tilex1 && y == tiley1 && z == tilez1)
            {
              tileset_sel.bitmap[z][tile].rotateLitSprite(backbuf, xx, yy, 256, 1.0f, light, lcount);
            }
          }
          xx -= 36;
        }
        yy += 52;
      }
    }

    // draw lose
    if(lost)
    {
      float p = 1.0f + (float)Math.abs(0.5f * Trig.sin[pulse & 1023]);
      font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2, "No More Matches!", p);
    }

    // draw paused
    if(paused)
      backbuf.rectfill(0, 0, backbuf.w - 1, backbuf.h - 1,
                    Col.makeRGB(0, 0, 0), 160);
  }

  public boolean menuLoop()
  {
    // play music
    sound.play(Snd.music1, false);
    sound.play(Snd.music2, false);
    sound.play(Snd.music3, false);

    // wait for button to be released & sound to stop playing
    if(!input.button1)
      go_ahead = true;
    if(go_ahead && input.button1)
    {
      // stop music
      sound.quiet();
      return true;
    }

    return false;
  }

  public void menuFrame()
  {
    Sprite.menu_back.blit(backbuf, 0, 0, 0, 0,
                          Sprite.menu_back.w, Sprite.menu_back.h);
    float s = 1.0f - (float)Math.abs(0.0625f * Trig.sin[pulse & 1023]);
    int a = 256 + (int)(8 * Trig.sin[pulse & 1023]);
    Sprite.logo.rotateSprite(backbuf, Screen.w / 2, 160, a, s);
    s = 1.0f + (float)Math.abs(0.25 * Trig.sin[pulse & 1023]);
    font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2 + 64, "Click to Play!", s);

    // draw copyright
    font16x32.drawCenteredString(backbuf, Screen.w / 2, Screen.h - 8, "Copyright (c) 2012 Joe Davisson. All Rights Reserved.", 0.5f);
  }

  public boolean winLoop()
  {
    // play music
    //sound.play(Snd.music4, false);
    //sound.play(Snd.music5, false);

    // wait for button to be released & sound to stop playing
/*
    if(!input.button1)
      go_ahead = true;
    if(go_ahead && input.button1)
    {
      // stop music
      sound.quiet();
      return true;
    }
*/
    winrotate += 4;
    winrotate &= 1023;

    menu_delay++;
    if(menu_delay < 1000)
      return false;
    menu_delay = 0;

    sound.quiet();
    return true;
  }

  private void winFrame()
  {
    Sprite.menu_back.blit(backbuf, 0, 0, 0, 0,
                          Sprite.menu_back.w, Sprite.menu_back.h);
    float s = 3.0f + (float)Math.abs(1.0 * Trig.sin[pulse & 1023]);
    tileset.bitmap[3][wintile].rotateSprite(backbuf, (int)Screen.w / 2, (int)Screen.h / 2, winrotate, s);
    s = 2.0f + (float)Math.abs(0.5 * Trig.sin[pulse & 1023]);
    font16x32.drawCenteredString(backbuf, (int)Screen.w / 2, (int)Screen.h / 2, "You Win!!!", s);
  }

  private void restartGame()
  {
    go_ahead = false;

    // reset tiles
    board.reset();

    // reset mouse button
    input.button1 = false;

    lost = false;
    tile_count = 144;
    winrotate = 0;
  }

  private void checkHint()
  {
    int x1, y1, z1;
    int x2, y2, z2;

    for(z1 = 0; z1 < 4; z1++)
    {
      for(y1 = 0; y1 < 8; y1++)
      {
        for(x1 = 0; x1 < 16; x1++)
        {
          if(board.getMap(x1, y1, z1) != -1)
          {
            // try to find match
            for(z2 = 0; z2 < 4; z2++)
            {
              for(y2 = 0; y2 < 8; y2++)
              {
                for(x2 = 0; x2 < 16; x2++)
                {
                  if(x1 == x2 && y1 == y2 && z1 == z2)
                    continue;
                  if(match(x1, y1, z1, x2, y2, z2))
                  {
                    tilex1 = x1;
                    tiley1 = y1;
                    tilez1 = z1;
                    selected = true;
                    return;
                  }
                }
              }
            }
          }
        }
      }
    }
  }


  private boolean checkLose()
  {
    int x1, y1, z1;
    int x2, y2, z2;

    for(z1 = 0; z1 < 4; z1++)
    {
      for(y1 = 0; y1 < 8; y1++)
      {
        for(x1 = 0; x1 < 16; x1++)
        {
          if(board.getMap(x1, y1, z1) != -1)
          {
            // try to find match
            for(z2 = 0; z2 < 4; z2++)
            {
              for(y2 = 0; y2 < 8; y2++)
              {
                for(x2 = 0; x2 < 16; x2++)
                {
                  if(x1 == x2 && y1 == y2 && z1 == z2)
                    continue;
                  if(match(x1, y1, z1, x2, y2, z2))
                    return false;
                }
              }
            }
          }
        }
      }
    }
    // didn't find match
    return true;
  }

  private boolean match(int tx1, int ty1, int tz1, int tx2, int ty2, int tz2)
  {
    if(tx1 == tx2 && ty1 == ty2 && tz1 == tz2)
      return false;

    int z;
    for(z = tz1 + 1; z < 4; z++)
    {
      if(board.getMap(tx1, ty1, z) != -1)
        return false;
    }
    for(z = tz2 + 1; z < 4; z++)
    {
      if(board.getMap(tx2, ty2, z) != -1)
        return false;
    }

    int t1 = board.getMap(tx1, ty1, tz1);
    int t2 = board.getMap(tx2, ty2, tz2);

    if(t1 == -1 || t2 == -1)
      return false;

    if(t1 == t2)
    {
      if(board.getMap(tx1 - 1, ty1, tz1) != -1
         && board.getMap(tx1 + 1, ty1, tz1) != -1)
      {
        return false;
      }
      if(board.getMap(tx2 - 1, ty2, tz2) != -1
         && board.getMap(tx2 + 1, ty2, tz2) != -1)
      {
        return false;
      }
      return true;
    }

    return false;
  }

}

