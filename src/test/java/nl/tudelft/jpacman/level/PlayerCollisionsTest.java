package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.npc.Ghost;
import nl.tudelft.jpacman.points.PointCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

/**
 *PlayerCollisionTest类用来验证是否正确处理了不同单元之间的冲突
 */
public class PlayerCollisionsTest  {

    /**
     * 初始化一个计分器，一个玩家，一个小球和一个幽灵以及一个PlayerCollisions对象用于测试
     * 使用Mockito.mock(classToMock)创建模拟对象
     */
    @BeforeEach
    void init() {
        this.setPointCalculator(Mockito.mock(PointCalculator.class));
        this.setPlayer(Mockito.mock(Player.class));
        this.setPellet(Mockito.mock(Pellet.class));
        this.setGhost(Mockito.mock(Ghost.class));
        //通过setPlayerCollisions()方法传入PlayerCollisions一个对象
        this.setPlayerCollisions(new PlayerCollisions(this.getPointCalculator()));
    }
    /**
     *创建所需对象
     */
    private PointCalculator pointCalculator;        //分数计分器
    private Player player;                          //玩家
    private Pellet pellet;                          //豆子
    private Ghost ghost;                            //幽灵
    private CollisionMap Collisions;                //冲突处理

    /**
     *创建get、set方法
     */
    public void setPointCalculator(PointCalculator pointCalculator) {
        this.pointCalculator = pointCalculator;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setPellet(Pellet pellet) {
        this.pellet = pellet;
    }

    public void setGhost(Ghost ghost) {
        this.ghost = ghost;
    }

    public void setPlayerCollisions(CollisionMap Collisions) {
        this.Collisions = Collisions;
    }

    public PointCalculator getPointCalculator() {
        return pointCalculator;
    }

    /**
     * 测试豆子与玩家之间的碰撞--吃掉+计分
     * Collisions.collide的第一个参数为player，第二个参数为pellet（被入侵）
     */
    @Test
    void testPlayerPellet() {
        Collisions.collide(player, pellet);
        //验证玩家与豆子相碰撞时，验证consumedAPellet（）方法至少发生一次，其功能是用来更新玩家分数
        Mockito.verify(pointCalculator, Mockito.times(1)).consumedAPellet(
            Mockito.eq(player),
            Mockito.eq(pellet)//参数匹配，等于所给定的值
        );
        //验证玩家与豆子相碰撞之后，玩家离开该方格空间后，验证leaveSquare()方法至少发生一次，其功能是将被吃掉的豆子移除棋盘
        Mockito.verify(pellet, Mockito.times(1)).leaveSquare();

        //确保行为均已通过验证
        Mockito.verifyNoMoreInteractions(player, pellet);
    }

    /**
     * 测试豆子与玩家之间的碰撞--吃掉+计分
     * playCollisions.collide的第一个参数为pellet，第二个参数为player（被入侵）
     */
    @Test
    void testPelletPlayer() {
        Collisions.collide(pellet, player);

        Mockito.verify(pointCalculator, Mockito.times(1)).consumedAPellet(
            Mockito.eq(player),
            Mockito.eq(pellet)
        );

        Mockito.verify(pellet, Mockito.times(1)).leaveSquare();

        Mockito.verifyNoMoreInteractions(pellet, player);
    }

    /**
     * 测试玩家与幽灵之间的碰撞--死亡
     * Collisions.collide的第一个参数为player，第二个参数为ghost
     */
    @Test
    void testPlayerGhost() {
        Collisions.collide(player, ghost);
        //验证玩家与幽灵相碰撞时，验证collidedWithAGhost（）方法至少发生一次，
        Mockito.verify(pointCalculator, Mockito.times(1)).collidedWithAGhost(
            Mockito.eq(player),
            Mockito.eq(ghost)
        );
        //当玩家与幽灵相遇后，玩家死掉，验证setAlive()方法至少发生一次，其功能是将玩家的状态设置为死亡
        Mockito.verify(player, Mockito.times(1)).setAlive(false);
        //玩家为死亡状态后，验证setKiller()方法至少发生一次，其功能是确定玩家的死亡状态
        Mockito.verify(player, Mockito.times(1)).setKiller(Mockito.eq(ghost));

        Mockito.verifyNoMoreInteractions(player, ghost);
    }


    /**
     * 测试玩家与幽灵之间的碰撞--死亡
     * Collisions.collide的第一个参数为ghost，第二个参数为player
     */
    @Test
    void testGhostPlayer() {
        Collisions.collide(ghost, player);

        Mockito.verify(pointCalculator, Mockito.times(1)).collidedWithAGhost(
            Mockito.eq(player),
            Mockito.eq(ghost)
        );

        Mockito.verify(player, Mockito.times(1)).setAlive(false);

        Mockito.verify(player, Mockito.times(1)).setKiller(Mockito.eq(ghost));

        Mockito.verifyNoMoreInteractions(player, ghost);
    }


    /**
     * 测试幽灵与豆子之间的碰撞--无任何事件发生
     * Collisions.collide的第一个参数为ghost，第二个参数为pellet
     */
    @Test
    void testGhostPellet() {
        Collisions.collide(ghost, pellet);
        Mockito.verify(pointCalculator, Mockito.times(0)).consumedAPellet(any(),any());
        Mockito.verifyZeroInteractions(ghost, pellet);
    }

    /**
     * 测试豆子与幽灵之间的碰撞--无任何事件发生
     * Collisions.collide的第一个参数为pellet，第二个参数为ghost
     */
    @Test
    void testPelletGhost() {
        Collisions.collide(pellet, ghost);
        Mockito.verify(pointCalculator, Mockito.times(0)).consumedAPellet(any(),any());
        Mockito.verifyZeroInteractions(pellet, ghost);
    }

    /**
     * 测试两个幽灵之间的碰撞--什么都不发生
     */
    @Test
    void testGhostGhost() {
        Ghost ghost1 = Mockito.mock(Ghost.class);
        Collisions.collide(ghost, ghost1);
        Mockito.verify(pointCalculator, Mockito.times(0)).consumedAPellet(any(),any());
        Mockito.verify(player, Mockito.times(0)).setAlive(false);
        Mockito.verifyZeroInteractions(ghost, ghost1);
    }


    /**
     * 测试豆子与豆子之间的碰撞--什么都不发生
     */
    @Test
    void testPelletPellet() {
        Pellet pellet1 = Mockito.mock(Pellet.class);
        Collisions.collide(pellet, pellet1);
        Mockito.verify(pointCalculator, Mockito.times(0)).consumedAPellet(any(),any());
        Mockito.verify(player, Mockito.times(0)).setAlive(false);
        Mockito.verifyZeroInteractions(pellet, pellet1);
    }


    /**
     * 测试两个玩家之间的碰撞--什么都不发生
     */
    @Test
    void testTwoPlayer() {
        Player player1 = Mockito.mock(Player.class);
        Collisions.collide(player, player1);
        Mockito.verify(pointCalculator, Mockito.times(0)).consumedAPellet(any(),any());
        Mockito.verify(player, Mockito.times(0)).setAlive(false);
        Mockito.verifyZeroInteractions(player, player1);
    }
}
