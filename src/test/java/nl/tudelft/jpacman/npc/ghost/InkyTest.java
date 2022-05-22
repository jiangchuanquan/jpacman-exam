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
 * 测试Inky的nextAiMove()方法 */
public class InkyTest {

    private PacManSprites pacManSprites;
    private LevelFactory levelFactory;
    private GhostFactory ghostFactory;
    private PlayerFactory playerFactory;
    private BoardFactory boardFactory;
    private GhostMapParser ghostMapParser;
    private PointCalculator pointCalculator;

    /**
     * 创建游戏所需的所有对象
     */
    @BeforeEach
    void Init() {
        pacManSprites = new PacManSprites();            //用于角色显示
        boardFactory = new BoardFactory(pacManSprites);     //构造游戏场景
        ghostFactory = new GhostFactory(pacManSprites);     //鬼工厂，用于构造LevelFactory对象
        pointCalculator = new DefaultPointCalculator();     //一个简单的，极简的点计算器,用于构造LevelFactory对象
        levelFactory = new LevelFactory(pacManSprites, ghostFactory, pointCalculator);
        playerFactory = new PlayerFactory(pacManSprites);
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);  //构造GhostMapParser对象（用于快速生成地图）
    }


    /**
     * 当游戏地图中没有Blinky时，Inky无法根据Blinky采取行动
     */
    @Test
    void testNoBlinky() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("###PI ", "######")
        );  //使用GhostMapParser的parseMap()方法接收字符串数组并返回一个level对象
        Player player = playerFactory.createPacMan();     //使用PlayerFactory生成Player对象
        player.setDirection(Direction.WEST);              //为player设置一个新初始方向
        level.registerPlayer(player);                     //将其player对象注册到该地图上
        //通过调用Navigation的findUnitInBoard()获取在该关卡中已经创建好的Inky对象
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }

    /**
     * 当player、inky、blinky都在地图中，但player与inky之间没有有效路径，则无法采取行动
     */
    @Test
    void testNoPath() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("############", "  #P#  I  B#", "############")
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());

    }


    /**
     * 当地图上没有player时，inky也无法采取行动
     */
    @Test
    void testNoPlayer() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("####", "B  I", "####")
        );
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }


    /**
     * 当player、inky、blinky都在地图中，且存在有效路径
     * Inky跟在Blinky之后，追随Player
     */
    @Test
    void testGoTowardsPlayer() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "#################","#    P    B   I #","#################"
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());
        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.WEST));
    }


    /**
     * 当player、inky、blinky都在地图中，且存在有效路径
     * Inky在Player之前，且Blinky在Inky之后，则Inky将远离Player
     */
    @Test
    void testInkyMovesAway() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
            "##############","#  B P  I    #","##############"
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.EAST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.of(Direction.EAST));

    }

    /**
     * 当player、inky、blinky都在地图中，且存在有效路径
     * Inky在Player与Binky之间，则Inky将靠近Player
     */
    @Test
    void testInkyMove(){
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList(
                "#########","# P  I B#","#########"
            )
        );
        Player player = playerFactory.createPacMan();
        player.setDirection(Direction.WEST);
        level.registerPlayer(player);
        Inky inky = Navigation.findUnitInBoard(Inky.class,level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.ofNullable(Direction.WEST));
    }
}
