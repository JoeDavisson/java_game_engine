// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

import java.io.*;
import javax.sound.sampled.*;
import java.lang.*;
import java.util.Arrays;

public class Sound extends Thread
{
  private byte buf[];
  private AudioFormat af;
  private SourceDataLine line;

  private Sample channel[];
  private int MAX_CHANNELS = 16;
  private int CHUNK_SIZE = 1024;

  private boolean play[];
  private int pos[];

  private Sample empty = new Sample(4, 1000.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);

  public Sound()
  {
    int i;

    af = new AudioFormat(32768, 8, 1, true, true);
    try
    {
      line = AudioSystem.getSourceDataLine(af);
    }
    catch(LineUnavailableException e)
    {
    }
      
    try
    {
      line.open(af, CHUNK_SIZE);
    }
    catch(LineUnavailableException e)
    {
    }

    buf = new byte[CHUNK_SIZE];

    channel = new Sample[MAX_CHANNELS];
    play = new boolean[MAX_CHANNELS];
    pos = new int[MAX_CHANNELS];

    for(i = 0; i < MAX_CHANNELS; i++)
    {
      channel[i] = empty;
      play[i] = false;
      pos[i] = 0;
    }

    line.start();
    start();
  }

  public void play(Sample sample, boolean allowDuplicate)
  {
    int i;

    // find empty channel
    int use = -1;
    for(i = 0; i < MAX_CHANNELS; i++)
    {
      // bail if sound is already playing
      if(!allowDuplicate && (sample == channel[i]) && play[i])
        return;

      if(!play[i])
      {
        use = i;
        break;
      }
    }

    if(use == -1)
      return;

    // set sample to channel
    channel[use] = sample;
    play[use] = true;
    pos[use] = 0;
  }

  public void quiet()
  {
    int i;

    for(i = 0; i < MAX_CHANNELS; i++)
    {
      play[i] = false;
      pos[i] = 0;
    }
  }

  public void run()
  {
    int i, j;

    while(true)
    {  
      // mix playing samples into chunk
      for(j = 0; j < CHUNK_SIZE; j++)
      {
        int val = 0;

        for(i = 0; i < MAX_CHANNELS; i++)
          if(play[i])
            val += (int)(channel[i].buf[pos[i] + j] * channel[i].volume);

        if(val > 127)
          val = 127;
        if(val < -127)
          val = -127;

        buf[j] = (byte)val;
      }

      // update current positions in samples
      for(i = 0; i < MAX_CHANNELS; i++)
      {
        pos[i] += CHUNK_SIZE;
        if(pos[i] >= channel[i].size)
        {
          //channel[i] = Snd.empty;
          play[i] = false;
          pos[i] = 0;
        }
      }

      // write chunk (this function is blocking so we don't need to sleep)
      line.write(buf, 0, CHUNK_SIZE);
    }
  }
}

