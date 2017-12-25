// Copyright (c) 2012 Joe Davisson. All Rights Reserved.

package game2d;

public class PropList
{
  int max = 0;
  public Prop prop[];

  public PropList(int maxprops)
  {
    max = maxprops;

    prop = new Prop[max];
    int i;

    for(i = 0; i < max; i++)
    {
      prop[i] = new Prop();
    }
  }

  public void add(Prop a)
  {
    int i;

    // find empty slot
    for(i = 0; i < max; i++)
    {
      if(!prop[i].status)
        break;
    }

    // add prop to list
    if(i < max)
      prop[i] = a;
  }
}

