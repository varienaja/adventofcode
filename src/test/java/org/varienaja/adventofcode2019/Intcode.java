package org.varienaja.adventofcode2019;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Intcode {
  private static final int MAX_MEMORY = 16384;
  private static ScheduledExecutorService service = new ScheduledThreadPoolExecutor(55);
  private static AtomicBoolean waiting = new AtomicBoolean(false);

  public static boolean isWaitingForInput() {
    return waiting.get();
  }

  public static long run(String input, Map<Integer, Long> initialValues) {
    try {
      return run(input, initialValues, new ArrayBlockingQueue<Long>(1), new ArrayBlockingQueue<Long>(1)).get();
    } catch (Exception e) {
      e.printStackTrace();
      return -1;
    }
  }

  public static Future<Long> run(String input, Map<Integer, Long> initialValues, BlockingQueue<Long> in, BlockingQueue<Long> out) {
    long[] opCode2ParamCount = new long[] {
        // 1, 2, 3, 4, 5, 6, 7, 8, 9
        0, 3, 3, 1, 1, 2, 2, 3, 3, 1
    };

    return service.schedule(new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        int ip = 0;
        int rb = 0;
        long[] program = new long[MAX_MEMORY];
        Arrays.fill(program, 0);
        long[] code = Arrays.stream(input.split(",")).mapToLong(Long::parseLong).toArray();
        System.arraycopy(code, 0, program, 0, code.length);

        initialValues.entrySet().forEach(e -> program[e.getKey()] = e.getValue());
        while (true) {
          long instr = program[ip];

          // mode param1 (2 = relative mode, 1 =immediate mode, 0==position mode)
          int a = (instr - 20000 > 0) ? 2 : (instr - 10000) > 0 ? 1 : 0;
          instr = instr - a * 10000;

          int b = (instr - 2000 > 0) ? 2 : (instr - 1000) > 0 ? 1 : 0;
          instr = instr - b * 1000;

          int c = (instr - 200 > 0) ? 2 : (instr - 100) > 0 ? 1 : 0;
          int opcode = (int)(instr - c * 100);

          long aa = 0;
          long bb = 0;
          long cc = 0;
          long paramCount = opcode == 99 ? 0 : opCode2ParamCount[opcode];

          if (paramCount >= 1) {
            cc = c == 0 ? program[(int)program[ip + 1]] : c == 1 ? program[ip + 1] : program[rb + (int)program[ip + 1]];
          }
          if (paramCount >= 2) {
            bb = b == 0 ? program[(int)program[ip + 2]] : b == 1 ? program[ip + 2] : program[rb + (int)program[ip + 2]];
          }
          if (paramCount >= 3) {
            aa = a == 0 ? program[(int)program[ip + 3]] : a == 1 ? program[ip + 3] : program[rb + (int)program[ip + 3]];
          }
          paramCount++;

          switch (opcode) {
            case 1: // add
              aa = a == 1 ? ip + 3 : a == 0 ? program[ip + 3] : rb + (int)program[ip + 3];
              program[(int)aa] = cc + bb;
              break;
            case 2: // mul
              aa = a == 1 ? ip + 3 : a == 0 ? program[ip + 3] : rb + (int)program[ip + 3];
              program[(int)aa] = bb * cc;
              break;
            case 3: // input
              cc = c == 1 ? ip + 1 : c == 0 ? program[ip + 1] : rb + (int)program[ip + 1];
              waiting.set(true);
              program[(int)cc] = in.take();
              waiting.set(false);
              break;
            case 4: // output
              out.put(cc);
              break;
            case 5: // jump-if-true
              if (cc != 0) {
                ip = (int)bb;
                continue;
              }
              break;
            case 6: // jump-if-false
              if (cc == 0) {
                ip = (int)bb;
                continue;
              }
              break;
            case 7: // less than
              int lt = cc < bb ? 1 : 0;
              if (a == 1) {
                program[ip + 3] = lt;
              } else if (a == 0) {
                program[(int)program[ip + 3]] = lt;
              } else {
                program[rb + (int)program[ip + 3]] = lt;
              }
              break;
            case 8: // equals
              int eq = cc == bb ? 1 : 0;
              if (a == 1) {
                program[ip + 3] = eq;
              } else if (a == 0) {
                program[(int)program[ip + 3]] = eq;
              } else {
                program[rb + (int)program[ip + 3]] = eq;
              }
              break;
            case 9: // set relative base
              rb += cc;
              break;
            case 99:
              return program[0];
            default:
              throw new IllegalStateException("Unknown opcode " + opcode);
          }
          ip += paramCount;
        }
      }
    }, 0, TimeUnit.MILLISECONDS);

  }

}
