package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UISsrBoardTest {

    @Test
    public void test_base() {
        UISsrBoard board = new UISsrBoard(null);
        board.slot0(self -> new SsrBlock("slot0"));
        board.slot1(self -> new SsrBlock("slot1"));
        assertEquals("slot0slot1", board.toString());
    }

    @Test
    public void test_cpu1() {
        UISsrBoard board = new UISsrBoard(null);
        board.cpu(new SsrBlock("<div>${callback(slot0)}</div>, <span>${callback(slot2)}</span>"));
        board.slot0(self -> new SsrBlock("slot0"));
        board.slot1(self -> new SsrBlock("slot2"));
        assertEquals("<div>slot0</div>, <span></span>", board.toString());
    }

    @Test
    public void test_cpu2() {
        UISsrBoard board = new UISsrBoard(null);
        board.cpu(new SsrBlock("<div>${callback(slot0)}</div>, <span>${callback(slot2)}</span>").toMap("level_", "1"));
        board.slot0(self -> new SsrBlock("slot0"));
        board.slot2(self -> new SsrBlock("slot2"));
        assertEquals("<div>slot0</div>, <span>slot2</span>", board.toString());
    }

}
