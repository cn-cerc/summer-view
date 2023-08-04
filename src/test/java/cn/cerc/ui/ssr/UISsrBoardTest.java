package cn.cerc.ui.ssr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UISsrBoardTest {

    public class SsrBlockTest implements SupplierCustomBlockImpl {
        private SsrBlock block;

        public SsrBlockTest(String text) {
            this.block = new SsrBlock(text);
        }

        @Override
        public SsrBlockImpl block() {
            return block;
        }

        @Override
        public SsrBlockImpl request(SsrComponentImpl owner) {
            return block;
        }

    }

    @Test
    public void test_base() {
        UISsrBoard board = new UISsrBoard(null);
        board.slot1(new SsrBlockTest("slot0"));
        board.slot1(new SsrBlockTest("slot1"));
        assertEquals("slot0slot1", board.toString());
    }

    @Test
    public void test_cpu1() {
        UISsrBoard board = new UISsrBoard(null);
        board.cpu(new SsrBlock("<div>${callback(slot0)}</div>, <span>${callback(slot2)}</span>"));
        board.slot0(new SsrBlockTest("slot0"));
        board.slot1(new SsrBlockTest("slot2"));
        assertEquals("<div>slot0</div>, <span></span>", board.toString());
    }

    @Test
    public void test_cpu2() {
        UISsrBoard board = new UISsrBoard(null);
        board.cpu(new SsrBlock("<div>${callback(slot0)}</div>, <span>${callback(slot2)}</span>").toMap("level_", "1"));
        board.slot0(new SsrBlockTest("slot0"));
        board.slot2(new SsrBlockTest("slot2"));
        assertEquals("<div>slot0</div>, <span>slot2</span>", board.toString());
    }

}
