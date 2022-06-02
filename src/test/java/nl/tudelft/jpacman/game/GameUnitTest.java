package nl.tudelft.jpacman.game;

import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
public class GameUnitTest {
    //定义一个玩家对象
    private Player player = Mockito.mock(Player.class);
    private Level level = Mockito.mock(Level.class);
    //单个积分计算器
    private PointCalculator pointCalculator = Mockito.mock(PointCalculator.class);
    //单人游戏
    private SinglePlayerGame singlePlayerGame;

    @BeforeEach
    void init() {
        singlePlayerGame = new SinglePlayerGame(player, level, pointCalculator);
    }

    /**
     * 对于此处，用来描述游戏时进行的，但是游戏角色的存亡以及豆子是否剩余，来对游戏的判断，其中大致分为
     * 几个方面，第一种情况，角色死亡，不管豆子是否剩余，游戏都是结束的。第二种情况就是玩家存活，而豆子
     * 的数量不为0，此时游戏仍然是继续进行的。第三种情况就是角色存活，但是豆子的数量为0，此时游戏是一种
     * 结束的状态
     */

    /**
     * 对于下面的这个函数，用来指明的是玩家是死亡的，而豆子有剩余量，对于这个剩余量，此处就用520表示，表明剩余520个，此处的游戏是结束的
     * 在Game类中，start的判断方法为if (getLevel().isAnyPlayerAlive() && getLevel().remainingPellets() > 0)
     */
    @Test
    public void notAlivePelletsHave() {
        Mockito.when(level.isAnyPlayerAlive()).thenReturn(false); //角色死亡，其返回false
        Mockito.when(level.remainingPellets()).thenReturn(520);   //豆子的数量
        singlePlayerGame.start();     //调用start函数
        assertFalse(singlePlayerGame.isInProgress());     //对于此处是用来判断Inprogress的进行，此处肯定是返回false，用断言来验证
    }
    /**
     * 对于下面的这个函数，用来指明的是玩家是存活的，而豆子有剩余量，对于这个剩余量，此处就用520表示，表明剩余520个，此处的游
     * 戏是进行的，玩家没死，豆子有剩余的。
     * 在Game类中，start的判断方法为if (getLevel().isAnyPlayerAlive() && getLevel().remainingPellets() > 0)
     */
    @Test
    public void alivePelletsHave() {
        Mockito.when(level.isAnyPlayerAlive()).thenReturn(true);   //角色存活，其返回true
        Mockito.when(level.remainingPellets()).thenReturn(520);    //豆子的数量
        singlePlayerGame.start();    //调用start函数
        assertTrue(singlePlayerGame.isInProgress());      //对于此处是用来判断Inprogress的进行，此处肯定是返回true，用断言来验证true
    }
    /**
     * 对于下面的这个函数，用来指明的是玩家是存活的，而豆子剩余量为0，此时的游戏是结束的，因为豆子已经被吃完，可以理解为游戏已通关
     * 在Game类中，start的判断方法为if (getLevel().isAnyPlayerAlive() && getLevel().remainingPellets() > 0)
     */
    @Test
    public void alivePelletsNotHave() {
        Mockito.when(level.isAnyPlayerAlive()).thenReturn(true);    //角色存活，其返回trueue
        Mockito.when(level.remainingPellets()).thenReturn(0);       //豆子的数量
        singlePlayerGame.start();                                   //调用start函数
        assertFalse(singlePlayerGame.isInProgress());               //对于此处是用来判断Inprogress的进行，此处肯定是返回false，用断言来验证treue
    }
}
