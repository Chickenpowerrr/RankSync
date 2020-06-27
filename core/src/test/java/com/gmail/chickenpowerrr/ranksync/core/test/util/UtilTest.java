package com.gmail.chickenpowerrr.ranksync.core.test.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.gmail.chickenpowerrr.ranksync.core.util.Util;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class UtilTest {

  @Test
  public void testInterchange() {
    Map<String, Collection<Integer>> input = Util.mapOf("Test", Util.setOf(5, 7),
        "Another", Arrays.asList(8, 5));
    Map<Integer, Collection<String>> result = Util.interchange(input);

    assertThat(result).hasSize(3);
    assertThat(result).anySatisfy((key, value) -> {
      assertThat(key).isEqualTo(5);
      assertThat(value).hasSize(2);
      assertThat(value).containsOnlyOnce("Test", "Another");
    });

    assertThat(result).anySatisfy((key, value) -> {
      assertThat(key).isEqualTo(7);
      assertThat(value).hasSize(1);
      assertThat(value).containsOnlyOnce("Test");
    });

    assertThat(result).anySatisfy((key, value) -> {
      assertThat(key).isEqualTo(8);
      assertThat(value).hasSize(1);
      assertThat(value).containsOnlyOnce("Another");
    });
  }

  @Test
  public void testSneakyThrow() {
    assertThatThrownBy(() -> Util.sneakyThrow(new IOException())).isInstanceOf(IOException.class);
    assertThatThrownBy(() -> Util.sneakyThrow(new RuntimeException())).isInstanceOf(RuntimeException.class);
    assertThatThrownBy(() -> Util.sneakyThrow(new Exception())).isInstanceOf(Exception.class);
  }

  @Test
  public void testSneakyExecute() {
    assertThatThrownBy(() -> Util.sneakyExecute(() -> {
      throw new IOException();
    })).isInstanceOf(IOException.class);

    assertThat(Util.sneakyExecute(() -> "value")).isEqualTo("value");
  }

  @Test
  public void testFlatMap() {
    Collection<Collection<Integer>> input = Arrays.asList(Arrays.asList(1, 4), Arrays.asList(6, 2));
    Collection<Integer> result = Util.flatMap(input);

    assertThat(result).hasSize(4);
    assertThat(result).containsExactlyInAnyOrder(1, 2, 4, 6);
  }
}
