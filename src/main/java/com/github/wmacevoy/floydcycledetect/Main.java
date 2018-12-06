/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.wmacevoy.floydcycledetect;

import java.util.Arrays;

/**
 *
 * @author warren
 */
public class Main {
    int sequential, cycle;
    
    int begin() {
        return -sequential;
    }
    
    int end() {
        return cycle > 0 ? Integer.MAX_VALUE : 0;
    }
    
    int next(int i) { 
        if (i < 0) { return i + 1; }
        return cycle > 0 ? (i + 1) % cycle : 0;
    }

    int v(int d) {
        return d < sequential ? d - sequential : (d - sequential) % cycle;
    }

    // returns length before cycle & cycle length (0 for no cycle) and
    // phase 1 iteration count (phase 2 = N), (phase 3 = C).
    int[] floyd1() {
        int t = 0;
        int fast = begin();
        int slow = begin();
        if (fast == end()) return new int[] { 0, 0, 0 };
        for (;;) {
            t += 1;
            fast = next(fast);
            if (fast == end()) return new int[] { 2*t-1, 0, t };
            fast = next(fast);
            if (fast == end()) return new int[] { 2*t, 0, t };
            slow = next(slow);
            if (fast == slow) {
                break;
            }
        }
        
        int N = 0;
        slow = begin();
        for (;;) {
            if (fast == slow) break;
            fast = next(fast);
            slow = next(slow);
            N += 1;
        }
        
        int C = 0;
        for (;;) {
            fast = next(fast);
            C += 1;
            if (fast == slow) break;
        }
        return new int[] { N, C, t};
    }

    int floyd2() {
        if (cycle == 0) return -sequential;
        
        if (sequential == 0) {
            return cycle;
        }
        int m = sequential % cycle;
        return m == 0 ? sequential : sequential + cycle - m;
    }

    void test() {
        for (sequential = 0; sequential < 100; ++sequential) {
            for (cycle = 0; cycle < 100; ++cycle) {
                int[] nct1 = floyd1();
                int n1 = nct1[0];
                int c1 = nct1[1];
                int t1 = nct1[2];
                int t2 = floyd2();
                int[] nct2 = new int[] { sequential, cycle, t2 };
                
                if (n1 != sequential || c1 != cycle || (cycle > 0 && t1 != t2)) {
                    throw new IllegalStateException("n=" + sequential + "c=" + cycle + " f1=" + Arrays.toString(nct1) + " f2=" + Arrays.toString(nct2));
                }
            }
        }

    }

    public static void main(String[] args) {
        new Main().test();
    }
}
