import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Rudresh Patel
 *
 * @version 1.2
 *
 * This JUnits tries to cover all methods and exceptions individually but there are some JUnits which will require you
 * to write the put and remove methods
 *
 * UPDATE v1.2: Added few additional tests inspired from piazza posts and random thoughts
 *
 * NOTE: Base template for these tests was taken from the "QPHashMapStudentTest.java file provided by the TAs.
 *       Some tests maybe similar if not same to the provided student tests by the TAs.
 */
public class RudreshPatelQPHashMapTest {

    private static final int TIMEOUT = 999999999;
    private QuadraticProbingHashMap<Integer, String> map;

    @Before
    public void setUp() {
        map = new QuadraticProbingHashMap<>();

    }

    @Test(timeout = TIMEOUT)
    public void testInitialization() {
        assertEquals(0, map.size());
        assertArrayEquals(new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY], map.getTable());
    }

    /**
     * This test will check the usage of MAX_LOAD_FACTOR.
     */
    @Test(timeout = TIMEOUT)
    public void testResizeLoadFactor0() {
        map.put(1, "A1");
        map.put(2, "B2");
        map.put(3, "C3");
        map.put(4, "D4");
        map.put(5, "E5");
        map.put(6, "F6");
        map.put(7, "G7");
        map.put(8, "H8");

        assertEquals(8, map.size()); // 8 elements / array.length < 0.67
        assertEquals(13, map.getTable().length);

        map.put(9, "I9");

        assertEquals(9, map.size()); // 8 elements / array.length < 0.67 --|-- 9 elements / array.length > 0.67.
        assertEquals(27, map.getTable().length);

    }

    @Test(timeout = TIMEOUT)
    public void testResizeLoadFactor1() {
        map.put(1, "A1");
        map.put(2, "B2");
        map.put(3, "C3");
        map.put(4, "D4");
        map.put(5, "E5");
        map.put(6, "F6");
        map.put(7, "G7");
        map.put(8, "H8");

        assertEquals(8, map.size()); // 8 elements / array.length < 0.67
        assertEquals(13, map.getTable().length);

        map.put(8, "H8"); // Here, even if the element is not added, the implementation would result into resize.

        assertEquals(8, map.size()); // 8 + 1 (attempt to add) elements / array.length > 0.67.
        assertEquals(27, map.getTable().length);
    }

    /**
     * The following tests will check for put method.
     */

    @Test(timeout = TIMEOUT)
    public void putTestSimple1() {
        assertNull(map.put(1, "A1"));
        assertNull(map.put(2, "B2"));
        assertNull(map.put(3, "C3"));
        assertNull(map.put(4, "D4"));
        assertNull(map.put(5, "E5"));
        assertEquals(5, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putReSizeTest2() {
        assertNull(map.put(1, "A1"));
        assertNull(map.put(2, "B2"));
        assertNull(map.put(3, "C3"));
        assertNull(map.put(4, "D4"));
        assertNull(map.put(5, "E5"));
        assertNull(map.put(6, "F6"));
        assertNull(map.put(7, "G7"));
        assertNull(map.put(8, "H8"));
        assertNull(map.put(9, "I9"));

        // we will trip max load factor resize
        assertEquals(9, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry
                [QuadraticProbingHashMap.INITIAL_CAPACITY * 2 + 1];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");
        expected[8] = new QuadraticProbingMapEntry<>(8, "H8");
        expected[9] = new QuadraticProbingMapEntry<>(9, "I9");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putWrapAroundTest3() {
        assertNull(map.put(1, "A1"));
        assertNull(map.put(2, "B2"));
        assertNull(map.put(3, "C3"));
        assertNull(map.put(4, "D4"));
        assertNull(map.put(5, "E5"));
        assertNull(map.put(6, "F6"));
        assertNull(map.put(13, "13Wrap Around At Index 0"));
        assertNull(map.put(20, "20Wrap Around At Index 7"));

        // we will test Wrap Around
        assertEquals(8, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(13, "13Wrap Around At Index 0");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(20, "20Wrap Around At Index 7");
        assertArrayEquals(expected, map.getTable());
    }


    @Test(timeout = TIMEOUT)
    public void putWrapAroundTest4() {
        assertNull(map.put(1, "A1"));
        assertNull(map.put(2, "B2"));
        assertNull(map.put(3, "C3"));
        assertNull(map.put(4, "D4"));
        assertNull(map.put(5, "E5"));
        assertNull(map.put(6, "F6"));
        assertNull(map.put(20, "20Wrap Around At Index 7 -> after resize -> it will be 20"));
        assertNull(map.put(21, "21Wrap Around At Index 8 -> after resize -> it will be 21"));
        assertNull(map.put(36, "36Wrap Around At Index 9"));

        // we will test Wrap Around at resize
        assertEquals(9, map.size());
        QuadraticProbingMapEntry[] expected =
                new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY * 2 + 1];
        expected[0] = null;
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = null;
        expected[8] = null;
        expected[20] = new QuadraticProbingMapEntry<>(20, "20Wrap Around At Index 7 -> after resize -> it will be 20");
        expected[21] = new QuadraticProbingMapEntry<>(21, "21Wrap Around At Index 8 -> after resize -> it will be 21");
        expected[9] = new QuadraticProbingMapEntry<>(36, "36Wrap Around At Index 9");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putDuplicateTest5() {
        String i = "Grrr";
        assertNull(map.put(1, "A1"));
        assertNull(map.put(2, "B2"));
        assertNull(map.put(3, "C3"));
        assertNull(map.put(4, "D4"));
        assertNull(map.put(5, "E5"));
        assertNull(map.put(6, "F6"));
        assertNull(map.put(7, i));
        assertSame(i, map.put(7, "G7"));

        // Here, we checked if the old value in the duplicate key is returned or not.
        assertEquals(7, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");

        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putDuplicateResizeTest6() {
        String i = "Grrr7";
        assertNull(map.put(1, "A1"));
        assertNull(map.put(2, "B2"));
        assertNull(map.put(3, "C3"));
        assertNull(map.put(4, "D4"));
        assertNull(map.put(5, "E5"));
        assertNull(map.put(6, "F6"));
        assertNull(map.put(7, i));
        assertNull(map.put(8, "H8"));
        assertSame(i, map.put(7, "G7"));

        // Here, we checked if the old value in the duplicate key is returned or not while resizing.
        // Moreover, the size will double because we attempted to add the ninth element (duplicate key element)
        // which makes it greater than the load factor.

        assertEquals(8, map.size());
        QuadraticProbingMapEntry[] expected =
                new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY * 2 + 1];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");
        expected[8] = new QuadraticProbingMapEntry<>(8, "H8");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putCollisionTest7() {
        assertNull(map.put(2, "A2"));   // [null, null, A2, null, null, null, null, null, null, null, null, null, null]
        assertNull(map.put(15, "B15")); // [null, null, A2, B15, null, null, null, null, null, null, null, null, null]
        assertNull(map.put(28, "C28")); // [null, null, A2, B15, null, null, C28, null, null, null, null, null, null]
        assertNull(map.put(11, "D11")); // [null, null, A2, B15, null, null, C28, null, null, null, null, D11, null]
        assertNull(map.put(41, "E41")); // [null, null, A2, B15, null, E41, C28, null, null, null, null, D11, null]


        assertEquals(5, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];

        expected[2] = new QuadraticProbingMapEntry<>(2, "A2");
        expected[3] = new QuadraticProbingMapEntry<>(15, "B15");
        expected[5] = new QuadraticProbingMapEntry<>(41, "E41");
        expected[6] = new QuadraticProbingMapEntry<>(28, "C28");
        expected[11] = new QuadraticProbingMapEntry<>(11, "D11");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putResizeCollisionTest8() {
        //Here, We will trigger load factor
        assertNull(map.put(2, "A2"));   // [null, null, A2, null, null, null, null, null, null, null, null, null, null]
        assertNull(map.put(15, "B15")); // [null, null, A2, B15, null, null, null, null, null, null, null, null, null]
        assertNull(map.put(28, "C28")); // [null, null, A2, B15, null, null, C28, null, null, null, null, null, null]
        assertNull(map.put(11, "D11")); // [null, null, A2, B15, null, null, C28, null, null, null, null, D11, null]
        assertNull(map.put(41, "E41")); // [null, null, A2, B15, null, E41, C28, null, null, null, null, D11, null]
        assertNull(map.put(43, "F43")); // [null, null, A2, B15, F43, E41, C28, null, null, null, null, D11, null]
        assertNull(map.put(44, "G44")); 
        // [0-null, 1-null, 2-A2, 3-B15, 4-F43, 5-E41, 6-C28, 7-null, 8-null, 9-G44, null, 11-D11, null]
        assertNull(map.put(13, "H13")); 
        // [0-H13, 1-null, 2-A2, 3-B15, 4-F43, 5-E41, 6-C28, 7-null, 8-null, 9-F44, null, 11-D11, null]
        // Now, resize will trigger. The array representation will use "value@index" notation in comment
        assertNull(map.put(27, "I27")); 
        // [I27 @ 00, C28 @ 01, A2 @ 02, D11 @ 11, H13 @ 13, E41 @ 14, B15 @ 15, F43 @ 16, G44 @ 17]
        /*
        Full Array:-
        [(27, I27), (28, C28), (2, A2), null, null, null, null, null, null, null, null, (11, D11), null, (13, H13),
        (41, E41), (15, B15), (43, F43), (44, G44), null, null, null, null, null, null, null, null, null]
         */
        // System.out.println(Arrays.toString(map.getTable()));

        assertEquals(9, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry
                [QuadraticProbingHashMap.INITIAL_CAPACITY * 2 + 1];

        expected[0] = new QuadraticProbingMapEntry<>(27, "I27");
        expected[1] = new QuadraticProbingMapEntry<>(28, "C28");
        expected[2] = new QuadraticProbingMapEntry<>(2, "A2");
        expected[11] = new QuadraticProbingMapEntry<>(11, "D11");
        expected[13] = new QuadraticProbingMapEntry<>(13, "H13");
        expected[14] = new QuadraticProbingMapEntry<>(41, "E41");
        expected[15] = new QuadraticProbingMapEntry<>(15, "B15");
        expected[16] = new QuadraticProbingMapEntry<>(43, "F43");
        expected[17] = new QuadraticProbingMapEntry<>(44, "G44");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putResizeCollisionTest9() {
        // Here, this test will trigger the resize when the probing count is equal to the length of the array
        // We will add all keys such that the mod will be 1.

        assertNull(map.put(1, "A1")); // 1 % 13 = 1
        assertNull(map.put(14, "B14")); // 14 % 13 = 1
        assertNull(map.put(27, "C27"));
        assertNull(map.put(40, "D40"));
        assertNull(map.put(53, "E53"));
        assertNull(map.put(66, "F66"));
        assertNull(map.put(79, "G79"));
        assertNull(map.put(92, "H92")); // 92 % 13 = 1.
        // This will eventually trigger resize even though we have less than load factor.
        // [C27 @ 00, A1 @ 01, H92 @ 11, F66 @ 12, D40 @ 13, B14 @ 14, G79 @ 25, E53 @ 26]
        //System.out.println(Arrays.toString(map.getTable()));

        /*
        [(27, C27), (1, A1), null, null, null, null, null, null, null, null, null, (92, H92), (66, F66),
        (40, D40), (14, B14), null, null, null, null, null, null, null, null, null, null, (79, G79), (53, E53)]
         */

        assertEquals(8, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry
                [QuadraticProbingHashMap.INITIAL_CAPACITY * 2 + 1];
        expected[0] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[11] = new QuadraticProbingMapEntry<>(92, "H92");
        expected[12] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[13] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[14] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[25] = new QuadraticProbingMapEntry<>(79, "G79");
        expected[26] = new QuadraticProbingMapEntry<>(53, "E53");
        assertArrayEquals(expected, map.getTable());
    }


    @Test(timeout = TIMEOUT)
    public void putEntryOnIsRemovedTest10() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        assertSame(temp5, map.remove(5));
        assertEquals(7, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[5].setRemoved(true);
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");
        expected[8] = new QuadraticProbingMapEntry<>(8, "H8");

        assertArrayEquals(expected, map.getTable());

        assertNull(map.put(5, "EE5"));

        expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "EE5");
        expected[5].setRemoved(false);
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");
        expected[8] = new QuadraticProbingMapEntry<>(8, "H8");

        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void removeSingleElementTest0() {
        String temp = "A";

        assertNull(map.put(1, temp));
        assertEquals(1, map.size());

        assertSame(temp, map.remove(1));
        assertEquals(0, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A");
        expected[1].setRemoved(true);
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void removeMultipleElementsTest1() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        assertSame(temp5, map.remove(5));
        assertSame(temp7, map.remove(7));
        assertSame(temp8, map.remove(8));
        assertEquals(5, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[5].setRemoved(true);
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");
        expected[7].setRemoved(true);
        expected[8] = new QuadraticProbingMapEntry<>(8, "H8");
        expected[8].setRemoved(true);
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void removeProbeTest2() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.remove(14));
        assertSame(temp5, map.remove(53));
        assertEquals(5, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[2].setRemoved(true);
        expected[3] = null;
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[4].setRemoved(true);
        expected[5] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[6] = null;
        expected[7] = null;
        expected[8] = null;
        expected[9] = null;
        expected[10] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[11] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void removeProbeIsRemovedVariationTest3() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.remove(14));
        assertEquals(6, map.size());

        try {
            map.remove(14);
        } catch (NoSuchElementException e) {
            System.out.println("Expected exception occurred: worked as expected by this JUnit. For reference,"
                    + " look at removeProbeIsRemovedVariationTest3 Junit");
        }
        // Here, we will check if the probe jumps the DEL marker as expected
        assertSame(temp5, map.remove(53));
        assertEquals(5, map.size());

        // This specifically checks that keys and values are not null. (Assert Arrays also checks that)
        assertNotNull(map.getTable()[2].getKey());
        assertNotNull(map.getTable()[4].getKey());

        assertNotNull(map.getTable()[2].getValue());
        assertNotNull(map.getTable()[4].getValue());


        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[2].setRemoved(true);
        expected[3] = null;
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[4].setRemoved(true);
        expected[5] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[6] = null;
        expected[7] = null;
        expected[8] = null;
        expected[9] = null;
        expected[10] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[11] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }


    @Test(timeout = TIMEOUT)
    public void getSingleElementTest0() {
        String temp = "A";

        assertNull(map.put(1, temp));
        assertEquals(1, map.size());

        assertSame(temp, map.get(1));
        assertEquals(1, map.size());
    }


    @Test(timeout = TIMEOUT)
    public void getMultipleElementsTest1() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        assertSame(temp5, map.remove(5));

        try {
            map.get(5);
        } catch (NoSuchElementException e) {
            System.out.println("This exception error is expected: This JUnit is working as expected. For reference,"
                    + "look at getMultipleElementsTest1 Junit");
        }

        assertSame(temp7, map.get(7));
        assertSame(temp8, map.get(8));
        assertEquals(7, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[5].setRemoved(true);
        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");
        expected[8] = new QuadraticProbingMapEntry<>(8, "H8");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void getProbeTest2() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.get(14));
        assertSame(temp2, map.remove(14));
        assertEquals(6, map.size());
        try {
            map.get(14);
        } catch (NoSuchElementException e) {
            System.out.println("This exception error is expected: This JUnit is working as expected. For reference,"
                    + "look at getProbeTest2 Junit");
        }

        assertSame(temp5, map.get(53));
        assertSame(temp5, map.remove(53));
        assertEquals(5, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[2].setRemoved(true);
        expected[3] = null;
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[4].setRemoved(true);
        expected[5] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[6] = null;
        expected[7] = null;
        expected[8] = null;
        expected[9] = null;
        expected[10] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[11] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void getProbeTest3() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));

        assertSame(temp1, map.get(1));
        assertSame(temp2, map.get(14));
        assertSame(temp3, map.get(27));
        assertSame(temp4, map.get(40));
        assertSame(temp5, map.get(53));
        assertSame(temp6, map.get(66));
        assertSame(temp7, map.get(79));
    }


    @Test(timeout = TIMEOUT)
    public void getProbeIsRemovedVariationTest4() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.get(14));
        assertSame(temp2, map.remove(14));
        assertEquals(6, map.size());

        try {
            map.get(14);
        } catch (NoSuchElementException e) {
            System.out.println("Expected exception occurred: worked as expected by this JUnit. For reference,"
                    + " look at getProbeIsRemovedVariationTest3 Junit");
        }
        // Here, we will check if the probe jumps the DEL marker as expected
        assertSame(temp5, map.get(53));
        assertSame(temp5, map.remove(53));
        assertEquals(5, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[2].setRemoved(true);
        expected[3] = null;
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[4].setRemoved(true);
        expected[5] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[6] = null;
        expected[7] = null;
        expected[8] = null;
        expected[9] = null;
        expected[10] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[11] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void containsProbeTest2() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());


        assertTrue(map.containsKey(14));
        assertSame(temp2, map.remove(14));
        assertFalse(map.containsKey(14));

        assertEquals(6, map.size());


        assertTrue(map.containsKey(53));
        assertSame(temp5, map.remove(53));
        assertFalse(map.containsKey(53));
        assertEquals(5, map.size());

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[2].setRemoved(true);
        expected[3] = null;
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[4].setRemoved(true);
        expected[5] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[6] = null;
        expected[7] = null;
        expected[8] = null;
        expected[9] = null;
        expected[10] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[11] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void containsSingleElementTest0() {
        String temp = "A";

        assertNull(map.put(1, temp));
        assertEquals(1, map.size());

        assertTrue(map.containsKey(1));

        assertSame(temp, map.remove(1));
        assertFalse(map.containsKey(1));

        assertFalse(map.containsKey(100));
    }


    @Test(timeout = TIMEOUT)
    public void containsMultipleElementsTest1() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        assertFalse(map.containsKey(100));

        assertTrue(map.containsKey(5));
        assertSame(temp5, map.remove(5));
        assertFalse(map.containsKey(5));
        assertEquals(7, map.size());

        assertTrue(map.containsKey(1));
        assertSame(temp1, map.remove(1));
        assertFalse(map.containsKey(1));
        assertEquals(6, map.size());

        assertTrue(map.containsKey(8));
        assertSame(temp8, map.remove(8));
        assertFalse(map.containsKey(8));
        assertEquals(5, map.size());

        assertTrue(map.containsKey(3));
        assertSame(temp3, map.remove(3));
        assertFalse(map.containsKey(3));
        assertEquals(4, map.size());

        assertTrue(map.containsKey(2));
        assertSame(temp2, map.remove(2));
        assertFalse(map.containsKey(2));
        assertEquals(3, map.size());

        assertTrue(map.containsKey(4));
        assertSame(temp4, map.remove(4));
        assertFalse(map.containsKey(4));
        assertEquals(2, map.size());

        assertTrue(map.containsKey(6));
        assertSame(temp6, map.remove(6));
        assertFalse(map.containsKey(6));
        assertEquals(1, map.size());

        assertTrue(map.containsKey(7));
        assertSame(temp7, map.remove(7));
        assertFalse(map.containsKey(7));
        assertEquals(0, map.size());

        assertFalse(map.containsKey(100));

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[1].setRemoved(true);

        expected[2] = new QuadraticProbingMapEntry<>(2, "B2");
        expected[2].setRemoved(true);

        expected[3] = new QuadraticProbingMapEntry<>(3, "C3");
        expected[3].setRemoved(true);

        expected[4] = new QuadraticProbingMapEntry<>(4, "D4");
        expected[4].setRemoved(true);

        expected[5] = new QuadraticProbingMapEntry<>(5, "E5");
        expected[5].setRemoved(true);

        expected[6] = new QuadraticProbingMapEntry<>(6, "F6");
        expected[6].setRemoved(true);

        expected[7] = new QuadraticProbingMapEntry<>(7, "G7");
        expected[7].setRemoved(true);

        expected[8] = new QuadraticProbingMapEntry<>(8, "H8");
        expected[8].setRemoved(true);

        assertArrayEquals(expected, map.getTable());
    }


    @Test(timeout = TIMEOUT)
    public void containsProbeIsRemovedVariationTest4() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertFalse(map.containsKey(100));

        assertTrue(map.containsKey(14));
        assertSame(temp2, map.remove(14));
        assertFalse(map.containsKey(14));

        assertEquals(6, map.size());


        // Here, we will check if the probe jumps the DEL marker as expected
        assertTrue(map.containsKey(53));
        assertSame(temp5, map.remove(53));
        assertFalse(map.containsKey(53));
        assertEquals(5, map.size());

        assertFalse(map.containsKey(100));

        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[2].setRemoved(true);
        expected[3] = null;
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[4].setRemoved(true);
        expected[5] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[6] = null;
        expected[7] = null;
        expected[8] = null;
        expected[9] = null;
        expected[10] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[11] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void keySetTest1() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        Set<Integer> expected = new HashSet<>();
        expected.add(1);
        expected.add(2);
        expected.add(3);
        expected.add(4);
        expected.add(5);
        expected.add(6);
        expected.add(7);
        expected.add(8);

        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void keySetTest2() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));


        Set<Integer> expected = new HashSet<>();
        expected.add(66);
        expected.add(1);
        expected.add(14);
        expected.add(79);
        expected.add(27);
        expected.add(40);
        expected.add(53);

        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void keySetTest3() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));

        assertSame(temp6, map.remove(66));
        assertEquals(6, map.size());

        Set<Integer> expected = new HashSet<>();
        expected.add(1);
        expected.add(14);
        expected.add(79);
        expected.add(27);
        expected.add(40);
        expected.add(53);

        assertEquals(expected, map.keySet());
    }

    @Test(timeout = TIMEOUT)
    public void valuesTest1() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        List<String> expected = new LinkedList<>();
        expected.add("A1");
        expected.add("B2");
        expected.add("C3");
        expected.add("D4");
        expected.add("E5");
        expected.add("F6");
        expected.add("G7");
        expected.add("H8");

        assertEquals(expected, map.values());
    }

    @Test(timeout = TIMEOUT)
    public void valuesTest2() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        assertSame(temp7, map.remove(7));
        assertSame(temp2, map.remove(2));

        assertEquals(6, map.size());

        List<String> expected = new LinkedList<>();
        expected.add("A1");
        expected.add("C3");
        expected.add("D4");
        expected.add("E5");
        expected.add("F6");
        expected.add("H8");
        assertEquals(expected, map.values());
    }

    @Test(timeout = TIMEOUT)
    public void valuesTest3() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));

        List<String> expected = new LinkedList<>();
        expected.add("F66");
        expected.add("A1");
        expected.add("B14");
        expected.add("E53");
        expected.add("C27");
        expected.add("D40");
        expected.add("G79");

        assertEquals(expected, map.values());

        assertSame(temp6, map.remove(66));
        assertEquals(6, map.size());

        expected = new LinkedList<>();
        expected.add("A1");
        expected.add("B14");
        expected.add("E53");
        expected.add("C27");
        expected.add("D40");
        expected.add("G79");
        assertEquals(expected, map.values());
    }

    @Test(timeout = TIMEOUT)
    public void resizeTest1() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));

        map.resizeBackingTable(8);
        assertEquals(7, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[8];
        expected[0] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[2] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[3] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[4] = null;
        expected[5] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[6] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[7] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void resizeTest2() {
        // Same length and size
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));

        map.resizeBackingTable(7);
        assertEquals(7, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[7];
        expected[5] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[1] = new QuadraticProbingMapEntry<>(1, "A1");
        expected[3] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[6] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[0] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[2] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void testClear() {
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));

        assertEquals(7, map.size());
        map.clear();
        assertEquals(0, map.size());
        assertArrayEquals(new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY], map.getTable());
    }

    // ------------------------|||||||||||||||||||||||| ----------------------------- ||||||||||||||||
    // ----------|||||||||||-----------Exceptions -----------|||||||||||------------- ||||||||||||||||
    // ------------------------|||||||||||||||||||||||| ----------------------------- ||||||||||||||||

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void putKeyNullTest1() {
        map.put(null, "String");
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void putValueNullTest2() {
        map.put(0, null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void putKeyAndValueNullTest() {
        map.put(null, null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void removeKeyNullTest() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        map.remove(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void removeNotFoundTest() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        map.remove(10);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void getKeyNullTest() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        map.get(null);
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void getNotFoundTest() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        map.get(10);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void containsKeyNullTest() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());

        map.containsKey(null);
    }

    @Test(timeout = TIMEOUT, expected = IllegalArgumentException.class)
    public void resizeLengthLesssThanSizeTest() {
        String temp1 = "A1";
        String temp2 = "B2";
        String temp3 = "C3";
        String temp4 = "D4";
        String temp5 = "E5";
        String temp6 = "F6";
        String temp7 = "G7";
        String temp8 = "H8";

        assertNull(map.put(1, temp1));
        assertNull(map.put(2, temp2));
        assertNull(map.put(3, temp3));
        assertNull(map.put(4, temp4));
        assertNull(map.put(5, temp5));
        assertNull(map.put(6, temp6));
        assertNull(map.put(7, temp7));
        assertNull(map.put(8, temp8));
        assertEquals(8, map.size());
        map.resizeBackingTable(7);
    }

    // ----------------------- These tests are inspired by Piazza posts and random thoughts ------------------------
    @Test
    public void multiTest() {
        // This test is inspired from piazza post
        assertNull(map.put(0, "0"));
        assertNull(map.put(13, "13"));

        map.remove(0);
        map.remove(13);

        assertNull(map.put(13, "13"));

        assertEquals(1, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[
                QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(13, "13");
        expected[1] = new QuadraticProbingMapEntry<>(13, "13");
        expected[1].setRemoved(true);
        assertArrayEquals(expected, map.getTable());
    }

    @Test
    public void multiTest2() {
        // This test is inspired from piazza post
        assertNull(map.put(0, "0"));
        assertNull(map.put(13, "13"));

        map.remove(0);
        map.remove(13);

        assertNull(map.put(26, "A"));

        assertEquals(1, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(26, "A");
        expected[1] = new QuadraticProbingMapEntry<>(13, "13");
        expected[1].setRemoved(true);
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void elementDNERemoveTest1() {
        // This exception is for an element that does not exist (or is removed) which will throw NoSuchElementexception
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.remove(14));
        assertEquals(6, map.size());
        map.remove(14);

        map.clear();
        // Restart the map
        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.remove(14));
        assertEquals(6, map.size());
        map.remove(19);

    }

    @Test
    public void multiTest3() {
        // this is inspired from a piazza post
        String str = "C39";
        assertNull(map.put(13, "A13"));
        assertNull(map.put(26, "B26"));
        assertNull(map.put(39, str));

        map.remove(13);
        map.remove(26);


        assertSame(null, map.put(26, "C39 Different"));

        assertEquals(2, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(26, "C39 Different");
        expected[1] = new QuadraticProbingMapEntry<>(26, "B26");
        expected[1].setRemoved(true);
        expected[4] = new QuadraticProbingMapEntry<>(39, "C39");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT)
    public void putResizeCollisionTestDuplicateTest() {
        /*
         This test checks that resize is not called when an element comes across a removed space
         and probe is called array.length times because then the element is supposed to be stored in the removed space.
         (all other limitations such as duplicates apply)
         */
        assertNull(map.put(1, "A1")); // 1 % 13 = 1
        assertNull(map.put(14, "B14")); // 14 % 13 = 1
        assertNull(map.put(27, "C27"));
        assertNull(map.put(40, "D40"));
        assertNull(map.put(53, "E53"));
        assertNull(map.put(66, "F66"));
        assertNull(map.put(79, "G79"));
        System.out.println(Arrays.toString(map.getTable()));
        /*
        [(66, F66), (1, A1), (14, B14), null, (53, E53), (27, C27), null, null, null, null, (40, D40), (79, G79), null]
        */

        map.remove(1);
        assertNull(map.put(92, "H92")); // 92 % 13 = 1.

        assertEquals(7, map.size());
        QuadraticProbingMapEntry[] expected = new QuadraticProbingMapEntry[QuadraticProbingHashMap.INITIAL_CAPACITY];
        expected[0] = new QuadraticProbingMapEntry<>(66, "F66");
        expected[1] = new QuadraticProbingMapEntry<>(92, "H92");
        expected[2] = new QuadraticProbingMapEntry<>(14, "B14");
        expected[4] = new QuadraticProbingMapEntry<>(53, "E53");
        expected[5] = new QuadraticProbingMapEntry<>(27, "C27");
        expected[10] = new QuadraticProbingMapEntry<>(40, "D40");
        expected[11] = new QuadraticProbingMapEntry<>(79, "G79");
        assertArrayEquals(expected, map.getTable());
    }

    @Test(timeout = TIMEOUT, expected = NoSuchElementException.class)
    public void elementDNEGetTest1() {
        // This exception is for an element that does not exist (or is removed) which will throw NoSuchElementexception
        String temp1 = "A1";
        String temp2 = "B14";
        String temp3 = "C27";
        String temp4 = "D40";
        String temp5 = "E53";
        String temp6 = "F66";
        String temp7 = "G79";

        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.remove(14));
        assertEquals(6, map.size());
        map.get(14);

        map.clear();
        // Restart the map
        assertNull(map.put(1, temp1));
        assertNull(map.put(14, temp2));
        assertNull(map.put(27, temp3));
        assertNull(map.put(40, temp4));
        assertNull(map.put(53, temp5));
        assertNull(map.put(66, temp6));
        assertNull(map.put(79, temp7));
        assertEquals(7, map.size());

        assertSame(temp2, map.remove(14));
        assertEquals(6, map.size());
        map.get(19);

    }
}