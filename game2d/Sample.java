// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

import java.lang.String;

public class Sample
{
  public int buf[];
  public int size;
  public float volume;
  public int lowpass = 2;

  // sound effect generator
  public Sample(int waveform, float freq, float v,
    float attack, float decay, float sustain, float release,
    float sweep)
  {
    size = 32768;
    buf = new int[size];
    if(freq < 1)
      freq = 1;
    if(freq > 16384)
      freq = 16384;
    volume = v;

    int i, j, k;
    float t;

    sweep = 1.0f + (sweep / size);

    // waveform
    switch(waveform)
    {
      case 0:  // sine
        for(i = 0; i < size; i++)
        {
          t = freq * (float)i / size;
          buf[i] = (int)(127.0f * Math.sin(2.0f * Math.PI * t));
          freq *= sweep;
        }
        break;
      case 1:  // triangle
        for(i = 0; i < size; i++)
        {
          t = freq * (float)i / size;
          buf[i] = (int)(127.0f *
            (1.0f - 4.0f * Math.abs(Math.round(t - 0.25f) - (t - 0.25f))));
          freq *= sweep;
        }
        break;
      case 2:  // sawtooth
        for(i = 0; i < size; i++)
        {
          t = freq * (float)i / size;
          buf[i] = (int)(127.0f * (2.0f * (t - Math.floor(t + 0.5f))));
          freq *= sweep;
        }
        break;
      case 3:  // square
        for(i = 0; i < size; i++)
        {
          t = freq * (float)i / size;
          buf[i] = (int)(127.0f * Math.signum(Math.sin(2.0f * Math.PI * t)));
          freq *= sweep;
        }
        break;
      case 4:  // noise
        int rnd = (Rnd.get() & 255) - 128;
        boolean reset = false;
        for(i = 0; i < size; i++)
        {
          t = freq * (float)i / size;
          int test = (int)(127.0f *
            (2.0f * (2.0f * t - Math.floor(2.0f * t + 0.5f))));
          buf[i] = rnd;
          if(!reset && test > 0)
          {
            rnd = (Rnd.get() & 255) - 128;
            reset = true;
          }
          if(test < 0)
            reset = false;
          freq *= sweep;
        }
        break;
    }

    // ADSR
    int mode = 0;
    int a = (int)(attack * size);
    int d = a + (int)(decay * size);
    int s = d + (int)(sustain * size);
    int r = s + (int)(release * size);

    if(a < 1)
      a = 1;
    if(d < a + 1)
      d = a + 1;
    if(s < d + 1)
      s = d + 1;
    if(r < s + 1)
      r = s + 1;

    if(a > size)
      a = size;
    if(d > size)
      d = size;
    if(s > size)
      s = size;
    if(r > size)
      r = size;

    float a_inc = 1.0f / a;
    float d_inc = 0.5f / (d - a);
    float r_inc = 0.5f / (r - s);

    float y = 0;

    for(i = 0; i < a; i++)
    {
      buf[i] *= y;
      y += a_inc;
    }
    for(i = a; i < d; i++)
    {
      buf[i] *= y;
      y -= d_inc;
    }
    for(i = d; i < s; i++)
    {
      buf[i] *= y;
    }
    for(i = s; i < r; i++)
    {
      buf[i] *= y;
      y -= r_inc;
    }
    for(i = r; i < size; i++)
    {
      buf[i] = 0;
    }
    size = (r / 1024) * 1024;

    // clip
    for(i = 0; i < size; i++)
    {
      if(buf[i] > 127)
        buf[i] = 127;
      if(buf[i] < -127)
        buf[i] = -127;
    }
  }

  // music from string
  public Sample(int waveform, int octave, float v,
    float attack, float decay, float sustain, float release,
    float sweep, int speed, String data)
  {
    int length = 1024 * speed;
    int i, j;
    int base = 64;

    // these keys sorta line up with piano keys
    char key[] = { 'q', '2', 'w', '3', 'e', 'r', '5', 't', '6', 'y' ,
                   '7', 'u', 'i', '9', 'o', '0', 'p', '[', '=', ']' };
    int k[] = new int[65536];
    for(i = 0; i < 20; i++)
      k[key[i]] = i;
    Sample instrument[];
    instrument = new Sample[20];
    volume = v;
    float freq = (float)(base << octave);
    float mul = (float)Math.pow(2, 1.0f / 12);
    for(i = 0; i < 20; i++)
    {
      instrument[i] = new Sample(waveform, freq, v,
        attack, decay, sustain, release, sweep);
      freq *= mul;
    }

    size = length * data.length();
    buf = new int[size];
    int index = 0;

    // notes
    for(i = 0; i < data.length(); i++)
    {
      if(data.charAt(i) == ' ')
      {
        for(j = 0; j < length; j++)
          buf[index + j] = 0;
      }
      else
      {
        int note = k[data.charAt(i)];

        for(j = 0; j < length; j++)
          buf[index + j] = instrument[note].buf[j];
      }
      index += length;
    }
    
    // clip
    for(i = 0; i < size; i++)
    {
      if(buf[i] > 127)
        buf[i] = 127;
      if(buf[i] < -127)
        buf[i] = -127;
    }
  }
}
