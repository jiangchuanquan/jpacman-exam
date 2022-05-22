package nl.tudelft.jpacman.npc.ghost;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.LevelFactory;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.level.PlayerFactory;
import nl.tudelft.jpacman.points.DefaultPointCalculator;
import nl.tudelft.jpacman.points.PointCalculator;
import nl.tudelft.jpacman.sprite.PacManSprites;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Class that tests the nextAiMove() method in the Clyde class.
 */
public class ClydeTest {

    private PacManSprites pacManSprites;
    private BoardFactory boardFactory;
    private GhostFactory ghostFactory;
    private PointCalculator pointCalculator;
    private LevelFactory levelFactory;
    private GhostMapParser ghostMapParser;
    private PlayerFactory playerFactory;

    /**
     * 构建所需的所有对象
     */
    @BeforeEach
    void init() {
        pacManSprites = new PacManSprites();            //用于角色显示
        boardFactory = new BoardFactory(pacManSprites);     //构造游戏场景
        ghostFactory = new GhostFactory(pacManSprites);     //鬼工厂，用于构造LevelFactory对象
        pointCalculator = new DefaultPointCalculator();     //一个简单的，极简的点计算器,用于构造LevelFactory对象
        levelFactory = new LevelFactory(pacManSprites, ghostFactory, pointCalculator);
        playerFactory = new PlayerFactory(pacManSprites);
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);  //构造GhostMapParser对象（用于快速生成地图）
    }


    /**
     * 间隔小于8，Clyde会远离Player
     */
    @Test
    void testTooClose() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "############", "#P       C##", "############"
            )  //使用GhostMapParser的parseMap()方法接收字符串数组并返回一个level对象
        );
        Player player = playerFactory.createPacMan();       //使用PlayerFactory生成Player对象
        player.setDirection(Direction.EAST);                //设置player的初始方向
        level.registerPlayer(player);                       //在这个关卡上，注册player，分配他到起始位置
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());    //通过Navigation.findUnitInBoard()获取在该关卡中已经创建好的Clyde对象

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));  //间隔小于8，则远离player
    }


    /**
     * 间隔大于8，Clyde会靠近Player
     */
    @Test
    void testTooFar() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "############", "C         P#", "############"
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));  //间隔大于8，则靠近player
    }

    /**
     * Player当前不在该关卡
     */
    @Test
    void testNoPlayer() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "#####", "##C  ", "     "
            )
        );
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());     //在该关卡为找到Player，则不会采取行为
    }


    /**
     * Player与Clyde之间不存在有效路径
     */
    @Test
    void testNoPathBetweenPlayerAndClyde() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "######", "#P##C ", " ###  "
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Clyde clyde = Navigation.findUnitInBoard(Clyde.class, level.getBoard());

        assertThat(clyde.nextAiMove()).isEqualTo(Optional.empty());     //Player在该关卡，但与Clyde之间没有有效路径
    }
}
