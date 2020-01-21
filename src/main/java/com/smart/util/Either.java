package com.smart.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by smartliao on 2019-06-26.
 */
@SuppressWarnings("unchecked")
public class Either<L, R> {

  private L leftValue;
  private R rightValue;

  private Either(L left, R right) {
    this.leftValue = left;
    this.rightValue = right;
  }

  public static <L, R> Either<L, R> left(L left) {
    return new Either<>(Objects.requireNonNull(left), null);
  }

  public static <L, R> Either<L, R> left(L left, L defaultValue) {
    return Objects.isNull(left) ? left(defaultValue) : left(left);
  }

  public static <L, R> Either<L, R> right(R right) {
    return new Either<>(null, Objects.requireNonNull(right));
  }

  public static <L, R> Either<L, R> right(R right, R defaultValue) {
    return Objects.isNull(right) ? right(defaultValue) : right(right);
  }

  public boolean isLeft() {
    return Objects.nonNull(leftValue);
  }

  public boolean isRight() {
    return Objects.nonNull(rightValue);
  }

  public <U> Either<U, R> leftMap(Function<? super L, U> function) {
    Objects.requireNonNull(function);
    return isLeft() ? left(function.apply(leftValue)) : (Either<U, R>) this;
  }

  public <U> Either<U, R> leftFlatMap(Function<? super L, Either<U, R>> function) {
    Objects.requireNonNull(function);
    return isLeft() ? function.apply(leftValue) : (Either<U, R>) this;
  }

  public <U> Either<L, U> rightMap(Function<? super R, U> function) {
    Objects.requireNonNull(function);
    return isRight() ? right(function.apply(rightValue)) : (Either<L, U>) this;
  }

  public <U> Either<L, U> rightFlatMap(Function<? super R, Either<L, U>> function) {
    Objects.requireNonNull(function);
    return isRight() ? function.apply(rightValue) : (Either<L, U>) this;
  }

  public <T, U> Either<T, U> map(Function<? super L, T> leftFun, Function<? super R, U> rightFun) {
    Objects.requireNonNull(leftFun);
    Objects.requireNonNull(rightFun);
    return isLeft() ? left(leftFun.apply(leftValue)) : right(rightFun.apply(rightValue));
  }

  public <T, U> Either<T, U> flatMap(Function<? super L, Either<T, U>> leftFun, Function<? super R, Either<T, U>> rightFun) {
    Objects.requireNonNull(leftFun);
    Objects.requireNonNull(rightFun);
    return isLeft() ? leftFun.apply(leftValue) : rightFun.apply(rightValue);
  }

  public <T, U> Either<BiTuple<L, T>, U> leftZipWhen(Function<? super L, T> function) {
    Objects.requireNonNull(function);
    return isLeft() ? left(BiTuple.create(leftValue, function.apply(leftValue))) : (Either<BiTuple<L, T>, U>) this;
  }

  public <T, U> Either<T, BiTuple<R, U>> rightZipWhen(Function<? super R, U> function) {
    Objects.requireNonNull(function);
    return isRight() ? right(BiTuple.create(rightValue, function.apply(rightValue))) : (Either<T, BiTuple<R, U>>) this;
  }

  private <T, U> Either<BiTuple<T, L>, U> zipLeftValue(T value) {
    return isLeft() ? left(BiTuple.create(value, leftValue)) : (Either<BiTuple<T, L>, U>) this;
  }

  private <T, U> Either<T, BiTuple<U, R>> zipRightValue(U value) {
    return isRight() ? right(BiTuple.create(value, rightValue)) : (Either<T, BiTuple<U, R>>) this;
  }

  public <T, U> Either<BiTuple<L, T>, U> leftFlatZipWhen(Function<? super L, Either<T, U>> function) {
    Objects.requireNonNull(function);
    return isLeft() ? function.apply(leftValue).zipLeftValue(leftValue) : (Either<BiTuple<L, T>, U>) this;
  }

  public <T, U> Either<T, BiTuple<R, U>> rightFlatZipWhen(Function<? super R, Either<T, U>> function) {
    Objects.requireNonNull(function);
    return isRight() ? function.apply(rightValue).zipRightValue(rightValue) : (Either<T, BiTuple<R, U>>) this;
  }

  public <T> T get(Function<? super L, T> leftFun, Function<? super R, T> rightFun) {
    return isLeft() ? leftFun.apply(leftValue) : rightFun.apply(rightValue);
  }

  public <T> T getValue() {
    return isLeft() ? (T) leftValue : (T) rightValue;
  }

  public void accept(Consumer<L> leftFun, Consumer<R> rightFun) {
    if (isLeft()) {
      leftFun.accept(leftValue);
    } else {
      rightFun.accept(rightValue);
    }
  }

  public void ifLeft(Consumer<L> leftFun) {
    if (isLeft()) {
      leftFun.accept(leftValue);
    }
  }

  public void ifRight(Consumer<R> rightFun) {
    if (isRight()) {
      rightFun.accept(rightValue);
    }
  }

  public L getLeft() {
    return leftValue;
  }

  public R getRight() {
    return rightValue;
  }
}
