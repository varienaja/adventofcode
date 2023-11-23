package org.varienaja.adventofcode2019;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Test;
import org.varienaja.PuzzleAbs;

/**
 * Solutions for Advent of Code 2019.
 *
 * @author Varienaja
 * @see <a href="https://adventofcode.com/2019">adventofcode.com</a>
 */
public class Puzzle25 extends PuzzleAbs {

  // TODO much cleanup needed, but I got my answer!!

  class FoundCodeException extends RuntimeException {
    long code;

    public FoundCodeException(long code) {
      this.code = code;
    }

    long getCode() {
      return code;
    }
  }

  class Room {
    String name;
    String description;
    Map<String, Room> checkeddirections = new HashMap<>();
    Set<String> items = new HashSet<>();

    public Room(String name, String description, List<String> doors, List<String> items) {
      this.name = name;
      this.description = description;
      for (String door : doors) {
        if ("== Security Checkpoint ==".equals(this.name) && "- west".equals(door)) {
          securityCheckPoint = this;
          continue; // Don't allow to go west from here initially
        }
        checkeddirections.put(door.replaceAll("- ", ""), null);
      }
      for (String item : items) {
        this.items.add(item.replaceAll("- ", ""));
      }
    }

    @Override
    public boolean equals(Object o) {
      if (!(o instanceof Room)) {
        return false;
      }

      Room other = (Room)o;
      return name.equals(other.name);
    }

    public String freshDirection() {
      for (Entry<String, Room> e : checkeddirections.entrySet()) {
        if (e.getValue() == null) {
          return e.getKey();
        }
      }
      return "";
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }

    public String hasItem() {
      return items.isEmpty() ? "" : items.iterator().next();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(name.replaceAll("=", ""));
      sb.append(" {");
      sb.append(checkeddirections.values().stream().map(r -> r.name.replaceAll("=", "")).collect(Collectors.joining(",")));
      sb.append("}");
      return sb.toString();
    }
  }

  private static final boolean DEBUG = true;

  /** Learned through experience that these items crash the game. */
  private Set<String> forbidden = Set.of("escape pod", "photons", "infinite loop", "molten lava", "giant electromagnet");
  private Set<String> directions = Set.of("east", "west", "north", "south");
  private Set<Room> world = new HashSet<>();
  private BlockingQueue<Long> in = new LinkedBlockingDeque<>();
  private BlockingQueue<Long> out = new LinkedBlockingDeque<>();
  private Room currentRoom = null;
  private String lastDirection = null;
  private Room securityCheckPoint = null;
  private List<String> myItems = new LinkedList<>();
  private List<String> allItems = new LinkedList<>();

  private Set<Room> discover(String input) {
    Intcode.run(input, Map.of(), in, out);

    do {
      Queue<String> path = new LinkedList<>();

      Room previous = currentRoom;
      currentRoom = parseRoom();
      if (world.contains(currentRoom)) {
        for (Room candidate : world) {
          if (candidate.equals(currentRoom)) {
            currentRoom = candidate;
          }
        }
      } else {
        world.add(currentRoom);
      }
      if (lastDirection != null) {
        previous.checkeddirections.put(lastDirection, currentRoom);
        currentRoom.checkeddirections.put(opposite(lastDirection), previous);
      }

      if (path.isEmpty()) {
        path.addAll(findUndiscoveredRoom());
      }

      String direction = path.poll();
      travelNode(direction);
    } while (!seenAll(world));

    System.out.println("Discovering complete");
    return world;
  }

  @Test
  public void doA() {
    announceResultA();
    long sum = solveA();
    System.out.println(sum);
    assertEquals(1073815584, sum);
  }

  private void doAction(String action, String item) {
    String command = action + " " + item;

    sendCommand(command);

    readLine(); // LF
    readLine(); // You take item
    readLine(); // LF
    readLine(); // Command?
  }

  private void drop(String item) {
    doAction("drop", item);
    myItems.remove(item);
  }

  private List<String> findPathTo(Room to) {
    // Dijkstra... find route to 'to', just by growing from the current room outward
    Map<Room, List<String>> visited = new HashMap<>();
    visited.put(currentRoom, new LinkedList<>());

    Queue<Room> queue = new LinkedList<>();
    queue.add(currentRoom);

    while (!queue.isEmpty()) {
      Room r = queue.poll();
      if (r.equals(to)) {
        return visited.get(to);
      }

      for (Entry<String, Room> e : r.checkeddirections.entrySet()) {
        if (e.getValue() != null && !visited.containsKey(e.getValue())) {
          List<String> road = new LinkedList<>(visited.get(r));
          road.add(e.getKey());
          visited.put(e.getValue(), road);
          queue.offer(e.getValue());
        }
      }
    }

    throw new IllegalStateException("No path from " + currentRoom + " to " + to + " found in " + world);
  }

  private List<String> findUndiscoveredRoom() {
    List<String> result = new LinkedList<>();
    if (!currentRoom.freshDirection().equals("")) { // Easy cast: trivial path
      result.add(currentRoom.freshDirection());
      return result;
    }

    // Find path to other undiscovered room!!
    for (Room candidate : world) {
      if (!candidate.freshDirection().equals("")) {
        result.addAll(findPathTo(candidate));
        result.add(candidate.freshDirection());
        break;
      }
    }

    return result;
  }

  private String opposite(String direction) {
    if ("north".equals(direction)) {
      return "south";
    } else if ("south".equals(direction)) {
      return "north";
    } else if ("west".equals(direction)) {
      return "east";
    }
    return "west";
  }

  private Room parseRoom() {
    readLine();
    readLine();
    readLine();

    String name = readLine();
    String description = readLine();
    readLine();
    readLine(); // Doors here lead:
    List<String> doors = new LinkedList<>();
    String door = "";
    while ((door = readLine()).startsWith("- ")) {
      doors.add(door);
    }
    String s6 = readLine(); // Command? / Items here
    List<String> items = new LinkedList<>();
    if (s6.contains("here:")) {
      String item = "";
      while ((item = readLine()).startsWith("- ")) {
        items.add(item);
      }
      readLine(); // Command?
    }

    return new Room(name, description, doors, items);
  }

  private String readLine() {
    StringBuilder sb = new StringBuilder();
    try {
      long l = 0;
      while ((l = out.poll(100, TimeUnit.MILLISECONDS)) != 10) {
        sb.append((char)l);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    if (DEBUG) {
      System.out.println(sb);
    }

    if (sb.indexOf("typing") != -1) { // Solution detected; extract code
      StringBuilder code = new StringBuilder();
      for (char c : sb.toString().toCharArray()) {
        if (Character.isDigit(c)) {
          code.append(c);
        }
      }
      throw new FoundCodeException(Long.parseLong(code.toString()));
    }

    return sb.toString();
  }

  private boolean seenAll(Set<Room> world) {
    for (Room r : world) {
      if (!r.freshDirection().isEmpty()) {
        return false;
      }
    }

    return true;
  }

  private void sendCommand(String command) {
    if (DEBUG) {
      System.out.println("Doing '" + command + "'");
    }

    for (char c : command.toCharArray()) {
      in.offer((long)c);
    }
    in.offer(10L);
  }

  public long solveA() {
    discover(getInputString());
    Set<Room> roomsWithItems = world.stream() //
        .filter(r -> !r.hasItem().isEmpty()) // with item
        .filter(r -> !forbidden.contains(r.hasItem())) // but not a forbidden one
        .collect(Collectors.toSet());
    for (Room roomWithItem : roomsWithItems) {
      travelNodes(findPathTo(roomWithItem));
      take(roomWithItem.hasItem());
      allItems.add(roomWithItem.hasItem());
    }

    // Go to security Checkpoint
    travelNodes(findPathTo(securityCheckPoint));

    // Try all combinations of items
    int n = allItems.size();
    try {
      for (int i = 0; i < (1 << n); i++) {
        // First, drop all you have
        while (!myItems.isEmpty()) {
          drop(myItems.iterator().next());
        }

        // Take permutation
        for (int j = 0; j < n; j++) {
          if ((i & (1 << j)) > 0) {
            take(allItems.get(j));
          }
        }

        travelNodes(List.of("west")); // Test weight
      }
    } catch (FoundCodeException e) {
      return e.getCode();
    }
    throw new IllegalStateException("No solution found");
  }

  /**
   * Play the game interactively.
   *
   * @param input the IntCode input
   */
  public void solveManual(String input) {
    Scanner scanner = new Scanner(System.in);

    BlockingQueue<Long> in = new LinkedBlockingDeque<>();
    BlockingQueue<Long> out = new LinkedBlockingDeque<>();
    Intcode.run(input, Map.of(), in, out);

    while (true) {
      try {
        Thread.sleep(100L);
      } catch (InterruptedException e) {
      }
      while (!out.isEmpty()) {
        long l = out.poll();
        System.out.print((char)l);
      }

      String command = scanner.nextLine();
      if ("exit".equals(command)) {
        break;
      }
      sendCommand(command);
    }
    scanner.close();
  }

  private void take(String item) {
    doAction("take", item);
    myItems.add(item);
  }

  private void travelNode(String command) {
    if (command == null) {
      return;
    }
    sendCommand(command);

    if (directions.contains(command)) {
      lastDirection = command;
    }
  }

  private void travelNodes(List<String> commands) {
    for (String command : commands) {
      travelNode(command);

      currentRoom = parseRoom();
      for (Room candidate : world) {
        if (candidate.equals(currentRoom)) {
          currentRoom = candidate;
        }
      }
    }
  }

}
