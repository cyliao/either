package com.smart.util;

/**
 * Created by smartliao on 2019-07-18.
 */
public class BiTuple<F, S> {

  private F first;
  private S second;

  public F getFirst() {
    return first;
  }

  public S getSecond() {
    return second;
  }

  private BiTuple(F first, S second) {
    this.first = first;
    this.second = second;
  }

  public static <F, S> BiTuple<F, S> create(F first, S second) {
    return new BiTuple<>(first, second);
  }
}
