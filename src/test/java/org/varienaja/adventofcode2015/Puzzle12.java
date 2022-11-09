package org.varienaja.adventofcode2015;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2015.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2015">adventofcode.com</a>
 */
public class Puzzle12 extends PuzzleAbs {

  private boolean hasRedAttribute(JsonObject object) {
    for (Entry<String, JsonValue> entry : object.entrySet()) {
      if (entry.getValue().getValueType() == ValueType.STRING && "\"red\"".equals(entry.getValue().toString())) {
        return true;
      }
    }

    return false;
  }

  public int run2(String rawInput) {
    JsonReader reader = Json.createReader(new StringReader(rawInput));
    JsonValue rootObject = reader.read();
    reader.close();

    Queue<JsonValue> queue = new LinkedList<>();
    queue.add(rootObject);

    int sum = 0;
    while (!queue.isEmpty()) {
      if (queue.peek() instanceof JsonObject) {
        JsonObject currentObject = (JsonObject)queue.poll();
        if (hasRedAttribute(currentObject)) {
          continue;
        }

        for (Entry<String, JsonValue> entry : currentObject.entrySet()) {
          if (entry.getValue().getValueType() == ValueType.NUMBER) {
            sum += Integer.parseInt(entry.getValue().toString());
          } else if (entry.getValue().getValueType() == ValueType.OBJECT) {
            queue.add(entry.getValue());
          } else if (entry.getValue().getValueType() == ValueType.ARRAY) {
            queue.add(entry.getValue());
          }
        }
      }

      else {
        JsonArray currentArray = (JsonArray)queue.poll();
        for (JsonValue value : currentArray) {
          if (value.getValueType() == ValueType.NUMBER) {
            sum += Integer.parseInt(value.toString());
          } else if (value.getValueType() == ValueType.OBJECT) {
            queue.add(value);
          } else if (value.getValueType() == ValueType.ARRAY) {
            queue.add(value);
          }
        }
      }
    }

    return sum;
  }

  private int solveB(InputStream inJson) {
    Stack<Event> stack = new Stack<>();
    int sum = 0;
    int subsum = 0;
    int objectDepth = 0;
    int redAt = Integer.MAX_VALUE;
    JsonParser parser = Json.createParser(inJson);
    while (parser.hasNext()) {
      Event e = parser.next();
      switch (e) {
        case START_ARRAY:
          stack.push(e);
          break;
        case END_ARRAY:
          stack.pop();
          if (objectDepth < redAt) {
            sum += subsum;
          }
          subsum = 0;
          break;
        case END_OBJECT:
          stack.pop();
          if (objectDepth < redAt) {
            sum += subsum;
            redAt = Integer.MAX_VALUE;
          }
          objectDepth--;
          subsum = 0;
          break;
        case START_OBJECT:
          stack.push(e);
          objectDepth++;
          if (objectDepth < redAt) {
            sum += subsum;
          }
          subsum = 0;
          break;
        case VALUE_STRING:
          if ("red".equals(parser.getString()) && stack.peek() != Event.START_ARRAY) {
            redAt = Math.min(objectDepth, redAt);
          }
          break;
        case VALUE_NUMBER:
          int i = parser.getInt();
          subsum += i;
          break;
        default:
          break;
      }
    }
    return sum;
  }

  @Test
  public void testDay12() {

    announceResultA();
    List<String> lines = getInput();
    int sum = 0;
    for (String line : lines) {
      Pattern p = Pattern.compile("(-?\\d+)");
      Matcher m = p.matcher(line);
      int i = 0;
      while (m.find(i)) {
        sum += Integer.parseInt(line.substring(m.start(), m.end()));
        i = m.end();
      }
      System.out.println(sum);
    }
    assertEquals(191164, sum);

    announceResultB();
    assertEquals(4, solveB(new ByteArrayInputStream("[1,{\"a\":3,\"c\":\"red\",\"b\":2},3]".getBytes())));
    assertEquals(6, solveB(new ByteArrayInputStream("[1,2,3]".getBytes())));
    assertEquals(0, solveB(new ByteArrayInputStream("{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}".getBytes())));
    assertEquals(6, solveB(new ByteArrayInputStream("[1,\"red\",5]".getBytes())));

    sum = run2(lines.get(0));

    // sum = solveB(Puzzle12.class.getClassLoader().getResourceAsStream("2015/day12.txt"));
    System.out.println(sum);
    assertEquals(87842, sum); // 125170 too high
    // 9859 too low
    // 118775 incorrect
    // 122924
    // 7693 incorrect
  }

}
