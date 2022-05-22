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
 * ����Inky��nextAiMove()���� */
public class InkyTest {

    private PacManSprites pacManSprites;
    private LevelFactory levelFactory;
    private GhostFactory ghostFactory;
    private PlayerFactory playerFactory;
    private BoardFactory boardFactory;
    private GhostMapParser ghostMapParser;
    private PointCalculator pointCalculator;

    /**
     * ������Ϸ��������ж���
     */
    @BeforeEach
    void Init() {
        pacManSprites = new PacManSprites();            //���ڽ�ɫ��ʾ
        boardFactory = new BoardFactory(pacManSprites);     //������Ϸ����
        ghostFactory = new GhostFactory(pacManSprites);     //���������ڹ���LevelFactory����
        pointCalculator = new DefaultPointCalculator();     //һ���򵥵ģ�����ĵ������,���ڹ���LevelFactory����
        levelFactory = new LevelFactory(pacManSprites, ghostFactory, pointCalculator);
        playerFactory = new PlayerFactory(pacManSprites);
        ghostMapParser = new GhostMapParser(levelFactory, boardFactory, ghostFactory);  //����GhostMapParser�������ڿ������ɵ�ͼ��
    }


    /**
     * ����Ϸ��ͼ��û��Blinkyʱ��Inky�޷�����Blinky��ȡ�ж�
     */
    @Test
    void testNoBlinky() {
        Level level = ghostMapParser.parseMap(
            Lists.newArrayList("###PI ", "######")
        );  //ʹ��GhostMapParser��parseMap()���������ַ������鲢����һ��level����
        Player player = playerFactory.createPacMan();     //ʹ��PlayerFactory����Player����
        player.setDirection(Direction.WEST);              //Ϊplayer����һ���³�ʼ����
        level.registerPlayer(player);                     //����player����ע�ᵽ�õ�ͼ��
        //ͨ������Navigation��findUnitInBoard()��ȡ�ڸùؿ����Ѿ������õ�Inky����
        Inky inky = Navigation.findUnitInBoard(Inky.class, level.getBoard());

        assertThat(inky.nextAiMove()).isEqualTo(Optional.empty());
    }

    /**
     * ��player��inky��blinky���ڵ�ͼ�У���player��inky֮��û����Ч·�������޷���ȡ�ж�
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
     * ����ͼ��û��playerʱ��inkyҲ�޷���ȡ�ж�
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
     * ��player��inky��blinky���ڵ�ͼ�У��Ҵ�����Ч·��
     * Inky����Blinky֮��׷��Player
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
     * ��player��inky��blinky���ڵ�ͼ�У��Ҵ�����Ч·��
     * Inky��Player֮ǰ����Blinky��Inky֮����Inky��Զ��Player
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
     * ��player��inky��blinky���ڵ�ͼ�У��Ҵ�����Ч·��
     * Inky��Player��Binky֮�䣬��Inky������Player
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
